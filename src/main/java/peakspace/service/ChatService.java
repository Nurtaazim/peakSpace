package peakspace.service;

import peakspace.dto.response.ChatResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserChatResponse;
import peakspace.entities.Chat;
import peakspace.entities.User;

import java.util.List;

public interface ChatService {

    Chat createChatBetweenUsers(User currentUser, User userReceiver);

    void sendMessage(Long chatId, User sender, String content);

    List<UserChatResponse> findAllBy();

    ChatResponse findChatId(Long currentUserId, Long foundUserId);

    SimpleResponse deleteChatId(Long chatId);

    SimpleResponse deleteMessageId(Long messageId);

    SimpleResponse edit(Long messageId, String newContent);

    ChatResponse findChatResponse(Long senderId, Long recipientId);
}
