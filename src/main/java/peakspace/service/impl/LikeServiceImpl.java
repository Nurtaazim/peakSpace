package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.entities.Publication;
import peakspace.entities.Like;
import peakspace.entities.User;
import peakspace.entities.Comment;
import peakspace.entities.Story;
import peakspace.exception.NotFoundException;
import peakspace.repository.PublicationRepository;
import peakspace.repository.LikeRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.CommentRepository;
import peakspace.repository.StoryRepository;
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
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        publication.getLikes().add(like);
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
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        comment.getLikes().add(like);
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
                return;
            }
        }
        Like like = new Like();
        like.setUser(currentUser);
        likeRepository.save(like);
        story.getLikes().add(like);
    }

}
