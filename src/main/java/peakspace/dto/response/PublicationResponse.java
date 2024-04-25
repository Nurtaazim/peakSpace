package peakspace.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import peakspace.entities.Link_Publication;

<<<<<<< HEAD
import java.time.ZonedDateTime;
import java.util.List;

=======
>>>>>>> origin/main
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
