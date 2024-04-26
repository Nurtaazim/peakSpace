package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private Long senderId;
    private Long recipientId;
    private String content;
}
