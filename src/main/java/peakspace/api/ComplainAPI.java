package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Publication;
import peakspace.service.PublicationService;

@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
public class ComplainAPI {

    private final PublicationService publicationService;

    @Secured("USER")
    @Operation(summary = "Для оставления жалобы на пост")
    @PostMapping("/save/{postId}")
    public SimpleResponse save(@PathVariable Long postId, @RequestParam String complain) {
        return publicationService.saveComplainToPost(postId, complain);
    }

}