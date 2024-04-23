package peakspace.service;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.PostResponse;
import peakspace.dto.response.SimpleResponse;

public interface PostService {

    public SimpleResponse savePost(PostRequest postRequest);
     public SimpleResponse update(Long postId, PostUpdateRequest postUpdateRequest);
    public SimpleResponse delete(Long postId);
    public SimpleResponse deleteLinkFromPost(Long linkId,Long postId);


}
