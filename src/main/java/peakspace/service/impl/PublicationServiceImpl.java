package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.LinkPublicationResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.HomePageResponse;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.LinkResponse;
import peakspace.dto.response.PostLinkResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.entities.Chapter;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.entities.Notification;
import peakspace.enums.Role;
import peakspace.exception.AccountIsBlock;
import peakspace.exception.BadRequestException;
import peakspace.exception.NotFoundException;
import peakspace.repository.*;
import peakspace.service.PublicationService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public GetAllPostsResponse getAllPosts(Principal principal) {
        User user = userRepository.getByEmail(principal.getName());
        Map<Long, String> publics = user.getPublications().stream()
                .filter(publication -> publication.getPablicProfile() == null)
                .collect(Collectors.toMap(
                        Publication::getId,
                        publication -> {
                            List<Link_Publication> linkPublications = publication.getLinkPublications();
                            return linkPublications.isEmpty() ? "" : linkPublications.getFirst().getLink();
                        }
                ));

        int countPablics = 0;
        if (user.getPablicProfiles() != null) {
            countPablics = user.getPablicProfiles().getUsers().size();
        }
        return GetAllPostsResponse.builder()
                .cover(user.getProfile().getCover())
                .avatar(user.getProfile().getAvatar())
                .userName(user.getUsername())
                .aboutMe(user.getProfile().getAboutYourSelf())
                .major(user.getProfile().getProfession())
                .countFriends(user.getChapters().size())
                .countPablics(countPablics)
                .publications(publics)
                .build();

    }



    @Override
    public MyPostResponse getById(Long postId) {
        publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой пост !"));
        return getMyPost(postId);
    }


    @Override
    public List<PublicationWithYouResponse> withPhoto(Long foundUserId) {
        User foundUser = userRepository.findByIds(foundUserId);

        if (foundUser.getIsBlock() && !thisUserMyFriend(foundUser)) {
            throw new AccountIsBlock("Закрытый аккаунт!");
        }

        List<Long> myAcceptPost = foundUser.getMyAcceptPost();
        List<PublicationWithYouResponse> publicationsWithYou = new ArrayList<>();

        for (Long postId : myAcceptPost) {
            Publication publication = publicationRepository.findPostById(postId);
            PublicationWithYouResponse publicationWithYouResponse = new PublicationWithYouResponse();
            publicationWithYouResponse.setId(publication.getId());
            publicationWithYouResponse.setDescription(publication.getDescription());
            publicationWithYouResponse.setLocation(publication.getLocation());

            List<LinkPublicationResponse> linkPublicationResponses = publication.getLinkPublications().stream()
                    .map(linkPublication -> {
                        LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                        linkPublicationResponse.setId(linkPublication.getId());
                        linkPublicationResponse.setLink(linkPublication.getLink());
                        return linkPublicationResponse;
                    })
                    .collect(Collectors.toList());

            publicationWithYouResponse.setLinks(linkPublicationResponses);
            publicationsWithYou.add(publicationWithYouResponse);
        }
        return publicationsWithYou;
    }



    private boolean thisUserMyFriend(User user) {
        for (Chapter chapter : user.getChapters()) {
            for (User friend : chapter.getFriends()) {
                if (friend.getId().equals(getCurrentUser().getId()))
                    return true;
            }
        }
        return false;
    }
    @Override
    @Transactional
    public List<PublicationResponse> findAllPublic(Long friendId) {

        User referenceById = userRepository.getReferenceById(friendId);
        if (referenceById.getIsBlock() && !thisUserMyFriend(referenceById)){
            throw new AccountIsBlock("Закрытый аккаунт! ");
        }

        List<Publication> friendsPublic = userRepository.findFriendsPub(friendId);
        List<PublicationResponse> allPublications = new ArrayList<>();

        for (Publication publication : friendsPublic) {
            if (publication.getPablicProfile() == null) {
                PublicationResponse publicationResponse = new PublicationResponse();
                publicationResponse.setId(publication.getId());

                List<Link_Publication> linkPublications = publication.getLinkPublications();
                List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();

                for (Link_Publication linkPublication : linkPublications) {
                    LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                    linkPublicationResponse.setId(linkPublication.getId());
                    linkPublicationResponse.setLink(linkPublication.getLink());
                    linkPublicationResponses.add(linkPublicationResponse);
                }
                publicationResponse.setLinkPublications(linkPublicationResponses);
                allPublications.add(publicationResponse);
            }
        }
        return allPublications;
    }

    @Override
    public List<HomePageResponse> homePage() {
        User currentUser = getCurrentUser();
        List<HomePageResponse> homePages = new ArrayList<>();

        List<Publication> currentUserPublications = currentUser.getPublications().stream()
                .filter(publication -> publication.getPablicProfile() == null)
                .toList();

        List<Publication> allPublications = new ArrayList<>(currentUserPublications);

        for (Chapter chapter : currentUser.getChapters()) {
            for (Publication friendPublication : chapter.getFriends().stream()
                    .flatMap(friend -> friend.getPublications().stream())
                    .filter(publication -> publication.getPablicProfile() == null)
                    .toList()) {
                if (!allPublications.contains(friendPublication)) {
                    allPublications.add(friendPublication);
                }
            }
        }

        allPublications.sort(Comparator.comparing(Publication::getCreatedAt).reversed());

        for (Publication publication : allPublications) {
            List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();
            for (Link_Publication linkPublication : publication.getLinkPublications()) {
                linkPublicationResponses.add(new LinkPublicationResponse(linkPublication.getId(), linkPublication.getLink()));
            }
            HomePageResponse homePageResponse = HomePageResponse.builder()
                    .id(publication.getOwner().getId())
                    .avatar(publication.getOwner().getProfile().getAvatar())
                    .username(publication.getOwner().getUsername())
                    .location(publication.getLocation())
                    .postId(publication.getId())
                    .description(publication.getDescription())
                    .linkPublicationResponseList(linkPublicationResponses)
                    .countLikes(publication.getLikes().size())
                    .countComments(publication.getComments().size())
                    .build();

            homePages.add(homePageResponse);
        }

        return homePages;
    }

    public MyPostResponse getMyPost(Long postId) {
        Publication publication = publicationRepository.getReferenceById(postId);
        List<CommentResponse> commentForResponse = commentRepository.getCommentForResponse(publication.getId());
        commentForResponse.reversed();

        List<LinkResponse> links = publication.getLinkPublications().stream()
                .map(link -> new LinkResponse(link.getId(), link.getLink()))
                .collect(Collectors.toList());

        return MyPostResponse.builder()
                .id(publication.getId())
                .userId(publication.getOwner().getId())
                .avatar(publication.getOwner().getProfile().getAvatar())
                .userName(publication.getOwner().getThisUserName())
                .location(publication.getLocation())
                .countLikes(publication.getLikes().size())
                .links(links)
                .commentResponses(commentForResponse)
                .build();
    }

    @Override
    public PostLinkResponse findInnerPost(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой post !"));

        List<LinkResponse> linkResponses = publication.getLinkPublications().stream()
                .map(link -> new LinkResponse(link.getId(), link.getLink()))
                .toList();

        return PostLinkResponse.builder()
                .postId(publication.getId())
                .linkResponses(linkResponses)
                .countLikes(publication.getLikes().size())
                .countComments(publication.getComments().size())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }

    @Override
    @Transactional
    public SimpleResponse saveComplainToPost(Long postId, String complain) {
        Publication publication = publicationRepository.getReferenceById(postId);

        if (publication.getComplains().containsKey(getCurrentUser().getId())) {
            throw new BadRequestException("Вы уже оставили жалобу!");
        }

        publication.getComplains().put(getCurrentUser().getId(), complain);

        Notification notification = new Notification();
        notification.setSenderUserId(getCurrentUser().getId());
        notification.setUserNotification(publication.getPablicProfile().getUser());
        notification.setNotificationMessage("оставил(-а) на этот пост жалоб!: " + complain);
        notificationRepository.save(notification);

        if (publication.getComplains().size() >= 10) {
            publicationRepository.save(publication);
            Notification notification1 = new Notification();
            notification1.setSenderUserId(publication.getPablicProfile().getUser().getId());
            notification1.setUserNotification(publication.getOwner());
            notification1.setNotificationMessage("Мы удалили вашу публикацию, потому что ее жаловали 10 раз!");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Мы удалили публикацию, потому что ее жаловали 10 раз!")
                    .build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved complain!")
                .build();
    }

}