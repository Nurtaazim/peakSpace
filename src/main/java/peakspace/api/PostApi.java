package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.FavoritePostResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;
import java.util.List;

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
    @PostMapping("/deleteLink/{postId}/{linkId}")
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

    @Secured("USER")
    @PostMapping("/savePostPublic/{publicId}/{userId}")
    @Operation(summary = " Для добавление пост на Паблике !")
    public SimpleResponse savePostPublic(@PathVariable Long publicId,@PathVariable Long userId,@RequestBody PostRequest postRequest){
        return postService.savePostPublic(publicId,userId,postRequest);
    }

    @Secured("USER")
    @PostMapping("/editPostPublic/{postId}")
    @Operation(summary = " Изменение поста на паблике !")
    public SimpleResponse editPostPublic(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.editPostPublic(postId, postUpdateRequest);
    }


    @Secured("USER")
    @DeleteMapping("/deletePostPublic/{postId}")
    @Operation(summary = " Удаление пост на паблике !")
    public SimpleResponse deletePostPublic(@PathVariable Long postId) {
        return postService.deletePostPublic(postId);
    }

    @Secured("USER")
    @DeleteMapping("/deleteLinkPublic/{postId}/{linkId}")
    @Operation(summary = " Удаление фото из публикации в паблике !")
    public SimpleResponse deleteLinkPublic(@PathVariable Long linkId, @PathVariable Long postId) {
        return postService.deleteLinkFromPostPublic(linkId, postId);
    }

}
