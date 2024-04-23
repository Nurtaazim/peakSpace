package peakspace.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.ChatRequest;
import peakspace.dto.response.ChatResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserChatResponse;
import peakspace.entities.Chat;
import peakspace.entities.User;
import peakspace.repository.ChatRepository;
import peakspace.repository.UserRepository;
import peakspace.service.ChatService;
import peakspace.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatAPI {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final JwtService jwtService;

        @MessageMapping("/chat")
        public void chat(@Payload ChatRequest chatRequest) {
            System.out.println("WebSocket chat method called.");

            User sender = userRepository.findById(chatRequest.getSenderId()).orElse(null);
            User recipient = userRepository.findById(chatRequest.getRecipientId()).orElse(null);

            if (sender != null && recipient != null) {
                Chat chat = chatRepository.findByUsers(sender, recipient);
                if (chat == null) {
                    chat = chatService.createChatBetweenUsers(sender,recipient);
                }
                chatService.sendMessage(chat.getId(), sender, chatRequest.getContent());
            }
        }

    @GetMapping("/findById/{currentUserId}/{foundUserId}")
    @ApiOperation("Send friends to a specific user in a chapter")
    public ChatResponse findById(@PathVariable Long currentUserId,@PathVariable Long foundUserId) {
        return chatService.findChatId(currentUserId,foundUserId);
    }

    @GetMapping("/findAllChat/")
    @ApiOperation(value = "Все чаты текущего пользователя")
    public List<UserChatResponse> findAllChat() {
        return chatService.findAllBy();
    }

    @PostMapping("/delete/{chatId}")
    @ApiOperation(value = "Удаление чат ")
    public SimpleResponse deleteChatId(@PathVariable Long chatId) {
            return chatService.deleteChatId(chatId);
    }

    @DeleteMapping("/delete/message/{messageId}")
    @ApiOperation(value = "Удалить сообщение !")
    public SimpleResponse deleteMessage(@PathVariable Long messageId) {
            return chatService.deleteMessageId(messageId);
    }

    @PutMapping("/edit/message/{messageId}")
    @ApiOperation(value = "Изменение  сообщение !")
    public SimpleResponse editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
            return chatService.edit(messageId,newContent);
    }



}


