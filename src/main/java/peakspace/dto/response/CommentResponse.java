package peakspace.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse{

    private Long id;
    private String avatar;
    private String userName;
    private String comment;
    private int countLike;
    private ZonedDateTime createdAt;
    private List<InnerCommentResponse> innerComments;

}
