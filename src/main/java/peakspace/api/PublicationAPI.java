package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.ProfileFriendsResponse;
import peakspace.dto.response.HomePageResponse;
import peakspace.dto.response.PostLinkResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.repository.jdbsTamplate.PublicationJdbcTemplate;
import peakspace.service.PublicationService;
import peakspace.service.UserService;

import java.util.List;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicationAPI {

    private final UserService userService;
    private final PublicationService publicationService;
    private final PublicationJdbcTemplate publicationJdbcTemplate;

    @Secured("USER")
    @GetMapping("/my")
    @Operation(summary = " Все мои посты !")
    public GetAllPostsResponse getAllPosts(Principal principal) {
        return publicationService.getAllPosts(principal);
    }

    @Secured({"USER"})
    @GetMapping("/photo-with-me/{foundUserId}")
    @Operation(summary = " Профиль друга Фото с вами !")
    public List<PublicationWithYouResponse> withPhoto(@PathVariable Long foundUserId) {
        return publicationJdbcTemplate.withPhoto(foundUserId);
    }

    @Secured({"USER"})
    @GetMapping("/find/{postId}")
    @Operation(summary = " Найти пост по id ")
    public MyPostResponse getById(@PathVariable Long postId) {
        return publicationService.getById(postId);
    }

    @Secured({"USER"})
    @GetMapping("/{friendId}")
    @Operation(summary = "Профиль друга все публикации друга ! ")
    public List<PublicationResponse> findAllPublic(@PathVariable Long friendId) {
        return publicationJdbcTemplate.findAllPublic(friendId);
    }

    @Secured({"USER"})
    @GetMapping("/profile-friends/{foundUserId}")
    @Operation(summary = " Профиль друга !", description = "Профиль страница друга  !")
    public ProfileFriendsResponse findByIdFriends(@PathVariable Long foundUserId) {
        return userService.findFriendsProfile(foundUserId);
    }

    @Secured("USER")
    @GetMapping("/my-friend-publications")
    @Operation(summary = " Главный страница текущего пользователя который друзья опубликовали !")
    public List<HomePageResponse> homePageResponses() {
        return publicationService.homePage();
    }

    @Secured("USER")
    @GetMapping("/find-inner-post-link/{postId}")
    @Operation(summary = " Это универсальный метод для поста который когда нажимает фото это страница отображется ! ")
    public PostLinkResponse findPostInnerLink(@PathVariable Long postId) {
        return publicationService.findInnerPost(postId);
    }

}