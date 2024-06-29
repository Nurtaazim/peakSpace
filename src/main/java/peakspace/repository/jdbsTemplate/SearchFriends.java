package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.FriendsResponse;

import java.util.List;

public interface SearchFriends {

    List<FriendsResponse> getAllFriendsWithJDBCTemplate(Long chapterId, String search);

}
