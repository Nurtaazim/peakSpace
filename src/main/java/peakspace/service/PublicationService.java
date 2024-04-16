package peakspace.service;

import peakspace.dto.response.GetAllPostsResponse;

import java.security.Principal;

public interface PublicationService {
    GetAllPostsResponse getAllPosts(Principal principal);

    GetAllPostsResponse getById(Long postId, Principal principal);
}
