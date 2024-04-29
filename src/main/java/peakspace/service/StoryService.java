package peakspace.service;

import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.SimpleResponse;

import java.beans.SimpleBeanInfo;
import java.util.List;

public interface StoryService  {
    SimpleResponse create(StoryRequest storyRequest, List<Long> id);

    SimpleResponse delete(long id);
}
