package peakspace.dto.response;

import lombok.Builder;

@Builder
public record StoryAllHomPageResponse(
        Long userId,
        String avatar,
        String username
) {
}
