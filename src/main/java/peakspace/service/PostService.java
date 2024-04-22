package peakspace.service;

import peakspace.dto.request.PostRequest;
import peakspace.dto.response.PostResponse;
import peakspace.dto.response.SimpleResponse;

public interface PostService {

    public SimpleResponse savePost(PostRequest postRequest);
    public PostResponse getById(Long postId);
    public SimpleResponse update(Long postId,PostRequest postRequest);
    public SimpleResponse delete(Long postId);
    public SimpleResponse deleteLinkFromPost(Long linkId,Long postId);

}
