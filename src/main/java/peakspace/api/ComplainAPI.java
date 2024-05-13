package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.response.SimpleResponse;
import peakspace.enums.Complains;
import peakspace.service.PublicationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
public class ComplainAPI {

    private final PublicationService publicationService;

    @Secured("USER")
    @Operation(summary = "Для оставления жалобы на пост")
    @PostMapping("/save/{postId}")
    public SimpleResponse save(@PathVariable Long postId,
                               @RequestParam Complains complain) {
        return publicationService.saveComplainToPost(postId, complain.getRussianName());
    }

}