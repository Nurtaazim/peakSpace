package peakspace.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse {
    private String userPhoto;
    private String userName;
    private List<String> photosOrVideosLink;
    private String text;
    private LocalDate createdAt;
}
