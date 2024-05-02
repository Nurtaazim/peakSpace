package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.*;
import peakspace.service.PublicationService;
import peakspace.service.UserService;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicationAPI {

    private final PublicationService publicationService;
    private final UserService userService;

    @Secured("USER")
    @GetMapping("/getAllMyPosts")
    @Operation(summary = " Все мои посты !")
    public GetAllPostsResponse getAllPosts(Principal principal) {
        return publicationService.getAllPosts(principal);
        }

    @Secured({"USER","ADMIN"})
    @GetMapping("/photoWithMe/{foundUserId}")
    @Operation(summary = " Фото с вами !")
    public List<PublicationWithYouResponse> withPhoto(@PathVariable Long foundUserId) {
        return publicationService.withPhoto(foundUserId);
    }

    @Secured({"USER"})
    @GetMapping("/{postId}")
    @Operation(summary = " Найти пост по  id ")
    public MyPostResponse getById(@PathVariable Long postId) {
        return publicationService.getById(postId);
    }

    @Secured({"USER"})
    @GetMapping("/findAll/{friendId}")
    @Operation(summary = " Все публикации друга ! ")
    public List<PublicationResponse> findAllPublic(@PathVariable Long friendId) {
        return publicationService.findAllPublic(friendId);
    }

    @Secured({"USER"})
    @GetMapping("/profileFriends/{foundUserId}")
    @Operation(summary = " Профиль ", description = "Профиль страница друга  !")
    public ProfileFriendsResponse findByIdFriends(@PathVariable Long foundUserId){
        return userService.findFriendsProfile(foundUserId);
    }

    @Secured("USER")
    @GetMapping("/findAllPublicMyFriends")
    @Operation(summary = "Главный страница текущего пользователя который друзья опубликовали !")
    public List<HomePageResponse> homePageResponses(){
        return publicationService.homePage();
    }

    @Secured("USER")
    @GetMapping("/findInnerPostLink/{postId}")
    @Operation(summary = " Это универсальный метод для поста который когда нажимает фото это страница отображется ! ")
    public PostLinkResponse findPostInnerLink(@PathVariable Long postId){
    return publicationService.findInnerPost(postId);
    }

}