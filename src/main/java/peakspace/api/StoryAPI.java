package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.service.StoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryAPI {

    private final StoryService service;

    @PostMapping
    @Operation(summary = "Создать сторис")
    SimpleResponse create(@RequestBody StoryRequest storyRequest) {
        return service.create(storyRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сторис", description = "id сториса которого хотите удалить")
    SimpleResponse delete(@PathVariable long id) {
        return service.delete(id);
    }

    @GetMapping
    @Operation(summary = "Получить сторисы пользователя", description = "id пользователя которого хотите смотреть сторис")
    List<StoryResponse> getAll(@RequestParam long userId) {
        return service.getAll(userId);
    }

}
