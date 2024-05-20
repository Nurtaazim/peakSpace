package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.SubscriptionResponse;
import peakspace.dto.response.SearchUserResponse;
import peakspace.enums.Choise;
import peakspace.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserAPI {

    private final UserService userService;

    @Secured("USER")
    @GetMapping("/search")
    @Operation(summary = "Поискавик по выборкам группы и пользователь !")
    public List<SearchResponse> search(@RequestParam Choise sample,
                                       @RequestParam String keyWord) {
        return userService.searchFriends(sample, keyWord);
    }

    @Secured("USER")
    @GetMapping("/search-hashtags")
    @Operation(summary = " Поискавик по хештегам !")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam Choise sample,
                                                       @RequestParam String keyWord) throws MessagingException {
        return userService.searchHashtags(sample, keyWord);
    }

    @Secured("USER")
    @PutMapping("/{foundUserId}")
    @Operation(summary = "Отписатся  пользователя из раздела !")
    public SimpleResponse unsubscribeUser(@PathVariable Long foundUserId) {
        return userService.unsubscribeUser(foundUserId);
    }

    @Secured("USER")
    @GetMapping("/search-history")
    @Operation(summary = "Пользователи, которые введены в поисковике, сохранены! ")
    public List<SubscriptionResponse> getAllSearchUserHistory() {
        return userService.getAllSearchUserHistory();
    }

    @Secured("USER")
    @GetMapping("/search-with-all")
    @Operation(summary = "Поиск пользователей со всеми! ")
    public List<SearchUserResponse> searchAll(@RequestParam String keyWord) {
        return userService.globalSearch(keyWord);
    }

}