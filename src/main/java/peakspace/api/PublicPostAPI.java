package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController
@RequestMapping("/public-post")
@RequiredArgsConstructor
public class PublicPostAPI {

    private final PostService postService;

    @Secured("USER")
    @PostMapping("/savePostPublic/{publicId}/{userId}")
    @Operation(summary = " Для добавление пост на Паблике !")
    public SimpleResponse savePostPublic(@PathVariable Long publicId, @PathVariable Long userId, @RequestBody PostRequest postRequest) {
        return postService.savePostPublic(publicId, userId, postRequest);
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


}
