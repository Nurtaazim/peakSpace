package peakspace.dto.response;

import lombok.Builder;

@Builder
public record ResponseWithGoogle(Long idUser,
                                 String description,
                                 String token) {
}
