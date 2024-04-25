package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.entities.Like;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import peakspace.repository.LikeRepository;
import peakspace.repository.PublicationRepository;
import peakspace.service.LikeService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final PublicationRepository publicationRepository;
    private final UserServiceImpl userService ;
    private final LikeRepository likeRepository;
    @Override
    @Transactional
    public void addLikeToPost(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with this id not found!"));
        User currentUser = userService.getCurrentUser();
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
}
