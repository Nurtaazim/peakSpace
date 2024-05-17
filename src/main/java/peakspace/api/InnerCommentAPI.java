package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.InnerCommentResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inner-comments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InnerCommentAPI {

    private final CommentService commentService;

    @Secured("USER")
    @Operation(summary = " Добавление комментарии в главном комментарии !")
    @PostMapping("/{commentId}")
    public SimpleResponse saveInnerComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequest commentRequest) {
        return commentService.saveInnerComment(commentId, commentRequest);
    }

    @Secured("USER")
    @Operation(summary = " Изменение комментарии в inner комментарии !")
    @PutMapping("/{innerCommentId}")
    public SimpleResponse editInnerComment(@PathVariable Long innerCommentId,
                                           @RequestBody CommentRequest commentRequest) {
        return commentService.editInnerComment(innerCommentId, commentRequest);
    }

    @Secured("USER")
    @Operation(summary = "Универсальный метод . Удаление комментарии и inner комментарии !")
    @DeleteMapping("/{commentId}")
    public SimpleResponse deleteInnerComment(@PathVariable Long commentId) {
        return commentService.removeInnerComment(commentId);
    }

    @Secured("USER")
    @Operation(summary = " Найти inner комментарии по id !")
    @GetMapping("/find/{innerCommentId}")
    public InnerCommentResponse findComment(@PathVariable Long innerCommentId) {
        return commentService.findInnerComment(innerCommentId);
    }

    @Secured({"USER"})
    @Operation(summary = " Все inner комментарии одного комментарии !")
    @GetMapping("/{commentId}")
    public List<InnerCommentResponse> getAllInnerComment(@PathVariable Long commentId) {
        return commentService.getAllInnerComment(commentId);
    }

}
