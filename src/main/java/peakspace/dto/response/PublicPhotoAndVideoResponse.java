package peakspace.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record PublicPhotoAndVideoResponse(Map<Long, String> publicationsPublic) {
}
