package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.ChatRequest;
import peakspace.dto.response.ChatResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserChatResponse;
import peakspace.entities.Chat;
import peakspace.entities.User;
import peakspace.repository.ChatRepository;
import peakspace.repository.UserRepository;
import peakspace.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatAPI {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping
    public void chat(@Payload ChatRequest chatRequest) {
        System.out.println("WebSocket chat method called.");

        User sender = userRepository.findById(chatRequest.getSenderId()).orElse(null);
        User recipient = userRepository.findById(chatRequest.getRecipientId()).orElse(null);

        if (sender != null && recipient != null) {
            Chat chat = chatRepository.findByUsers(sender, recipient);
            if (chat == null) {
                chat = chatService.createChatBetweenUsers(sender, recipient);
            }
            chatService.sendMessage(chat.getId(), sender, chatRequest.getContent());

            ChatResponse chatResponse = chatService.findChatResponse(chatRequest.getSenderId(), chatRequest.getRecipientId());
            simpMessagingTemplate.convertAndSendToUser(recipient.getId().toString(), "/queue/chat", chatResponse);
        }
    }

    @Secured("USER")
    @GetMapping("/findById/{currentUserId}/{foundUserId}")
    @Operation(summary = "Send friends to a specific user in a chapter")
    public ChatResponse findById(@PathVariable Long currentUserId, @PathVariable Long foundUserId) {
        return chatService.findChatId(currentUserId, foundUserId);
    }

    @Secured("USER")
    @GetMapping("/findAllChat/")
    @Operation(summary = "Все чаты текущего пользователя")
    public List<UserChatResponse> findAllChat() {
        return chatService.findAllBy();
    }

    @Secured("USER")
    @PostMapping("/delete/{chatId}")
    @Operation(summary = "Удаление чат ")
    public SimpleResponse deleteChatId(@PathVariable Long chatId) {
        return chatService.deleteChatId(chatId);
    }

    @Secured("USER")
    @DeleteMapping("/delete/message/{messageId}")
    @Operation(summary = "Удалить сообщение !")
    public SimpleResponse deleteMessage(@PathVariable Long messageId) {
        return chatService.deleteMessageId(messageId);
    }

    @Secured("USER")
    @PutMapping("/edit/message/{messageId}")
    @Operation(summary = "Изменение  сообщение !")
    public SimpleResponse editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
        return chatService.edit(messageId, newContent);
    }

}