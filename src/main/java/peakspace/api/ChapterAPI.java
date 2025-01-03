package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.*;
import peakspace.service.ChapterService;
import peakspace.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chapters")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChapterAPI {

    private final ChapterService chapterService;
    private final UserService userService;

    @Secured("USER")
    @GetMapping("/user-chapters/{userId}")
    @Operation(summary = "Для получения всех разделов пользователя !")
    public List<ChapTerResponse> getAllChapter(@PathVariable Long userId) {
        return chapterService.getAllChaptersByUserId(userId);
    }

    @Secured("USER")
    @GetMapping("/search")
    @Operation(summary = "Поиск разделов !")
    public List<ChapTerResponse> findSearch(@RequestParam String search) {
        return userService.searchChapter(search);
    }

    @Secured("USER")
    @PostMapping("/send/{foundUserId}")
    @Operation(summary = "Send friends", description = "Отправляйте друзей в определенную chapter")
    public SimpleResponse send(@PathVariable Long foundUserId,
                               @RequestParam Long nameChapterId) {
        return userService.sendFriends(foundUserId, nameChapterId);
    }

    @Secured("USER")
    @PostMapping
    @Operation(summary = " Создать раздел !")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest) {
        return userService.createChapter(chapterRequest);
    }

    @Secured("USER")
    @GetMapping("/search-friends/{chapterId}")
    @Operation(summary = "Получить друзья пользователя и поиск по имени пользователя и ФИО! ")
    public List<FriendsResponse> getAllFriends(@PathVariable Long chapterId,
                                             @RequestParam(required = false) String search) {
        return userService.searchAllFriendsByChapter(chapterId, search);
    }

    @Secured("USER")
    @GetMapping("/all-my-friends/{userId}")
    @Operation(summary = "Получить всех друзья пользователя! developer: Mirlan")
    public List<AllFriendsResponse> getAllFriends(@RequestParam(required = false) String userName,
                                                  @PathVariable Long userId){
        return userService.getAllFriendsById(userId, userName);
    }

}