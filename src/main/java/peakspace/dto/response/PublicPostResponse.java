package peakspace.dto.response;


import java.util.List;

public record PublicPostResponse(Long id,
                                 Long userId,
                                 String avatar,
                                 String userName,
                                 String location,
                                 String description,
                                 int countLikes,
                                 List<LinkResponse> links,
                                 List<CommentResponse> commentResponses,
                                 boolean blockComment
) {
}
