package peakspace.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleResponse {
    private HttpStatus httpStatus;
    private String message;
}
