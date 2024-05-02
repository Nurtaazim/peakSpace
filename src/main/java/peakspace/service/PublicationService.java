package peakspace.service;

import peakspace.dto.response.*;

import java.security.Principal;
import java.util.List;

public interface PublicationService {
    GetAllPostsResponse getAllPosts(Principal principal);
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);

    MyPostResponse getById(Long postId);
    List<PublicationResponse> findAllPublic(Long friendId);

    List<HomePageResponse> homePage();

    PostLinkResponse findInnerPost(Long postId);

}
