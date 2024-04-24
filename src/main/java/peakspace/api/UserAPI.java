package peakspace.api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.*;
import peakspace.enums.Choise;
import peakspace.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;


    @GetMapping("/send/{foundUserId}")
    @Operation(summary = "Send friends", description = "Send friends to a specific chapter")
    public SimpleResponse send(@PathVariable Long foundUserId,@RequestParam Long nameChapterId) {
        return userService.sendFriends(foundUserId,nameChapterId);
    }

    @GetMapping("/searchChapter")
    @Operation(summary = "Поиск  разделов  !")
    public List<ChapTerResponse> findSearch(@RequestParam String search){
        return userService.searchChapter(search);
    }

    @GetMapping("/search")
    @Operation( summary = "Поискавик", description = "Поиск по выборкам !")
    public List<SearchResponse> search(@RequestParam Choise sample, @RequestParam  String keyWord) {
        return userService.searchFriends(sample,keyWord);
    }

    @PostMapping("/createChapter")
    @Operation(summary = "Создать раздел ")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest){
        return userService.createChapter(chapterRequest);
    }

    @GetMapping("/searchHashtags")
    @Operation(summary = "Поискавик", description = "Поиск по хештегам  !")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam Choise sample,@RequestParam String keyWord) throws MessagingException {
        return userService.searchHashtags(sample,keyWord);
    }
    @GetMapping("/findAllChapter/{chapterId}")
    @Operation(summary = "Все разделы !")
    public List<SearchResponse> findAllChapter(@RequestParam(required = false)  String userName,@PathVariable Long chapterId) {
        return userService.searchMyFriends(chapterId,userName);
    }

    @GetMapping("/deleteUser/{chapterId}/{foundUserId}")
    @Operation(summary = "Отписатся  пользователя из раздела !")
    public SimpleResponse unsubscribeUser(@PathVariable Long chapterId,@PathVariable Long foundUserId) {
        return userService.unsubscribeUser(chapterId,foundUserId);
    }

    @GetMapping("/searchHistory")
    @Operation(summary = "Пользователи, которые введены в поисковике, сохранены! ")
    public List<SubscriptionResponse> getAllSearchUserHistory(){
        return userService.getAllSearchUserHistory();
    }


}




