package peakspace.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApi {
    private final PostService postService;

    @PostMapping("/post")
    @ApiOperation(value = " Добавленеи  поста !")
    public SimpleResponse savePost(@RequestBody PostRequest postRequest) {
        return postService.savePost(postRequest);
    }

    @PostMapping("/updatePost/{postId}")
    @ApiOperation(value = " Изменение поста !")
    public SimpleResponse updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.update(postId, postUpdateRequest);
    }

    @PostMapping("/deletePost/{postId}")
    @ApiOperation(value = "Удаление пост !")
    public SimpleResponse delete(@PathVariable Long postId) {
        return postService.delete(postId);
    }

    @PostMapping("/deleteLink/{linkId}/{postId}")
    @ApiOperation(value = "Удаление фото из публикации !")
    public SimpleResponse deleteLink(@PathVariable Long linkId, @PathVariable Long postId) {
        return postService.deleteLinkFromPost(linkId, postId);
    }

    @PutMapping("/addNotationFriends/{postId}/{foundUsersId}")
    @ApiOperation(value = " Отметка друзей в публикации !")
    public SimpleResponse notationFriends(@PathVariable Long postId,@PathVariable List<Long> foundUsersId) {
        return postService.notationFriend(postId,foundUsersId);
    }

    @PutMapping("/removeNotationFriends/{friendsId}")
    @ApiOperation(value = "Удалить друзей из отметки !")
    public SimpleResponse removeNotationFriends(@PathVariable List<Long> friendsId) {
        return postService.removeNotation(friendsId);
    }


}
