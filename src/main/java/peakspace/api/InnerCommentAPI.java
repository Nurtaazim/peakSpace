package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.InnerCommentResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/innerComment")
@RequiredArgsConstructor
public class InnerCommentAPI {

    private final CommentService commentService;

    @Secured("USER")
    @Operation(summary = " Добавление комментарии в главном комментарии !")
    @PostMapping("/save/{commentId}")
    public SimpleResponse saveInnerComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.saveInnerComment(commentId,commentRequest);
    }

    @Secured("USER")
    @Operation(summary = " Изменение комментарии в inner комментарии !")
    @PutMapping("/edit/{innerCommentId}")
    public SimpleResponse editInnerComment(@PathVariable Long innerCommentId, @RequestBody CommentRequest commentRequest) {
        return commentService.editInnerComment(innerCommentId,commentRequest);
    }

    @Secured("USER")
    @Operation(summary = " Удаление комментарии в inner комментарии !")
    @DeleteMapping("/deleteInnerComment/{innerCommentId}")
    public SimpleResponse deleteInnerComment(@PathVariable Long innerCommentId) {
        return commentService.removeInnerComment(innerCommentId);
    }

    @Secured("USER")
    @Operation(summary = " Найти inner комментарии по id !")
    @GetMapping("/findInnerComment/{innerCommentId}")
    public InnerCommentResponse findComment(@PathVariable Long innerCommentId){
        return commentService.findInnerComment(innerCommentId);
    }

    @Secured({"USER"})
    @Operation(summary = " Все inner комментарии одного комментарии !")
    @GetMapping("/getAllComment/{commentId}")
    public List<InnerCommentResponse> getAllInnerComment(@PathVariable Long commentId) {
        return commentService.getAllInnerComment(commentId);
    }




}
