package peakspace.service;

import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.*;

import java.util.List;

public interface PublicProfileService {
    PublicProfileResponse save(PublicRequest publicRequest);

    SimpleResponse edit(PublicRequest publicRequest);

    SimpleResponse delete();

    PublicPostResponse findPostPublic(Long postId);

    SimpleResponse removeUser(Long friendId);

    SimpleResponse sendPublic(Long publicId);

    SimpleResponse removeComment(Long commentId);

    PublicProfileResponse forwardingMyPublic(String publicName);

    ProfileFriendsResponse forwardingMyProfile(String userName);

    ProfileFriendsResponse findUserByPostId(Long postId);

    List<PublicProfileResponse> getRandomCommunities();

    List<PublicProfileResponse> getMyCommunities();

    PublicProfileResponse getMyCommunity();

    PublicProfileResponse getCommunityById(Long communityId);

    SimpleResponse addPublicationToCommunityById(Long communityId, PostRequest postRequest);

    List<ShortPublicationResponse> getAllPublicationByCommunityId(Long communityId);


    SimpleResponse blockUserInCommunity(Long communityId, Long userId);

    List<SearchResponse> getUsersByCommunityId(Long communityId);
}
