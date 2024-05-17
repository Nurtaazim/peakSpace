package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignInResponse {
    private long id;
    private String token;

}
