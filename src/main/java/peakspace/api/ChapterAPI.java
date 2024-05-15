package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.ChapTerResponse;
import peakspace.dto.response.FriendsPageResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.ChapterService;
import peakspace.service.UserService;

import java.util.List;
import java.util.Map;

@RestController("/chapters")
@RequiredArgsConstructor
public class ChapterAPI {

    private final ChapterService chapterService;
    private final UserService userService;

    @Secured("USER")
    @GetMapping("/users-chapters/{userId}")
    @Operation(summary = "Для получения всех разделов пользователя!")
    public Map<Long, String> getAllChapter(@PathVariable Long userId){
        return chapterService.getAllChaptersByUserId(userId);
    }

    @Secured("USER")
    @GetMapping("/searchChapter")
    @Operation(summary = "Поиск  разделов  !")
    public List<ChapTerResponse> findSearch(@RequestParam String search) {
        return userService.searchChapter(search);
    }

    @Secured("USER")
    @PostMapping("/send/{foundUserId}")
    @Operation(summary = "Send friends", description = "Отправляйте друзей в определенную chapter")
    public SimpleResponse send(@PathVariable Long foundUserId, @RequestParam Long nameChapterId) {
        return userService.sendFriends(foundUserId, nameChapterId);
    }

    @Secured("USER")
    @PostMapping
    @Operation(summary = " Создать раздел !")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest) {
        return userService.createChapter(chapterRequest);
    }

    @Secured("USER")
    @GetMapping("/{chapterId}")
    @Operation(summary = "Все разделы !")
    public List<SearchResponse> findAllChapter(@RequestParam(required = false) String userName, @PathVariable Long chapterId) {
        return userService.searchMyFriends(chapterId, userName);
    }

    @Secured("USER")
    @GetMapping("/search-friends/{userId}/{chapterId}")
    @Operation(summary = "Получить друзья пользователя и поиск по имени пользователя и ФИО! ")
    public FriendsPageResponse getAllFriends(@PathVariable Long userId,
                                             @PathVariable Long chapterId,
                                             @RequestParam(required = false) String search) {
        return userService.searchAllFriendsByChapter(userId, chapterId, search);
    }

}