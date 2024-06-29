package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;

import java.util.List;

public interface PublicationJdbcRepository {
    List<PublicationResponse> findAllPublic(Long friendId);
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);
}
