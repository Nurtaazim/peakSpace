package peakspace.dto.response;

import lombok.Builder;

@Builder
public record AllFriendsResponse(Long idUser,
                                 String avatar,
                                 String userName,
                                 String aboutMe,
                                 boolean isMyFriend) {
}
