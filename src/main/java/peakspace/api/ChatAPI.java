package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatAPI {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping
    @CrossOrigin(origins = "*", maxAge = 3600)
    public void chat(@Payload ChatRequest chatRequest) {

        log.info("Message mapping method is working");
        User sender = userRepository.findById(chatRequest.getSenderId()).orElse(null);
        User recipient = userRepository.findById(chatRequest.getRecipientId()).orElse(null);

        if (sender != null && recipient != null) {
            Chat chat = chatRepository.findByUsers(sender, recipient);

            log.info("in first if");
            if (chat == null) {
                log.info("in second if");
                chat = chatService.createChatBetweenUsers(sender, recipient);
            }
            chatService.sendMessage(chat.getId(), sender, chatRequest.getContent());
            log.info("message sent");

            ChatResponse chatResponse = chatService.findChatResponse(chatRequest.getSenderId(), chatRequest.getRecipientId());
            simpMessagingTemplate.convertAndSendToUser(recipient.getId().toString(), "/queue/chat", chatResponse);
        }
    }

    @Secured("USER")
    @GetMapping("/find/{currentUserId}/{foundUserId}")
    @Operation(summary = "Send friends to a specific user in a chapter")
    public ChatResponse findById(@PathVariable Long currentUserId,
                                 @PathVariable Long foundUserId) {
        log.info("find by id");
        return chatService.findChatId(currentUserId, foundUserId);
    }

    @Secured("USER")
    @GetMapping
    @Operation(summary = "Все чаты текущего пользователя")
    public List<UserChatResponse> findAllChat() {
        log.info("find all chats");
        return chatService.findAllBy();
    }

    @Secured("USER")
    @PostMapping("/{chatId}")
    @Operation(summary = "Удаление чат ")
    public SimpleResponse deleteChatId(@PathVariable Long chatId) {
        log.info("delete chat id {}", chatId);
        return chatService.deleteChatId(chatId);
    }

    @Secured("USER")
    @DeleteMapping("/message/{messageId}")
    @Operation(summary = "Удалить сообщение !")
    public SimpleResponse deleteMessage(@PathVariable Long messageId) {
        log.info("delete message id {}", messageId);
        return chatService.deleteMessageId(messageId);
    }

    @Secured("USER")
    @PutMapping("/message/{messageId}")
    @Operation(summary = "Изменение  сообщение !")
    public SimpleResponse editMessage(@PathVariable Long messageId,
                                      @RequestBody String newContent) {
        log.info("edit message id {}", messageId);
        return chatService.edit(messageId, newContent);
    }

}