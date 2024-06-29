package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.AllFriendsResponse;

import java.util.List;

public interface GetAllFriendsJdbcRepository {

    List<AllFriendsResponse> getAllFriendsById(Long userId, String userName);

}
