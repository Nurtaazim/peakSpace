package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MessageResponse;
import peakspace.service.StoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryAPI {
    private final StoryService service;
    @PostMapping("/create")
    MessageResponse create(@RequestBody StoryRequest storyRequest,
                           @RequestParam List<Long> id){
        return service.create(storyRequest, id);
    }
}
