package peakspace.api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.service.PublicationService;

import java.security.Principal;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicationAPI {

    private final PublicationService publicationService;

    @GetMapping("/getAll")
    public GetAllPostsResponse getAllPosts(Principal principal){
        return publicationService.getAllPosts(principal);
    }

    @GetMapping("/{postId}")
    public GetAllPostsResponse getById(@PathVariable Long postId, Principal principal){
        return publicationService.getById(postId, principal);
    }
}
