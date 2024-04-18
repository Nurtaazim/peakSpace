package peakspace.dto.response;

import lombok.Builder;

@Builder
public record ResponseWithGoogle(Long id,
                                 String token) {
}
