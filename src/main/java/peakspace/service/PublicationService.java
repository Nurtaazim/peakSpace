package peakspace.service;

import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.HomePageResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.PostLinkResponse;

import java.security.Principal;
import java.util.List;

public interface PublicationService {

    GetAllPostsResponse getAllPosts(Principal principal);

    MyPostResponse getById(Long postId);

    List<HomePageResponse> homePage();

    SimpleResponse saveComplainToPost(Long postId, String complain);

    PostLinkResponse findInnerPost(Long postId);
    List<PublicationResponse> findAllPublic(Long friendId);
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);

}
