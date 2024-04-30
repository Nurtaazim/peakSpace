package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserMarkResponse;
import peakspace.entities.User;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.Notification;
import peakspace.entities.PablicProfile;
import peakspace.enums.Role;
import peakspace.exception.BadRequestException;
import peakspace.exception.NotFoundException;
import peakspace.repository.LinkPublicationRepo;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PostService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final LinkPublicationRepo linkPublicationRepo;
    private final PublicationRepository publicationRepo;

    @Override
    public SimpleResponse savePost(PostRequest postRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        List<Link_Publication> linkPublications = new ArrayList<>();
        for (int i = 0; i < postRequest.getLinks().size(); i++) {
            Link_Publication linkPublication = new Link_Publication();
            linkPublication.setLink(postRequest.getLinks().get(i));
            linkPublications.add(linkPublication);
            linkPublicationRepo.save(linkPublication);
        }
        Publication publication = new Publication();
        publication.setDescription(postRequest.getDescription());
        publication.setLocation(postRequest.getLocation());
        publication.setBlockComment(postRequest.isBlockComment());
        publication.setLinkPublications(linkPublications);
        publication.setBlockComment(postRequest.isBlockComment());
        publication.setOwner(user);

        publicationRepo.save(publication);
        user.getPublications().add(publication);
        userRepository.save(user);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse update(Long postId, PostUpdateRequest postUpdateRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if (publication.getId().equals(postId)) {
                publication.setDescription(postUpdateRequest.getDescription());
                publication.setLocation(postUpdateRequest.getLocation());
                publication.setBlockComment(postUpdateRequest.isBlockComment());
                publication.setUpdatedAt(ZonedDateTime.now());
                break;
            }
        }
        return SimpleResponse.builder()
                .message("Successfully updated!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if (publication.getOwner().getId().equals(user.getId())) {
                if (publication.getId().equals(postId)) {
                    publicationRepo.deleteComNotifications(postId);
                    publicationRepo.deleteCom(postId);
                    publicationRepo.deleteByIds(publication.getId());
                }
            }
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully deleted!")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse deleteLinkFromPost(Long linkId, Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if (publication.getId().equals(postId)) {
                if (publication.getOwner().getId().equals(user.getId())) {
                    List<Link_Publication> photots = publication.getLinkPublications();
                    List<Link_Publication> orig = new ArrayList<>();
                    for (Link_Publication phot : photots) {
                        if (!phot.getId().equals(linkId)) {
                            orig.add(phot);
                        }
                    }
                    publication.setLinkPublications(orig);
                    publicationRepo.deletePublicationLink(postId, linkId);

                }
            }
        }
        return SimpleResponse.builder()
                .message("Successfully deleted!")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Override
    public SimpleResponse notationFriend(Long postId, List<Long> foundUserId) {
        User owner = getCurrentUser();
        List<UserMarkResponse> foundUsers = userRepository.findFoundUserId(foundUserId);
        if (foundUserId.contains(owner.getId())) {
            throw new BadRequestException("Нельзя отмечать себя!");
        }

        Publication publication = publicationRepo.findByIdAndOwner(postId, owner);
        if (publication == null) {
            throw new NotFoundException(" Нет такой публикации !");
        }
        for (UserMarkResponse userMarkResponse : foundUsers) {
            User markUser = userRepository.findById(userMarkResponse.id()).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
            if (publication.getTagFriends().contains(markUser)) {
                throw new BadRequestException("Уже отмечали друга " + markUser.getThisUserName());
            }
            publication.getTagFriends().add(markUser);

            Notification notification = new Notification();
            notification.setNotificationMessage(" Хочет выложить фото с вами!");
            notification.setUserNotification(markUser);
            notification.setSeen(false);
            notification.setCreatedAt(ZonedDateTime.now());
            notification.setSenderUserId(owner.getId());
            markUser.getNotifications().add(notification);
        }
        publicationRepo.save(publication);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Друзья успешно отмечены!")
                .build();
    }

    @Override
    public SimpleResponse removeNotation(List<Long> friendsId) {
        User owner = getCurrentUser();
        List<UserMarkResponse> foundUser = userRepository.findFoundUserId(friendsId);

        for (Publication publication : owner.getPublications()) {
            for (UserMarkResponse userMarkResponse : foundUser) {
                User markUser = userRepository.findById(userMarkResponse.id()).orElse(null);
                publication.getTagFriends().remove(markUser);
            }
        }
        publicationRepo.saveAll(owner.getPublications());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Друзья успешно удален из отметки !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse addFavorite(Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        Publication post = publicationRepo.findPostById(postId);
        if (user.getProfile().getFavorites().contains(post.getId())) {
            user.getProfile().getFavorites().remove(post.getId());
        } else {
            user.getProfile().getFavorites().add(post.getId());
        }
        return SimpleResponse.builder()
                .message("Successfully !")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public GetAllPostsResponse favorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        List<Publication> allById = publicationRepo.findAllById(user.getProfile().getFavorites());

        Map<Long, String> publics = allById.stream()
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
                .countPablics(user.getPublications().size())
                .publications(publics)
                .build();
    }


    @Override @Transactional
    public SimpleResponse savePostPublic(PostRequest postRequest) {
        User currentUser = getCurrentUser();
        PablicProfile publicProfile = Optional.ofNullable(currentUser.getPablicProfiles())
                .orElseThrow(() -> new NotFoundException(" Нету паблик у вас!"));

        List<Link_Publication> linkPublications = postRequest.getLinks().stream()
                .map(link -> {
                    Link_Publication linkPublication = new Link_Publication();
                    linkPublication.setLink(link);
                    linkPublicationRepo.save(linkPublication);
                    return linkPublication;
                })
                .collect(Collectors.toList());

        Publication publication = new Publication();
        publication.setDescription(postRequest.getDescription());
        publication.setLocation(postRequest.getLocation());
        publication.setBlockComment(postRequest.isBlockComment());
        publication.setLinkPublications(linkPublications);
        publication.setOwner(currentUser);
        publication.setPablicProfile(publicProfile);
        publicationRepo.save(publication);
        currentUser.getPublications().add(publication);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно сохранен пост паблике !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse editPostPublic(Long postId, PostUpdateRequest postUpdateRequest) {
        User currentUser = getCurrentUser();

        Optional<Publication> publicationOptional = currentUser.getPublications().stream()
                .filter(publication -> publication.getId().equals(postId))
                .findFirst();

        if (publicationOptional.isPresent()) {
            Publication publication = publicationOptional.get();
            publication.setDescription(postUpdateRequest.getDescription());
            publication.setLocation(postUpdateRequest.getLocation());
            publication.setBlockComment(postUpdateRequest.isBlockComment());
            publication.setUpdatedAt(ZonedDateTime.now());
            return SimpleResponse.builder()
                    .message(" Успешно сохранен пост паблике!")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            throw new NotFoundException(" Нет такой публикации на паблике !");
        }
    }

    @Override @Transactional
    public SimpleResponse deletePostPublic(Long postId) {
        User currentUser = getCurrentUser();

        Optional<Publication> publicationToRemove = currentUser.getPablicProfiles().getPublications().stream()
                .filter(publication -> publication.getId().equals(postId))
                .findFirst();

        if (publicationToRemove.isPresent()) {
            Publication publication = publicationToRemove.get();
            publicationRepo.deleteComNotifications(postId);
            publicationRepo.deleteCom(postId);
            publicationRepo.deleteByIds(publication.getId());

            System.err.println("Test");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message(" Удачно удаление !")
                    .build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(" Нет такой публикации на паблике !")
                    .build();
        }
    }

    @Override @Transactional
    public SimpleResponse deleteLinkFromPostPublic(Long linkId, Long postId) {
        User currentUser = getCurrentUser();

        currentUser.getPablicProfiles().getPublications().stream()
                .filter(publication -> publication.getId().equals(postId))
                .filter(publication -> publication.getOwner().getId().equals(currentUser.getId()))
                .findFirst()
                .ifPresent(publication -> {
                    List<Link_Publication> links = publication.getLinkPublications().stream()
                            .filter(link -> !link.getId().equals(linkId))
                            .collect(Collectors.toList());
                    publication.setLinkPublications(links);
                    publicationRepo.deletePublicationLink(postId, linkId);

                });

        return SimpleResponse.builder()
                .message("Successfully deleted!")
                .httpStatus(HttpStatus.OK)
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