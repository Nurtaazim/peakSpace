package peakspace.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
public class PostRequest {
    private List<String> links;
    private String description;
    private String location;
    private boolean isBlockComment;
}
