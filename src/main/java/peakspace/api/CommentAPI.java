package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.CommentResponseByPost;
import peakspace.dto.response.CommentInnerResponse;
import peakspace.service.CommentService;
import peakspace.service.PublicProfileService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentAPI {

    private final CommentService commentService;
    private final PublicProfileService publicService;

    @Secured("USER")
    @Operation(summary = "Для добавление коммментарии на пост !")
    @PostMapping("/{postId}")
    public SimpleResponse save(@PathVariable Long postId, @RequestBody CommentRequest comment) {
        return commentService.save(postId, comment);
    }

    @Secured({"USER"})
    @Operation(summary = " Все комментарии одного поста !")
    @GetMapping("/{postId}")
    public List<CommentResponseByPost> getAllComment(@PathVariable Long postId) {
        return commentService.getAllComment(postId);
    }

    @Secured("USER")
    @Operation(summary = " Найти комментарии по id !")
    @GetMapping("/find/{commentId}")
    public CommentInnerResponse findComment(@PathVariable Long commentId) {
        return commentService.findComment(commentId);
    }

    @Secured("USER")
    @Operation(summary = " Для изменение комментарии !")
    @PutMapping("/{commentId}")
    public SimpleResponse edit(@PathVariable Long commentId, @RequestBody CommentRequest comment) {
        return commentService.editComment(commentId, comment);
    }

    @Secured("USER")
    @Operation(summary = " Удалить комментарии !")
    @DeleteMapping("/{commentId}")
    public SimpleResponse delete(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @Secured("USER")
    @Operation(summary = "Для удаление комментарии от имени Admin (владелец паблика) !")
    @DeleteMapping("/remove-comment/{commentId}")
    public SimpleResponse removeCommentAdmin(@PathVariable Long commentId) {
        return publicService.removeComment(commentId);
    }

}
