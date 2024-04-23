package peakspace.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import peakspace.entities.Link_Publication;

import java.time.ZonedDateTime;
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
