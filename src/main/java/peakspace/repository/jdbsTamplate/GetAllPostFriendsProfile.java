package peakspace.repository.jdbsTamplate;

import peakspace.dto.response.PublicationResponse;

import java.util.List;

public interface GetAllPostFriendsProfile {
    List<PublicationResponse> findAllPublic(Long friendId);
}
