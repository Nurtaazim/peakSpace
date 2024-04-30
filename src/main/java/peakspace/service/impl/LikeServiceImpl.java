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
import peakspace.repository.*;
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
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException("Пост с такой id не существует!"));
        if (isLike(publication.getLikes())){
            Like like = createLike(publication.getOwner());
            like.setPublication(publication);
        }
    }

    @Override
    @Transactional
    public void addLikeToComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий с такой id не существует!"));
        if (isLike(comment.getLikes())){
            Like like = createLike(comment.getUser());
            like.setComment(comment);
        }
    }

    @Override
    @Transactional
    public void addLikeToStory(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException("Сторис с такой id не существует!"));
        if (!isLike(story.getLikes())){
            Like like = createLike(story.getOwner());
            like.setStory(story);
        }
    }
    private User currentUser (){
        return userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    @Transactional
    protected boolean isLike(List<Like> likes){
        for (Like like : likes) {
            if (currentUser().getId().equals(like.getUser().getId()) ){
                likeRepository.delete(like.getId());
                notificationRepository.deleteByid(like.getNotification().getId());
                return true;
            }

        }
        return false;
    }
    @Transactional
    protected Like createLike(User recipientOfTheLike){
        Like like = new Like();
        like.setNotification(new Notification(like, recipientOfTheLike, currentUser().getId()));
        Like save = likeRepository.save(like);
        like.setUser(currentUser());
        return save;
    }

}
