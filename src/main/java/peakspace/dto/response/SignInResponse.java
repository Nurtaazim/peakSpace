package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignInResponse {
    private long id;
    private String token;

}
