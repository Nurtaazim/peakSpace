package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.response.*;
import peakspace.enums.Choise;
import peakspace.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAuthority('USER')")
public class UserAPI {

    private final UserService userService;

    @GetMapping("/search")
    @Operation(summary = "Поискавик по выборкам группы и пользователь !")
    public List<SearchResponse> search(@RequestParam Choise sample,
                                       @RequestParam String keyWord) {
        return userService.searchFriends(sample, keyWord);
    }

    @GetMapping("/search-hashtags")
    @Operation(summary = " Поискавик по хештегам !")
    public List<SearchHashtagsResponse> searchHashTags(@RequestParam Choise sample,
                                                       @RequestParam String keyWord) throws MessagingException {
        return userService.searchHashtags(sample, keyWord);
    }

    @PutMapping("/{foundUserId}")
    @Operation(summary = "Отписатся  пользователя из раздела !")
    public SimpleResponse unsubscribeUser(@PathVariable Long foundUserId) {
        return userService.unsubscribeUser(foundUserId);
    }

    @GetMapping("/search-history")
    @Operation(summary = "Пользователи, которые введены в поисковике, сохранены! ")
    public List<SubscriptionResponse> getAllSearchUserHistory() {
        return userService.getAllSearchUserHistory();
    }

    @GetMapping("/search-with-all")
    @Operation(summary = "Поиск пользователей со всеми! ")
    public List<SearchUserResponse> searchAll(@RequestParam String keyWord) {
        return userService.globalSearch(keyWord);
    }

    @PostMapping("/{foundUserId}")
    @Operation(summary = "Сохранить пользователь в истории поиска! ")
    public SimpleResponse searchAll(@PathVariable Long foundUserId) {
        return userService.saveUserToHistorySearch(foundUserId);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей, кроме заблокированного!")
    public List<UserResponse> findAllUsers(Principal principal){
        return userService.findAllUsers(principal);
    }
}