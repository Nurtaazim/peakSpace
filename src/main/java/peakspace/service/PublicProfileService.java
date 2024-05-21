package peakspace.service;

import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.*;
import peakspace.enums.Choise;
import java.util.List;

public interface PublicProfileService {
    SimpleResponse save(PublicRequest publicRequest);

    SimpleResponse edit(PublicRequest publicRequest);

    SimpleResponse delete(Long publicId);

    PublicProfileResponse findPublicProfile(Long publicId,Long userId);

    List<PublicPhotoAndVideoResponse> getPublicPost(Choise choise,Long publicId,Long userId);

    PublicPostResponse findPostPublic(Long postId);

    SimpleResponse removeUser(Long friendId);

    SimpleResponse sendPublic(Long publicId);

    SimpleResponse removePost(Long postId);

    SimpleResponse removeComment(Long commentId);

    List<PublicProfile> getAllPublics(Long userId);

}
