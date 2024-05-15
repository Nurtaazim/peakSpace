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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.FavoritePostResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostAPI {

    private final PostService postService;

    @Secured("USER")
    @PostMapping
    @Operation(summary = " Добавление  поста !")
    public SimpleResponse savePost(@RequestBody PostRequest postRequest) {
        return postService.savePost(postRequest);
    }

    @Secured("USER")
    @PatchMapping("/{postId}")
    @Operation(summary = " Изменение поста !")
    public SimpleResponse updatePost(@PathVariable Long postId,
                                     @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.update(postId, postUpdateRequest);
    }

    @Secured("USER")
    @DeleteMapping("/{postId}")
    @Operation(summary = " Удаление пост !")
    public SimpleResponse delete(@PathVariable Long postId) {
        return postService.delete(postId);
    }

    @Secured("USER")
    @Operation(summary = " Добавление поста на избранный !")
    @PostMapping("/to-favorite/{postId}")
    public SimpleResponse addFavorite(@PathVariable Long postId) {
        return postService.addFavorite(postId);
    }

    @Secured("USER")
    @Operation(summary = " Все избранные текущего пользователя !")
    @GetMapping("/favorites")
    public FavoritePostResponse favorite() {
        return postService.favorites();
    }

    @Secured("USER")
    @PutMapping("/add-notation-friends/{postId}/{foundUsersId}")
    @Operation(summary = " Отметка друзей в публикации !")
    public SimpleResponse notationFriends(@PathVariable Long postId,
                                          @PathVariable List<Long> foundUsersId) {
        return postService.notationFriend(postId, foundUsersId);
    }

    @Secured("USER")
    @DeleteMapping("/{friendsId}")
    @Operation(summary = " Удалить друзей из отметки !")
    public SimpleResponse removeNotationFriends(@PathVariable List<Long> friendsId) {
        return postService.removeNotation(friendsId);
    }

}
