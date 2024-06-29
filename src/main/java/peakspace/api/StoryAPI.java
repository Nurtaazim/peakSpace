package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.service.StoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StoryAPI {

    private final StoryService service;

    @Secured("USER")
    @PostMapping
    @Operation(summary = "Создать сторис")
    public SimpleResponse create(@RequestBody StoryRequest storyRequest) {
        return service.create(storyRequest);
    }

    @Secured("USER")
    @DeleteMapping("/{storyId}")
    @Operation(summary = "Удалить сторис", description = "id сториса которого хотите удалить")
    public SimpleResponse delete(@PathVariable Long storyId) {
        return service.delete(storyId);
    }

    @Secured("USER")
    @GetMapping
    @Operation(summary = "Получить сторисы пользователя", description = "id пользователя которого хотите смотреть сторис")
    public List<StoryResponse> getAll(@RequestParam Long userId) {
        return service.getAll(userId);
    }

    @Secured("USER")
    @GetMapping("/all")
    @Operation(summary = "Это все истории, которые опубликовали мои друзья на главной странице!")
    public List<StoryAllHomPageResponse> getAll() {
      return  service.getAllFriendsStory();
    }

    @Secured("USER")
    @GetMapping("/my-stories")
    @Operation(summary = "все мои истории!")
    public List<MyStoriesResponse> allMyStories() {
        return  service.getMyStories();
    }

}
