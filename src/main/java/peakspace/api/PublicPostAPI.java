package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-posts")
public class PublicPostAPI {

    private final PostService postService;

    @Secured("USER")
    @PostMapping("/{publicId}/{userId}")
    @Operation(summary = " Для добавление пост на Паблике !")
    public SimpleResponse savePostPublic(@PathVariable Long publicId,
                                         @PathVariable Long userId,
                                         @RequestBody PostRequest postRequest) {
        return postService.savePostPublic(publicId, userId, postRequest);
    }

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
