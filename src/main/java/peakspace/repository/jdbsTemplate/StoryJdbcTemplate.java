package peakspace.repository.jdbsTemplate;

import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;

import java.util.List;

public interface StoryJdbcTemplate {

    List<StoryAllHomPageResponse> getAllFriendsStory();

    List<MyStoriesResponse> getMyStories();

    List<StoryResponse> getAllStoriesByUserId(Long userId);
}
