package peakspace.dto.response;

import lombok.Builder;

@Builder
public record S3Response(SimpleResponse simpleResponse,
                         Object object) {
}
