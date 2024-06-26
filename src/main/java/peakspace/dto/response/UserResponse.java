package peakspace.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String avatar,
        String firstName,
        String lastName,
        String userName,
        String email) {
}
