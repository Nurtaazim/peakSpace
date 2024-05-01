package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.FavoritePostResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserMarkResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Notification;
import peakspace.entities.Publication;
import peakspace.entities.User;
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
    public SimpleResponse delete(Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if (publication.getOwner().getId().equals(user.getId())) {
                if (publication.getId().equals(postId)) {
                    publicationRepo.deleteCom(postId);
                    publicationRepo.deleteLink(postId);
                    publicationRepo.deleteTag(postId);
                    publicationRepo.deleteLike(postId);
                    publicationRepo.deletePublic(publication.getId());
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
                    linkPublicationRepo.deleteById(linkId);
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


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
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
    public FavoritePostResponse favorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        List<Publication> allById = publicationRepo.findAllById(user.getProfile().getFavorites());

        Map<Long, String> publics = allById.stream()
                .collect(Collectors.toMap(
                        Publication::getId,
                        publication -> publication.getLinkPublications().getFirst().getLink()
                ));

        return FavoritePostResponse.builder()
                .publications(publics)
                .build();
    }
}