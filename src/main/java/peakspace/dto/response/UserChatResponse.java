package peakspace.dto.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record UserChatResponse(Long id,
                               String username,
                               String content,
                               Date timestamp) {

    public UserChatResponse(Long id, String username) {
        this(id, username, "", null);
    }
}
