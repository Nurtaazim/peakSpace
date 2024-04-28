package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.entities.Like;
import peakspace.entities.Notification;
import peakspace.entities.Comment;
import peakspace.entities.Story;
import peakspace.exception.NotFoundException;
import peakspace.repository.PublicationRepository;
import peakspace.repository.LikeRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.CommentRepository;
import peakspace.repository.StoryRepository;
import peakspace.repository.NotificationRepository;
import peakspace.service.LikeService;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final PublicationRepository publicationRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final NotificationRepository notificationRepository;
    @Override
    @Transactional
    public void addLikeToPost(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with this id not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(email);
        List<Like> likes = publication.getLikes();
        for (Like like : likes) {
            if (Objects.equals(like.getUser().getId(), currentUser.getId())){
                likeRepository.delete(like);
                for (Notification notification : publication.getOwner().getNotifications()) {
                    if (notification.getLike().getUser().getId().equals(currentUser.getId())){
                        notificationRepository.delete(notification);
                    }
                }
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        publication.getLikes().add(like);
        Notification notification = new Notification();
        notificationRepository.save(notification);
        notification.setLike(like);
        notification.setUserNotification(currentUser);
        notification.setSenderUserId(publication.getOwner().getId());
    }

    @Override
    @Transactional
    public void addLikeToComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment with this id not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(email);
        List<Like> likes = comment.getLikes();
        for (Like like : likes) {
            if (Objects.equals(like.getUser().getId(), currentUser.getId())){
                likeRepository.delete(like);
                for (Notification notification : comment.getUser().getNotifications()) {
                    if (notification.getLike().getUser().getId().equals(currentUser.getId())){
                        notificationRepository.delete(notification);
                    }
                }
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        comment.getLikes().add(like);
        Notification notification = new Notification();
        notificationRepository.save(notification);
        notification.setLike(like);
        notification.setUserNotification(currentUser);
        notification.setSenderUserId(comment.getUser().getId());
    }

    @Override
    @Transactional
    public void addLikeToStory(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException("Story with this id not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(email);
        List<Like> likes = story.getLikes();
        for (Like like : likes) {
            if (Objects.equals(like.getUser().getId(), currentUser.getId())){
                likeRepository.delete(like);
                for (Notification notification : story.getOwner().getNotifications()) {
                    if (notification.getLike().getUser().getId().equals(currentUser.getId())){
                        notificationRepository.delete(notification);
                    }
                }
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        story.getLikes().add(like);
        Notification notification = new Notification();
        notificationRepository.save(notification);
        notification.setLike(like);
        notification.setUserNotification(currentUser);
        notification.setSenderUserId(story.getOwner().getId());
    }

}
