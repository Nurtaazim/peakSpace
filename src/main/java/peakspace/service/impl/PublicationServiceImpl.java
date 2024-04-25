package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.*;
import peakspace.entities.*;
import peakspace.repository.CommentRepository;
import peakspace.enums.Role;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PublicationService;

import java.util.*;
import java.security.Principal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public GetAllPostsResponse getAllPosts(Principal principal) {
        User user = userRepository.getByEmail(principal.getName());
        Map<Long, String> publics = user.getPublications().stream()
                .collect(Collectors.toMap(
                        Publication::getId,
                        publication -> publication.getLinkPublications().getFirst().getLink()
                ));
        return GetAllPostsResponse.builder()
                .cover(user.getProfile().getCover())
                .avatar(user.getProfile().getAvatar())
                .userName(user.getUsername())
                .aboutMe(user.getProfile().getAboutYourSelf())
                .major(user.getProfile().getProfession())
                .countFriends(user.getChapters().size())
                .countPablics(user.getPablicProfiles().size())
                .publications(publics)
                .build();

    }

    public List<PublicationWithYouResponse> withPhoto(Long foundUserId) {
        User foundUser = userRepository.findByIds(foundUserId);
        List<PublicationWithYouResponse> publicationsWithYou = new ArrayList<>();

        if (foundUser != null) {
            for (Publication publication : foundUser.getPublications()) {
                if (publication.getTagFriends().stream().anyMatch(user -> user.getId().equals(getCurrentUser().getId()))) {
                    PublicationWithYouResponse publicationWithYouResponse = new PublicationWithYouResponse();
                    publicationWithYouResponse.setId(publication.getId());
                    publicationWithYouResponse.setDescription(publication.getDescription());
                    publicationWithYouResponse.setLocation(publication.getLocation());

                    List<Link_Publication> linkPublications = publication.getLinkPublications();
                    List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();

                    if (linkPublications != null && !linkPublications.isEmpty()) {
                        for (Link_Publication linkPublication : linkPublications) {
                            LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                            linkPublicationResponse.setId(linkPublication.getId());
                            linkPublicationResponse.setLink(linkPublication.getLink());
                            linkPublicationResponses.add(linkPublicationResponse);
                        }
                    }

                    publicationWithYouResponse.setLinks(linkPublicationResponses);
                    publicationsWithYou.add(publicationWithYouResponse);
                }
            }
        }

        return publicationsWithYou;
    }

    @Override
    public MyPostResponse getById(Long postId) {
        MyPostResponse myPost = getMyPost(postId);
        for (CommentResponse commentResponse : myPost.commentResponses()) {
            commentResponse.setInnerComments(commentRepository.getInnerComments(commentResponse.getId()));
        }
        return myPost;
    }

    @Override
    public List<PublicationResponse> findAllPublic(Long friendId) {

        List<Publication> friendsPublic = userRepository.findFriendsPub(friendId);
        List<PublicationResponse> allPublications = new ArrayList<>();
        for (Publication publication : friendsPublic) {
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
//            publicationResponse.setLinkPublications(linkPublicationResponses);
            allPublications.add(publicationResponse);
        }
        return allPublications;
    }

    @Override
    public List<HomePageResponse> homePage() {
        User currentUser = getCurrentUser();
        List<HomePageResponse> homePages = new ArrayList<>();
        List<Publication> allPublications = new ArrayList<>();

        allPublications.addAll(currentUser.getPublications());

        for (Chapter chapter : currentUser.getChapters()) {
            allPublications.addAll(chapter.getFriends().stream()
                    .flatMap(friend -> friend.getPublications().stream())
                    .collect(Collectors.toList()));
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
        return MyPostResponse.builder()
                .id(publication.getId())
                .userId(publication.getOwner().getId())
                .links(publication.getLinkPublications().stream().map(Link_Publication::getLink).collect(Collectors.toList()))
                .countLikes(publication.getLikes().size())
                .avatar(publication.getOwner().getProfile().getAvatar())
                .userName(publication.getOwner().getThisUserName())
                .location(publication.getLocation())
                .commentResponses(commentForResponse)
                .build();

    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }

}
