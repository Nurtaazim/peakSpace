package peakspace.service;

import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;

import java.security.Principal;
import java.util.List;

public interface PublicationService {
    GetAllPostsResponse getAllPosts(Principal principal);
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);

    MyPostResponse getById(Long postId);
    List<PublicationResponse> findAllPublic(Long friendId);
}
