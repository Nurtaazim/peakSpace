package peakspace.dto.response;
import lombok.Builder;
import java.util.List;

@Builder
public record MyPostResponse(Long id,
                             Long userId,
                             List<String> links,
                             int countLikes,
                             String avatar,
                             String userName,
                             String location,
                             List<CommentResponse> commentResponses) {
}
