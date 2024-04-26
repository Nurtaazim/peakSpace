package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MessageChatResponse {

    private Long chatId;
    private Long senderId;
    private Long recipientId;
}
