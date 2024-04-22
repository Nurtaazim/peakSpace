package peakspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String message;
    private ZonedDateTime createdAt;
}
