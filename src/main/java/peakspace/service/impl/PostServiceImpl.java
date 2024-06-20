package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.config.amazonS3.AwsS3Service;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.*;
import peakspace.entities.User;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.Notification;
import peakspace.enums.Role;
import peakspace.exception.BadRequestException;
import peakspace.exception.NotFoundException;
import peakspace.repository.LinkPublicationRepo;
import peakspace.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;
    private final AwsS3Service awsS3Service;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
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
        Publication publication = publicationRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException("Публикация с таким айди не найдено"));
        if (publication.getOwner().equals(user)) {

            // Удалить связанные записи из comments_notifications
            String deleteNotificationsQuery = "DELETE FROM comments_notifications WHERE notifications_id IN (SELECT id FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?))";
            jdbcTemplate.update(deleteNotificationsQuery, publication.getId());

            // Удалить связанные записи из notifications
            String deleteNotificationsQuery1 = "DELETE FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteNotificationsQuery1, publication.getId());

            // Удалить связанные записи из comments_likes
            String deleteCommentsLikesSQL = "DELETE FROM comments_likes WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteCommentsLikesSQL, publication.getId());

            // Удалить связанные записи из inner_comment
            String deleteInnerCommentsSql = "DELETE FROM inner_comment WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteInnerCommentsSql, publication.getId());

            // Удалить комментарии
            String deleteCommentsSQL = "DELETE FROM comments WHERE publication_id = ?";
            jdbcTemplate.update(deleteCommentsSQL, publication.getId());

            notificationRepository.deleteByPublicationId(publication.getId());
            publicationRepo.delete(publication);

            for (Link_Publication linkPublication : publication.getLinkPublications()) {
                awsS3Service.deleteFile(linkPublication.getLink());
            }

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully deleted!")
                    .build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .message("У вас нету прав удалить чужие публикации")
                .build();
    }



    @Transactional
    @Override
    public SimpleResponse notationFriend(Long postId, List<Long> foundUserIds) {
        User owner = getCurrentUser();

        if (foundUserIds.contains(owner.getId())) {
            throw new BadRequestException("Нельзя отмечать себя!");
        }

        Publication publication = publicationRepo.findByIdAndOwner(postId, owner.getId());
        if (publication == null) {
            throw new NotFoundException("Нет такой публикации!");
        }

        for (User tagFriend : publication.getTagFriends()) {
            if (foundUserIds.contains(tagFriend.getId())) {
                throw new BadRequestException(" Уже отмечен друг !");
            }
        }

        List<User> usersToMark = foundUserIds.stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!")))
                .filter(user -> !publication.getTagFriends().contains(user))
                .collect(Collectors.toList());

        if (!usersToMark.isEmpty()) {
            for (User user : usersToMark) {
                publication.getTagFriends().add(user);
                Notification notification = createNotification(owner, user);
                user.getNotifications().add(notification);
            }
            publicationRepo.save(publication);
        }

        return SimpleResponse.builder()
                .httpStatus(usersToMark.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .message(usersToMark.isEmpty() ? "Отмечать некого" : "Друзья успешно отмечены!")
                .build();
    }

    private Notification createNotification(User owner, User user) {
        Notification notification = new Notification();
        notification.setNotificationMessage(owner.getUsername() + " хочет выложить фото с вами!");
        notification.setUserNotification(user);
        notification.setSeen(false);
        notification.setCreatedAt(ZonedDateTime.now());
        notification.setSenderUserId(owner.getId());
        return notification;
    }


    @Override
    public SimpleResponse removeNotation(Long postId, List<Long> friendsId) {
        User owner = getCurrentUser();
        Publication publication = publicationRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException("Нет такой публикации!"));

        if (publication.getOwner().getId().equals(owner.getId())) {
            List<Long> friendIdsToRemove = friendsId.stream()
                    .filter(id -> publication.getTagFriends().stream()
                            .anyMatch(user -> user.getId().equals(id)))
                    .collect(Collectors.toList());

            if (friendIdsToRemove.size() < friendsId.size()) {
                throw new NotFoundException("Один или несколько друзей не найдены в списке отмеченных друзей!");
            }

            publication.getTagFriends().removeIf(user -> friendIdsToRemove.contains(user.getId()));
            publicationRepo.save(publication);
        } else {
            throw new BadRequestException(" Это не твой публикация !");
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Друзья успешно удалены из отметки!")
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
    public FavoritePostResponse favorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        List<Long> favoriteIds = user.getProfile().getFavorites();

        List<Publication> favoritePublications = publicationRepo.findAllById(favoriteIds);

        List<Map<Long, String>> publications = favoritePublications.stream()
                .map(publication -> {
                    List<Link_Publication> linkPublications = publication.getLinkPublications();
                    String link = linkPublications.stream()
                            .filter(linkPublication -> !linkPublication.getLink().isEmpty())
                            .findFirst()
                            .map(Link_Publication::getLink)
                            .orElse("");
                    return Map.of(publication.getId(), link);
                })
                .collect(Collectors.toList());

        return FavoritePostResponse.builder()
                .publications(publications.reversed())
                .build();
    }


    @Override
    @Transactional
    public SimpleResponse editPostPublic(Long postId, PostUpdateRequest postUpdateRequest) {
        User currentUser = getCurrentUser();
        Publication publication = publicationRepo.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой пост !"));

        if (publication.getOwner().equals(currentUser) || currentUser.getCommunity().getPublications().contains(publication)) {
            publication.setDescription(postUpdateRequest.getDescription());
            publication.setLocation(postUpdateRequest.getLocation());
            publication.setUpdatedAt(ZonedDateTime.now());
            return SimpleResponse.builder()
                    .message(" Успешно изменен пост паблике!")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            throw new NotFoundException(" Нет такой публикации на паблике !");
        }
    }

    @Override
    @Transactional
    public SimpleResponse deletePostPublic(Long postId) {
        User currentUser = getCurrentUser();
        Publication publication = publicationRepo.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой пост !"));
        if (publication.getPablicProfile() == null)
            throw new BadRequestException("Это публикация не состоит в сообществе");
        if (publication.getOwner().equals(currentUser) || currentUser.equals(publication.getPablicProfile().getOwner())) {
            String deleteCommentsNotificationsQuery = "DELETE FROM comments_notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteCommentsNotificationsQuery, postId);

            String deleteNotificationsQuery = "DELETE FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteNotificationsQuery, postId);

            String deleteCommentsLikesSQL = "DELETE FROM comments_likes WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteCommentsLikesSQL, postId);
            String deleteInnerCommentsSql = "DELETE FROM inner_comment WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteInnerCommentsSql, postId);

            String deleteCommentsSQL = "DELETE FROM comments WHERE publication_id = ?";
            jdbcTemplate.update(deleteCommentsSQL, postId);
            notificationRepository.deleteByPublicationId(postId);
            publicationRepo.deleteByIds(publication.getId());

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message(" Пост успешно удален !")
                    .build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message(" Вы не можете удалить эту публикацию у вас нету доступа !")
                    .build();
        }
    }


    @Transactional
    @Override
    public SimpleResponse acceptTagFriend(Long postId, boolean tag) {
        getCurrentUser();
        Publication post = publicationRepo.findPostById(postId);
        if (tag) {
            getCurrentUser().getMyAcceptPost().add(post.getId());
        } else {
            post.getTagFriends().remove(getCurrentUser());
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно  одобрено !")
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