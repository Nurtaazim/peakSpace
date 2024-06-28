package peakspace.service;

public interface LikeService {

    boolean addLikeToPost(Long postId);

    boolean addLikeToComment(Long commentId);

    boolean addLikeToStory(Long storyId);

}
