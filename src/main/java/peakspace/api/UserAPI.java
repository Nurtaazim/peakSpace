package peakspace.api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Send friends", notes = "Send friends to a specific chapter")
    public SimpleResponse send(@PathVariable Long foundUserId,@RequestParam String nameChapter) {
        return userService.sendFriends(foundUserId,nameChapter);
    }

    @GetMapping("/searchChapter")
    @ApiOperation(value = "Поиск по разделам !")
    public List<ChapTerResponse> findSearch(@RequestParam String search){
        return userService.searchChapter(search);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Поискавик", notes = "Поиск по выборкам !")
    public List<SearchResponse> search(@RequestParam Choise sample, @RequestParam  String keyWord) {
        return userService.searchFriends(sample,keyWord);
    }

    @PostMapping("/createChapter")
    @ApiOperation(value = "Создать раздел ")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest){
        return userService.createChapter(chapterRequest);
    }

    @GetMapping("/searchHashtags")
    @ApiOperation(value = "Поискавик", notes = "Поиск по хештегам  !")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam Choise sample,@RequestParam String keyWord) {
        return userService.searchHashtags(sample,keyWord);
    }
    @GetMapping("/findAllChapter/{chapterId}")
    @ApiOperation(value = "Все разделы !")
    public List<SearchResponse> findAllChapter(@RequestParam(required = false)  String userName,@PathVariable Long chapterId) {
        return userService.searchMyFriends(chapterId,userName);
    }

    @GetMapping("/profileFriends/{foundUserId}")
    @ApiOperation(value = " Профиль ", notes = "Профиль страница друга  !")
    public ProfileFriendsResponse findByIdFriends(@PathVariable Long foundUserId){
        return userService.findFriendsProfile(foundUserId);
    }

    @GetMapping("/deleteUser/{chapterId}/{foundUserId}")
    @ApiOperation(value = "Отписатся  пользователя из раздела !")
    public SimpleResponse deleteUser(@PathVariable Long chapterId,@PathVariable Long foundUserId) {
        return userService.unsubscribeUser(chapterId,foundUserId);
    }
}




