package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LikeAPI {

    private final LikeService likeService;

    @PostMapping("/to-post/{postId}")
    @Operation(description = "Поставить лайк на пост")
    boolean addLikeToPost(@PathVariable Long postId) {
        return likeService.addLikeToPost(postId);
    }

    @PostMapping("/to-comment/{commentId}")
    @Operation(description = "Поставить лайк на комментарий")
    boolean addLikeToComment(@PathVariable Long commentId) {
        return likeService.addLikeToComment(commentId);
    }

    @PostMapping("/to-story/{storyId}")
    @Operation(description = "Поставить лайк на сторис")
    boolean addLikeToStory(@PathVariable Long storyId) {
        return likeService.addLikeToStory(storyId);
    }

}
