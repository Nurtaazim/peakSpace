package peakspace.dto.response;

import peakspace.enums.Tematica;

import java.util.List;

public record PublicPostResponse(
        Long id,
        Long userId,
        String avatar,
        String userName,
        Tematica tematica,
        int countLikes,
        List<LinkResponse> links,
        List<CommentResponse> commentResponses
) {
}
