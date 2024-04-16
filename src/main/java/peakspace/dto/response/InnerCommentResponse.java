package peakspace.dto.response;
import lombok.Builder;
import java.time.ZonedDateTime;

@Builder
public record InnerCommentResponse(Long id,
                                   String avatar,
                                   String userName,
                                   String comment,
                                   int countLike,
                                   ZonedDateTime createdAt) {
}