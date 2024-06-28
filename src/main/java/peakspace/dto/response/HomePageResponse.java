package peakspace.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private boolean isLike;
    private boolean isFavorite;
    private boolean fromMyBlockAccount;

}
