package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.AllFriendsResponse;

import java.util.List;

public interface GetAllFriends {

    List<AllFriendsResponse> getAllFriendsById(Long userId, String userName);

}
