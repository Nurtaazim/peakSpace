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
        if (removeLike(publication.getLikes())){
            Like like = createLike();
            publication.getLikes().add(like);
            createNotification(like, "Ваш пост понравился пользователю " + currentUser().getThisUserName(), publication.getOwner());

            return true;
        }
        else return false;
    }

    @Override
    @Transactional
    public boolean addLikeToComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий с такой id не существует!"));
        if (removeLike(comment.getLikes())){
            Like like = createLike();
            comment.getLikes().add(like);
            createNotification(like, "Ваш комментарий понравился пользователю " + currentUser().getThisUserName(), comment.getUser());

            return true;
        }
        else return false;
    }

    @Override
    @Transactional
    public boolean addLikeToStory(Long storyId) {
        Story story  = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException("Сторис с такой id не существует!"));
        if (removeLike(story.getLikes())){
            Like like = createLike();
            story.getLikes().add(like);
            createNotification(like, "Ваш сторис понравился пользователю " + currentUser().getThisUserName(), story.getOwner());
            return true;
        }
        else return false;
    }

    private User currentUser (){
        return userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    protected boolean removeLike(List<Like> likes){
        for (Like like : likes) {
            if (currentUser().getId().equals(like.getUser().getId()) ){
                likes.remove(like);
                deleteNotificationByLikeId(like.getId());
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

    private void deleteNotificationByLikeId(long likeId){
        Notification notification = notificationRepository.findByLikeId(likeId).orElseThrow(() -> new NotFoundException("Такое уведомление не существует!"));
        notificationRepository.deleteById(notification.getId());
    }

    private void createNotification(Like like, String forNotificationMassage, User userNotification){
        Notification n = new Notification();
        n.setLike(like);
        n.setSenderUserId(currentUser().getId());
        n.setNotificationMessage(forNotificationMassage);
        n.setUserNotification(userNotification);
        notificationRepository.save(n);
    }




}
