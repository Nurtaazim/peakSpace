package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.service.StoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StoryAPI {

    private final StoryService service;

    @PostMapping
    @Operation(summary = "Создать сторис")
    public SimpleResponse create(@RequestBody StoryRequest storyRequest) {
        return service.create(storyRequest);
    }

    @DeleteMapping("/{storyId}")
    @Operation(summary = "Удалить сторис", description = "id сториса которого хотите удалить")
    public SimpleResponse delete(@PathVariable Long storyId) {
        return service.delete(storyId);
    }

    @GetMapping
    @Operation(summary = "Получить сторисы пользователя", description = "id пользователя которого хотите смотреть сторис")
    public List<StoryResponse> getAll(@RequestParam long userId) {
        return service.getAll(userId);
    }

}
