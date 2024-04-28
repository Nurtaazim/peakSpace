package peakspace.dto.response;

import lombok.Builder;

@Builder
public record FriendsResponse(Long userId,
                              String avatar,
                              String userName,
                              String aboutMe) {
}
