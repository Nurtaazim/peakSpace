package peakspace.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HomePageResponse {

    Long id;
    String avatar;
    String username;
    String location;
    Long postId;
    String description;
    List<LinkPublicationResponse> linkPublicationResponseList;
    int countLikes;
    int countComments;

}
