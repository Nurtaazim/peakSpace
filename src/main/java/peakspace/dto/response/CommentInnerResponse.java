package peakspace.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class CommentInnerResponse {
    private Long commentId;
    private Long userId;
    private String avatar;
    private String userName;
    private String comment;
    private long countLike;
    private ZonedDateTime createdAt;
    private List<InnerCommentResponse> innerCommentResponseList;

    public CommentInnerResponse(Long commentId, Long userId, String avatar, String userName, String comment, long countLike, ZonedDateTime createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.avatar = avatar;
        this.userName = userName;
        this.comment = comment;
        this.countLike = countLike;
        this.createdAt = createdAt;
    }
}
