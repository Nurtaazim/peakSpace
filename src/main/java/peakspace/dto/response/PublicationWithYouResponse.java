package peakspace.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PublicationWithYouResponse {
    private Long id;
    private String description;
    private String location;
    private List<LinkPublicationResponse> links;

}
