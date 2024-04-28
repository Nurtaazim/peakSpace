package peakspace.service;

import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MessageResponse;

import java.util.List;

public interface StoryService  {
    MessageResponse create(StoryRequest storyRequest, List<Long> id);
}
