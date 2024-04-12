package peakspace.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(String token, SimpleResponse simpleResponse) {
}
