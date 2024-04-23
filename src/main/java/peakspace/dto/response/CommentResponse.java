package peakspace.dto.response;

import java.time.ZonedDateTime;

public record CommentResponse(
        Long id,
        String message,
        ZonedDateTime createdAt) {
}
