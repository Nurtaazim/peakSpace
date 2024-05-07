package peakspace.service;

import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryResponse;

import java.util.List;

public interface StoryService  {
    SimpleResponse create(StoryRequest storyRequest);

    SimpleResponse delete(long id);

    List<StoryResponse> getAll(long userId);
}
