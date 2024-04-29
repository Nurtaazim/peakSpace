package peakspace.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record FriendsPageResponse(Long userId,
                                  Map<Long, String> chapters,
                                  List<FriendsResponse> friendsResponsesList) {
}
