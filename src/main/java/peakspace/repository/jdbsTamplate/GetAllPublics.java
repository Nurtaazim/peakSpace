package peakspace.repository.jdbsTamplate;

import peakspace.dto.response.GetAllPublicProfileResponse;

import java.util.List;

public interface GetAllPublics {

    List<GetAllPublicProfileResponse> getAllPublics(Long userId);
}
