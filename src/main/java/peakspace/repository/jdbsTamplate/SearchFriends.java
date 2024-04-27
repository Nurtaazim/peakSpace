package peakspace.repository.jdbsTamplate;

import peakspace.dto.response.FriendsResponse;

import java.util.List;

public interface SearchFriends {
    List<FriendsResponse> getAllFriendsWithJDBCTemplate(Long userId, Long chapterId, String search);
}
