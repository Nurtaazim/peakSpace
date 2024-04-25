package peakspace.api;

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
    void addLikeToPost(@PathVariable Long postId){
        likeService.addLikeToPost(postId);
    }
}
