package peakspace.service;

import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.PublicPhotoAndVideoResponse;
import peakspace.dto.response.PublicProfileResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.enums.Choise;
import java.util.List;

public interface PublicProfileService {
    SimpleResponse save(PublicRequest publicRequest);

    SimpleResponse edit(Long publicId, PublicRequest publicRequest);

    SimpleResponse delete(Long publicId);

    PublicProfileResponse findPublicProfile();

    List<PublicPhotoAndVideoResponse> getPublicPost(Choise choise);
}
