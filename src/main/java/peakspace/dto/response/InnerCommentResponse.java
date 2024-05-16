package peakspace.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record InnerCommentResponse(Long innerCommentId,
                                   Long userId,
                                   String avatar,
                                   String userName,
                                   String comment,
                                   long countLike,
                                   ZonedDateTime createdAt) {
}