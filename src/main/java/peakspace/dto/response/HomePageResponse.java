package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HomePageResponse {

    private Long id;
    private String avatar;
    private String username;
    private String location;
    private Long postId;
    private String description;
    private List<LinkPublicationResponse> linkPublicationResponseList;
    private int countLikes;
    private int countComments;

}
