package peakspace.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record FavoritePostResponse(
        List<Map<Long, String>> publications) {

}
