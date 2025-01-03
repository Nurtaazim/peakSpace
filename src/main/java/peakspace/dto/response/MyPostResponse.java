package peakspace.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MyPostResponse(Long postId,
                             Long userId,
                             String avatar,
                             String userName,
                             String location,
                             String description,
                             int countLikes,
                             List<LinkResponse> links,
                             List<CommentResponse> commentResponses) {
}
