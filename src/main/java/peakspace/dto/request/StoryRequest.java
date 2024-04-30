package peakspace.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record StoryRequest(
        List <String> PhotoUrlOrVideoUrl,
        String description
) {

}
