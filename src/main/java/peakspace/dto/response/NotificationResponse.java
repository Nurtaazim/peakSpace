package peakspace.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String message;
    private String senderUserName;
    private LocalDate createdAt;
    private String publicationOrStoryImageUrl;
    private String senderProfileImageUrl;
    private boolean seen;
    private Long senderUserId;
    private Long publicationId;
    private Long commentId;
    private Long storyId;
}
