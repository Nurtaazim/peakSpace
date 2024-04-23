package peakspace.api;

import io.swagger.annotations.ApiOperation;
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
    public GetAllPostsResponse getAllPosts(Principal principal) {
        return publicationService.getAllPosts(principal);

        }
    @GetMapping("/photoWithMe/{foundUserId}")
    @ApiOperation(value = " Фото с вами !")
    public List<PublicationWithYouResponse> withPhoto(@PathVariable Long foundUserId) {
        return publicationService.withPhoto(foundUserId);
    }

    @Secured({"USER", "ADMIN"})
    @GetMapping("/{postId}")
    public MyPostResponse getById(@PathVariable Long postId) {
        return publicationService.getById(postId);
    }

    @GetMapping("/findAll/{friendId}")
    @ApiOperation(value = " Все публикации друга ! ")
    public List<PublicationResponse> findAllPublic(@PathVariable Long friendId) {
        return publicationService.findAllPublic(friendId);
    }

    @GetMapping("/profileFriends/{foundUserId}")
    @ApiOperation(value = " Профиль ", notes = "Профиль страница друга  !")
    public ProfileFriendsResponse findByIdFriends(@PathVariable Long foundUserId){
        return userService.findFriendsProfile(foundUserId);
    }




}
