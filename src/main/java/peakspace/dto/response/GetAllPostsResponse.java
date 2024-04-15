package peakspace.dto.response;
import lombok.Builder;
import java.util.Map;

@Builder
public record GetAllPostsResponse(String cover,
                                  String avatar,
                                  String userName,
                                  String aboutMe,
                                  String major,
                                  Long countFriends,
                                  String countPublics,
                                  Map<Long, String> publications) {
}
