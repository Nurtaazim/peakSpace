package peakspace.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StoryResponse {
    private String userPhoto;
    private String userName;
    private List<String> photosOrVideosLink;
    private String text;
    private LocalDate createdAt;
}
