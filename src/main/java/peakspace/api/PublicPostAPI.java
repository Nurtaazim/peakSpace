package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicPostAPI {

    private final PostService postService;


    @Secured("USER")
    @PatchMapping("/{postId}")
    @Operation(summary = " Изменение поста на паблике !")
    public SimpleResponse editPostPublic(@PathVariable Long postId,
                                         @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.editPostPublic(postId, postUpdateRequest);
    }

    @Secured("USER")
    @DeleteMapping("/{postId}")
    @Operation(summary = " Удаление пост на паблике !")
    public SimpleResponse deletePostPublic(@PathVariable Long postId) {
        return postService.deletePostPublic(postId);
    }

}
