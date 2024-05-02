package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeAPI {

    private final LikeService likeService;

    @PostMapping("/addLikeToPost/{postId}")
    @Operation(description = "Поставить лайк на пост")
    boolean addLikeToPost(@PathVariable Long postId){
       return likeService.addLikeToPost(postId);
    }

    @PostMapping("/addLikeToComment/{commentId}")
    @Operation(description = "Поставить лайк на комментарий")
    boolean  addLikeToComment(@PathVariable Long commentId){
       return likeService.addLikeToComment(commentId);
    }

    @PostMapping("/addLikeToStory/{storyId}")
    @Operation(description = "Поставить лайк на сторис")
    boolean addLikeToStory(@PathVariable Long storyId){
       return likeService.addLikeToStory(storyId);
    }

}
