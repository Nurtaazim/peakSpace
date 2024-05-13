package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String massage;
    private String senderUserName;
    private LocalDate createdAt;
    private String publicationOrStoryImageUrl;
    private String senderProfileImageUrl;
    private long senderUserId;
    private long publicationOrStoryOrCommentId;
}
