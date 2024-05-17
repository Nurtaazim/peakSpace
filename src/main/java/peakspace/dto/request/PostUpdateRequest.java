package peakspace.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostUpdateRequest {
    private String description;
    private String location;
    private boolean isBlockComment;
}
