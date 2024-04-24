package peakspace.api;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.*;
import peakspace.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    @GetMapping("/send/{foundUserId}")
    public SimpleResponse send(@PathVariable Long foundUserId,@RequestParam String nameChapter) {
        return userService.sendFriends(foundUserId,nameChapter);
    }

    @GetMapping("/search")
    public List<SearchResponse> search(@RequestParam String sample, @RequestParam  String keyWord) throws MessagingException {
        return userService.searchFriends(sample,keyWord);
    }
    @PostMapping("/createChapter")
    public SimpleResponse createChapter(@RequestBody ChapterRequest chapterRequest){
        return userService.createChapter(chapterRequest);
    }

    @GetMapping("/searchHashtags")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam  String keyWord) {
        return userService.searchHashtags(keyWord);
    }
    @GetMapping("/findAllChapter/{chapterId}")
    public List<SearchResponse> findAllChapter(@RequestParam(required = false)  String userName,@PathVariable Long chapterId) {
        return userService.searchMyFriends(chapterId,userName);
    }

    @GetMapping("/profileFriends/{foundUserId}")
    public ProfileFriendsResponse findByIdFriends(@PathVariable Long foundUserId){
        return userService.findFriendsProfile(foundUserId);
    }

    @GetMapping("/searchWithAll")
    public List<SearchUserResponse> searchAll(@RequestParam String keyWord){
        return userService.globalSearch(keyWord);
    }
}
