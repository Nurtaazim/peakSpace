package peakspace.service;

import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;

import java.util.List;

public interface PublicationService {
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);

    List<PublicationResponse> findAllPublic(Long friendId);
}
