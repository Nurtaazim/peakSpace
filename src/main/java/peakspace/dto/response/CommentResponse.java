package peakspace.dto.response;

import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long userId;
    private String avatar;
    private String userName;
    private String comment;
    private long countLike;
    private ZonedDateTime createdAt;
    private boolean isLike;

    public CommentResponse(Long id, Long userId, String avatar, String userName, String comment, long countLike, ZonedDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.avatar = avatar;
        this.userName = userName;
        this.comment = comment;
        this.countLike = countLike;
        this.createdAt = createdAt;
    }

}
