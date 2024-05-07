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
@RequestMapping("/story")
public class StoryAPI {
    private final StoryService service;
    @PostMapping("/create")
    @Operation(summary = "Создать сторис")
    SimpleResponse create(@RequestBody StoryRequest storyRequest){
        return service.create(storyRequest);
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить сторис", description = "id сториса которого хотите удалить")
    SimpleResponse delete(@PathVariable long id){
        return service.delete(id);
    }
    @GetMapping("/stories")
    @Operation(summary = "Получить сторисы пользователя", description = "id пользователя которого хотите смотреть сторис")
    List<StoryResponse>  getAll(@RequestParam long userId){
        return service.getAll(userId);
    }
}
