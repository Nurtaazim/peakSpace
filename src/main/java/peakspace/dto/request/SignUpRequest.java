package peakspace.dto.request;

import lombok.Builder;

@Builder
public record SignUpRequest(
        String surName,
        String name,
        String userName,
        String email,
        String password
) {
}
