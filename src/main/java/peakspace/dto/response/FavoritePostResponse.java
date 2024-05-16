package peakspace.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record FavoritePostResponse(
        Map<Long, String> publications) {

}
