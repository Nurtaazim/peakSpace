package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.FavoritePostResponse;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApi {
    private final PostService postService;

    @Secured("USER")
    @PostMapping("/post")
    @Operation(summary = " Добавленеи  поста !")
    public SimpleResponse savePost(@RequestBody PostRequest postRequest) {
        return postService.savePost(postRequest);
    }

    @Secured("USER")
    @PostMapping("/updatePost/{postId}")
    @Operation(summary = " Изменение поста !")
    public SimpleResponse updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.update(postId, postUpdateRequest);
    }

    @Secured("USER")
    @PostMapping("/deletePost/{postId}")
    @Operation(summary = " Удаление пост !")
    public SimpleResponse delete(@PathVariable Long postId) {
        return postService.delete(postId);
    }

    @Secured("USER")
    @PostMapping("/deleteLink/{linkId}/{postId}")
    @Operation(summary = " Удаление фото из публикации !")
    public SimpleResponse deleteLink(@PathVariable Long linkId, @PathVariable Long postId) {
        return postService.deleteLinkFromPost(linkId, postId);
    }

    @Secured("USER")
    @Operation(summary = " Добавление поста на избранный !")
    @PostMapping("/addToFavorite/{postId}")
    public SimpleResponse addFavorite(@PathVariable Long postId) {
        return postService.addFavorite(postId);
    }

    @Secured("USER")
    @Operation(summary = " Все избранные текущего пользователя !")
    @GetMapping("/getAllFavoritePost")
    public FavoritePostResponse favorite(){
        return postService.favorites();
    }

    @Secured("USER")
    @PutMapping("/addNotationFriends/{postId}/{foundUsersId}")
    @Operation(summary = " Отметка друзей в публикации !")
    public SimpleResponse notationFriends(@PathVariable Long postId,@PathVariable List<Long> foundUsersId) {
        return postService.notationFriend(postId,foundUsersId);
    }

    @Secured("USER")
    @PutMapping("/removeNotationFriends/{friendsId}")
    @Operation(summary = " Удалить друзей из отметки !")
    public SimpleResponse removeNotationFriends(@PathVariable List<Long> friendsId) {
        return postService.removeNotation(friendsId);
    }

}
