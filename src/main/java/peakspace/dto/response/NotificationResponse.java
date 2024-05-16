package peakspace.dto.response;

import lombok.*;

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
