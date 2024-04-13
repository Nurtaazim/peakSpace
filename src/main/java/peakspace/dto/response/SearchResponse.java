package peakspace.dto.response;

import lombok.Builder;

@Builder
public record SearchResponse(
        Long id,
        String name,
        String avatar,
        String description
) {
}
