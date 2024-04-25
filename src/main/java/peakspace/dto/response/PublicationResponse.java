package peakspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

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
