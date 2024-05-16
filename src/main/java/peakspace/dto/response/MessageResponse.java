package peakspace.dto.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record MessageResponse(long id,
                              String content,
                              Date timestamp,
                              boolean readOrNotRead
) {
}
