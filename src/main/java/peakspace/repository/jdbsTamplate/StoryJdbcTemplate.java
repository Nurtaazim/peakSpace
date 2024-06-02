package peakspace.repository.jdbsTamplate;

import peakspace.dto.response.StoryAllHomPageResponse;

import java.util.List;

public interface StoryJdbcTemplate {
    List<StoryAllHomPageResponse> getAllFriendsStory();
}
