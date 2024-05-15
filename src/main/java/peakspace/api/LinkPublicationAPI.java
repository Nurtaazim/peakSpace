package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController("/links")
@RequiredArgsConstructor
public class LinkPublicationAPI {

    private final PostService postService;

    @Secured("USER")
    @DeleteMapping("/{postId}/{linkId}")
    @Operation(summary = " Удаление фото из публикации !")
    public SimpleResponse deleteLink(@PathVariable Long linkId, @PathVariable Long postId) {
        return postService.deleteLinkFromPost(linkId, postId);
    }

}
