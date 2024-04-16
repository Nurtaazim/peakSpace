package peakspace.api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.service.PublicationService;

import java.security.Principal;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicationAPI {

    private final PublicationService publicationService;

    @Secured("USER")
    @GetMapping("/getAllMyPosts")
    public GetAllPostsResponse getAllPosts(Principal principal){
        return publicationService.getAllPosts(principal);
    }

    @Secured({"USER", "ADMIN"})
    @GetMapping("/{postId}")
    public MyPostResponse getById(@PathVariable Long postId, Principal principal){
        return publicationService.getById(postId, principal);
    }

}
