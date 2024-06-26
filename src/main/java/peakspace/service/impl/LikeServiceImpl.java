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
    public boolean addLikeToPost(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException("Пост с такой id не существует!"));
        if (removeLikeFromPublication(publication.getLikes(), postId)){
            Like like = createLike();
            publication.getLikes().add(like);
            Notification notification = createNotification(like, "Ваш пост понравился пользователю " + currentUser().getThisUserName(), publication.getOwner());
            notification.setPublication(publication);
            return true;
        }
        else return false;
    }

    @Override
    @Transactional
    public boolean addLikeToComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий с такой id не существует!"));
        if (removeLikeFromComment(comment.getLikes(), commentId)){
            Like like = createLike();
            comment.getLikes().add(like);
            Notification notification = createNotification(like, "Ваш комментарий понравился пользователю " + currentUser().getThisUserName(), comment.getUser());
            notification.setComment(comment);
            return true;
        }
        else return false;
    }

    @Override
    @Transactional
    public boolean addLikeToStory(Long storyId) {
        Story story  = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException("Сторис с такой id не существует!"));
        if (removeLikeFromStory(story.getLikes(), storyId)){
            Like like = createLike();
            story.getLikes().add(like);
            Notification notification = createNotification(like, "Ваш сторис понравился пользователю " + currentUser().getThisUserName(), story.getOwner());
            notification.setStory(story);
            return true;
        }
        else return false;
    }

    private User currentUser (){
        return userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    protected boolean removeLikeFromComment(List<Like> likes, long commentId){
        for (Like like : likes) {
            if (currentUser().getId().equals(like.getUser().getId()) ){
                likes.remove(like);
                deleteNotificationByLikeIdFromComment(like.getId(),commentId);
                return false;
            }
        }
        return true;
    }@Transactional
    protected boolean removeLikeFromPublication(List<Like> likes, long publicationId){
        for (Like like : likes) {
            if (currentUser().getId().equals(like.getUser().getId()) ){
                likes.remove(like);
                deleteNotificationByLikeIdFromPublication(like.getId(), publicationId);
                return false;
            }
        }
        return true;
    }@Transactional
    protected boolean removeLikeFromStory(List<Like> likes, long storyId){
        for (Like like : likes) {
            if (currentUser().getId().equals(like.getUser().getId()) ){
                likes.remove(like);
                deleteNotificationByLikeIdFromStory(like.getId(), storyId);
                return false;
            }
        }
        return true;
    }

    @Transactional
    protected Like createLike(){
        Like likeByUserId = likeRepository.getLikeByUserId(currentUser().getId());
        if(likeByUserId == null) {
            likeByUserId = new Like();
            likeRepository.save(likeByUserId);
            likeByUserId.setUser(currentUser());
        }
        return likeByUserId;
    }

    private void deleteNotificationByLikeIdFromPublication(long likeId, long postId){
        List<Notification> notification = notificationRepository.findByLikeId(likeId);
        for (Notification notification1 : notification) {
            if (notification1.getPublication() != null && notification1.getPublication().getId().equals(postId)){
                notificationRepository.deleteById(notification1.getId());
            }

        }

    }private void deleteNotificationByLikeIdFromComment(long likeId, long commentId){
        List<Notification> notification = notificationRepository.findByLikeId(likeId);
        for (Notification notification1 : notification) {
            if (notification1.getComment() != null && notification1.getComment().getId().equals(commentId)){
                notificationRepository.deleteById(notification1.getId());
            }
        }

    }private void deleteNotificationByLikeIdFromStory(long likeId, long storyId){
        List<Notification> notification = notificationRepository.findByLikeId(likeId);
        for (Notification notification1 : notification) {
            if (notification1.getStory() != null && notification1.getStory().getId().equals(storyId)){
                notificationRepository.deleteById(notification1.getId());
            }
        }

    }

    private Notification createNotification(Like like, String forNotificationMassage, User userNotification){
        Notification n = new Notification();
        n.setLike(like);
        n.setSenderUserId(currentUser().getId());
        n.setNotificationMessage(forNotificationMassage);
        n.setUserNotification(userNotification);
        return notificationRepository.save(n);
    }

}
