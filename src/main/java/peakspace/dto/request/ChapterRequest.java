package peakspace.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRequest {

    @NotBlank
    private String groupName;
}
