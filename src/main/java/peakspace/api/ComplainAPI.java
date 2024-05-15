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
import peakspace.enums.Complains;
import peakspace.service.PublicationService;

@RestController
@RequestMapping("/complains")
@RequiredArgsConstructor
public class ComplainAPI {

    private final PublicationService publicationService;

    @Secured("USER")
    @Operation(summary = "Метод для сохранения жалобы на пост.",
            description = "Этот метод получает ID поста и жалобу на него, сохраняет жалобу и возвращает ответ о результате операции (SimpleResponse).")
    @PostMapping("/{postId}")
    public SimpleResponse save(@PathVariable Long postId,
                               @RequestParam Complains complain) {
        return publicationService.saveComplainToPost(postId, complain.getRussianName());
    }

}