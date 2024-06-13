package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class SimpleResponse {
    private HttpStatus httpStatus;
    private String message;
    private boolean isBlock;
}
