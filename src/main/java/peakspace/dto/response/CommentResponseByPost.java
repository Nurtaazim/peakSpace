package peakspace.dto.response;

import lombok.Builder;

import java.util.List;
@Builder
public record CommentResponseByPost (
        Long id,
        Long userId,
        String avatar,
        String userName,
        String location,
        int countLikes,
        List<LinkResponse> links,
        List<CommentResponse> commentResponses
){

}
