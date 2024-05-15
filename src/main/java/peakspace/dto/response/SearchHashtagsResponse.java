package peakspace.dto.response;

import lombok.Builder;
import peakspace.entities.Link_Publication;

@Builder
public record SearchHashtagsResponse(Long id,
                                     Link_Publication linkPublications
) {
}
