package peakspace.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class PostRequest {
    private String link;
    private String description;
    private String location;
    private boolean isBlockComment;
}
