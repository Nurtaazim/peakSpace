package peakspace.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationResponse {
    private Long id;
    private List<LinkPublicationResponse> linkPublications;


    public PublicationResponse(Long id) {
        this.id = id;
    }
}
