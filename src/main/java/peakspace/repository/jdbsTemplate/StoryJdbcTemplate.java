package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.entities.User;

import java.util.List;

public interface StoryJdbcTemplate {

    List<StoryAllHomPageResponse> getAllFriendsStory(Long currentUserId);

    List<MyStoriesResponse> getMyStories(User user);

    List<StoryResponse> getAllStoriesByUserId(Long userId);
}
