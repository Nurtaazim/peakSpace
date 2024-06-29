package peakspace.service;

import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;

import java.util.List;

public interface StoryService  {

    SimpleResponse create(StoryRequest storyRequest);

    SimpleResponse delete(Long id);

    List<StoryResponse> getAll(Long userId);

    List<StoryAllHomPageResponse> getAllFriendsStory();

    List<MyStoriesResponse> getMyStories();

}
