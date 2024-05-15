package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LinkPublicationAPI {

    private final PostService postService;

    @Secured("USER")
    @DeleteMapping("/{postId}/{linkId}")
    @Operation(summary = " Удаление фото из публикации !")
    public SimpleResponse deleteLink(@PathVariable Long linkId,
                                     @PathVariable Long postId) {
        return postService.deleteLinkFromPost(linkId, postId);
    }

}
