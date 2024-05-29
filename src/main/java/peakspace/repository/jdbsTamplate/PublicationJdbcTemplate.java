package peakspace.repository.jdbsTamplate;

import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;

import java.util.List;

public interface PublicationJdbcTemplate {
    List<PublicationResponse> findAllPublic(Long friendId);
    List<PublicationWithYouResponse> withPhoto(Long foundUserId);
}
