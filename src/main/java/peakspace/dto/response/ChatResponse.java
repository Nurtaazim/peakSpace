package peakspace.dto.response;

import lombok.Builder;
import peakspace.entities.MessageContent;

import java.util.List;

@Builder
public record ChatResponse(
        Long id,
        String avatar,
        String userName,
        String aboutYourSelf,
        List<MessageResponse> messageContents
) {
    public ChatResponse(Long id, String avatar, String userName, String aboutYourSelf) {
        this(id, avatar, userName, aboutYourSelf, List.of());
    }
}
