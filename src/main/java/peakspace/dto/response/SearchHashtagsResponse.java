package peakspace.dto.response;
import lombok.Builder;
import peakspace.entities.Link_Publication;

import java.util.List;

@Builder
public record SearchHashtagsResponse(
        Long id,
        Link_Publication linkPublications
) {
}
