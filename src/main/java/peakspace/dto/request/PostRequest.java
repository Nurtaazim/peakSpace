package peakspace.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class PostRequest {
    @Nullable
    private List<String> links = new ArrayList<>();
    private String description;
    private String location;
    private boolean isBlockComment;
}
