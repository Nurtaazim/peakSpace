package peakspace.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record GetAllPostsResponse(Long userId,
                                  String cover,
                                  String avatar,
                                  String userName,
                                  String aboutMe,
                                  String major,
                                  int countFriends,
                                  int countPablics,
                                  List<Map<Long, String>> publications) {
}
