package peakspace.service;

import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.MyPostResponse;

import java.security.Principal;

public interface PublicationService {
    GetAllPostsResponse getAllPosts(Principal principal);

    MyPostResponse getById(Long postId);
}
