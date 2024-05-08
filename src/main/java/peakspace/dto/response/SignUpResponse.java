package peakspace.dto.response;

import lombok.Builder;

@Builder
public record SignUpResponse(Long userId,
                             String message
) {}