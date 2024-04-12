package peakspace.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.SampleRequest;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SubscriptionResponse;
import peakspace.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    @GetMapping("/send/{foundUserId}")
    public List<SubscriptionResponse> send(@PathVariable Long foundUserId) {
        return userService.sendFriends(foundUserId);
    }

    @GetMapping("/search")
    public List<SearchResponse> search(@RequestParam String sample, @RequestParam  String keyWord) {
        return userService.searchFriends(sample,keyWord);
    }
}
