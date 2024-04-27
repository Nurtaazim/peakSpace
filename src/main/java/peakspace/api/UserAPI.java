package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.ChapTerResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.SubscriptionResponse;
import peakspace.enums.Choise;
import peakspace.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    @Secured("USER")
    @GetMapping("/send/{foundUserId}")
    @Operation(summary = "Send friends", description = "Send friends to a specific chapter")
    public SimpleResponse send(@PathVariable Long foundUserId,@RequestParam Long nameChapterId) {
        return userService.sendFriends(foundUserId,nameChapterId);
    }

    @Secured("USER")
    @GetMapping("/searchChapter")
    @Operation(summary = "Поиск  разделов  !")
    public List<ChapTerResponse> findSearch(@RequestParam String search){
        return userService.searchChapter(search);
    }

    @Secured("USER")
    @GetMapping("/search")
    @Operation( summary = "Поискавик", description = "Поиск по выборкам !")
    public List<SearchResponse> search(@RequestParam Choise sample, @RequestParam  String keyWord) {
        return userService.searchFriends(sample,keyWord);
    }

    @Secured("USER")
    @PostMapping("/createChapter")
    @Operation(summary = "Создать раздел ")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest){
        return userService.createChapter(chapterRequest);
    }

    @Secured("USER")
    @GetMapping("/searchHashtags")
    @Operation(summary = "Поискавик", description = "Поиск по хештегам  !")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam Choise sample,@RequestParam String keyWord) throws MessagingException {
        return userService.searchHashtags(sample,keyWord);
    }

    @Secured("USER")
    @GetMapping("/findAllChapter/{chapterId}")
    @Operation(summary = "Все разделы !")
    public List<SearchResponse> findAllChapter(@RequestParam(required = false)  String userName,@PathVariable Long chapterId) {
        return userService.searchMyFriends(chapterId,userName);
    }

    @Secured("USER")
    @GetMapping("/deleteUser/{chapterId}/{foundUserId}")
    @Operation(summary = "Отписатся  пользователя из раздела !")
    public SimpleResponse unsubscribeUser(@PathVariable Long chapterId,@PathVariable Long foundUserId) {
        return userService.unsubscribeUser(chapterId,foundUserId);
    }

    @Secured("USER")
    @GetMapping("/searchHistory")
    @Operation(summary = "Пользователи, которые введены в поисковике, сохранены! ")
    public List<SubscriptionResponse> getAllSearchUserHistory(){
        return userService.getAllSearchUserHistory();
    }
}





