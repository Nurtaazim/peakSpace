package peakspace.api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
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
    public List<SearchResponse> search(@RequestParam String sample, @RequestParam  String keyWord) {
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
    @GetMapping("/findAllChapter")
    public List<SearchResponse> findAllChapter(@RequestParam  String section) {
        return userService.searchMyFriends(section);
    }
}
