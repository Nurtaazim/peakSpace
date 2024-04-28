package peakspace.dto.request;

import lombok.Builder;

@Builder
public record StoryRequest(
        String PhotoUrlOrVideoUrl,
        String description
) {

}
