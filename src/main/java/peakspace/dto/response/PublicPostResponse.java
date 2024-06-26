package peakspace.dto.response;


import java.time.ZonedDateTime;
import java.util.List;

public record PublicPostResponse(Long id,
                                 Long userId,
                                 String avatar,
                                 String userName,
                                 String location,
                                 String description,
                                 int countLikes,
                                 ZonedDateTime createdAt,
                                 List<LinkResponse> links,
                                 boolean blockComment
) {
}
