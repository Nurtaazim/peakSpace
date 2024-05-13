package peakspace.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PostLinkResponse(
        Long postId,
        List<LinkResponse> linkResponses,
        int countLikes,
        int countComments

) {
}
