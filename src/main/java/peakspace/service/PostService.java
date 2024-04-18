package peakspace.service;

import peakspace.dto.request.PostRequest;
import peakspace.dto.response.SimpleResponse;

public interface PostService {

    public SimpleResponse savePost(PostRequest postRequest);
}
