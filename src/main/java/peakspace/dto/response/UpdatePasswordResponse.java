package peakspace.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePasswordResponse {

    private Long id;
    private String email;
    private String token;
}
