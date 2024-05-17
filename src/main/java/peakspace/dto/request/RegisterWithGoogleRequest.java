package peakspace.dto.request;

import lombok.Builder;

@Builder
public record RegisterWithGoogleRequest(
        Long idVerifierUser,
        String confirmationCode,
        String tokenGoogle) {

}
