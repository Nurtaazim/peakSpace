package peakspace.service;

public interface LikeService {
    void addLikeToPost(Long postId);

    void addLikeToComment(Long commentId);

    void addLikeToStory(Long storyId);


}
