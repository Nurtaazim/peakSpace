package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Builder
public record FavoritePostResponse(Map<Long, String> publications) {

}
