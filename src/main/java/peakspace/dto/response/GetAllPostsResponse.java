package peakspace.dto.response;
import lombok.Builder;
import java.util.Map;

@Builder
public record GetAllPostsResponse(String cover,
                                  String avatar,
                                  String userName,
                                  String aboutMe,
                                  String major,
                                  int countFriends,
                                  int countPublics,
                                  Map<Long, String> publications) {
}
