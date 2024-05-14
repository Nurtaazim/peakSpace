package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import peakspace.service.ChapterService;

import java.util.Map;

@RestController("/chapter")
@RequiredArgsConstructor
public class ChapterAPI {

    private final ChapterService chapterService;

    @Secured("USER")
    @GetMapping("/get_chapters/{userId}")
    @Operation(summary = "Для получения всех разделов пользователя!")
    public Map<Long, String> getAllChapter(@PathVariable Long userId){
        return chapterService.getAllChaptersByUserId(userId);
    }

}