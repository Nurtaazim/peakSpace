package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.config.jwt.JwtService;
import peakspace.dto.response.ChatResponse;
import peakspace.dto.response.MessageResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserChatResponse;
import peakspace.entities.Chat;
import peakspace.entities.MessageContent;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.IllegalArgumentException;
import peakspace.exception.NotFoundException;
import peakspace.repository.ChatRepository;
import peakspace.repository.MessageContentRepository;
import peakspace.repository.UserRepository;
import peakspace.service.ChatService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageContentRepository messageContentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtService jwtService;


    @Transactional
    @Override
    public Chat createChatBetweenUsers(User currentUser,User userReceiver) {
        User managedCurrentUser = userRepository.findById(currentUser.getId()).orElse(null);
        User managedUserReceiver = userRepository.findById(userReceiver.getId()).orElse(null);

        log.info("In create chat method");
        if (managedCurrentUser != null && managedUserReceiver != null) {
            log.info("in first if");
            Chat existingChat = chatRepository.findByUsers(managedCurrentUser, managedUserReceiver);
            if (existingChat != null) {
                log.info("existing chat");
                return existingChat;
            } else {
                log.info("existing chat");
                Chat chat = new Chat();
                chat.setSender(managedCurrentUser);
                chat.setReceiver(managedUserReceiver);
                chat.setMessageContents(new ArrayList<>());
                return chatRepository.save(chat);
            }
        } else {
            throw new IllegalArgumentException(" Плохой запрос !");
        }
    }

    @Transactional
    @Override
    public void sendMessage(Long chatId,User sender,String content) {

        Chat chat = chatRepository.findById(chatId).orElse(null);
        log.info("In sendMessage method");
        if (chat != null) {
            log.info("In chat non null");
            MessageContent message = new MessageContent();
            message.setContent(content);

            message.setTimestamp(Date.from(ZonedDateTime.now().toInstant()));
            message.setReadOrNotRead(false);
            message.setChat(chat);
            MessageContent savedMessage = messageContentRepository.save(message);
            chat.getMessageContents().add(savedMessage);
            simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), message, Collections.singletonMap("token", jwtService.createToken(sender)));
        } else {
            throw new NotFoundException("Нет такого чат!");
        }
    }


    private Chat getLastChat(User currentUser, User friend) {
        log.info("In getLastChat method");
        List<Chat> chats = currentUser.getChats();
        for (Chat chat : chats) {
            if (chat.getSender().equals(friend) || chat.getReceiver().equals(friend)) {
                return chat;
            }
        }
        return null;
    }


    public ChatResponse findChatResponse(Long currentUserId, Long foundUserId) {
        log.info("In findChatResponse method");
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User foundUser = userRepository.findById(foundUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Chat chat = chatRepository.findByUsers(currentUser, foundUser);

        if (chat == null) {
            chat = createChatBetweenUsers(currentUser, foundUser);
        }

        List<MessageContent> allMessages = chat.getMessageContents();

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (MessageContent messageContent : allMessages) {
            MessageResponse messageResponse = MessageResponse.builder()
                    .id(messageContent.getId())
                    .content(messageContent.getContent())
                    .timestamp(messageContent.getTimestamp())
                    .readOrNotRead(messageContent.isReadOrNotRead())
                    .build();
            messageResponses.add(messageResponse);
        }

        return ChatResponse.builder()
                .id(foundUser.getId())
                .avatar(foundUser.getProfile().getAvatar())
                .userName(foundUser.getUsername())
                .aboutYourSelf(foundUser.getProfile().getAboutYourSelf())
                .messageContents(messageResponses)
                .build();
    }


    @Override
    @Transactional
    public ChatResponse findChatId(Long currentUserId,Long foundUserId) {
        log.info("In findChatId method");
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        User foundUser = userRepository.findById(foundUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<MessageContent> allMessages = chatRepository.findMessagesBetweenUsers(currentUser, foundUser);

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (MessageContent messageContent : allMessages) {
            MessageResponse messageResponse = MessageResponse.builder()
                    .id(messageContent.getId())
                    .content(messageContent.getContent())
                    .timestamp(messageContent.getTimestamp())
                    .readOrNotRead(messageContent.isReadOrNotRead())
                    .build();
            messageResponses.add(messageResponse);
        }

        ChatResponse chatResponse = ChatResponse.builder()
                .id(foundUser.getId())
                .avatar(foundUser.getProfile().getAvatar())
                .userName(foundUser.getUsername())
                .aboutYourSelf(foundUser.getProfile().getAboutYourSelf())
                .messageContents(messageResponses)
                .build();

        for (MessageContent message : allMessages) {
            message.setReadOrNotRead(true);
        }

        return chatResponse;
    }

    @Override
    public List<UserChatResponse> findAllBy() {
        log.info("findAllBy method");
        User currentUser = getCurrentUser();
        List<User> currentUserFriends = currentUser.getFriendsWithChats();

        List<UserChatResponse> userResponse = new ArrayList<>();

        for (User friend : currentUserFriends) {
            Chat lastChat = getLastChat(currentUser, friend);
            if (lastChat != null) {
                MessageContent lastMessage = lastChat.getLastMessage();
                if (lastMessage != null) {
                    UserChatResponse response = UserChatResponse.builder()
                            .id(friend.getId())
                            .username(friend.getUsername())
                            .content(lastMessage.getContent())
                            .timestamp(lastMessage.getTimestamp())
                            .build();
                    userResponse.add(response);
                }
            }
        }
        return userResponse;
    }

    @Override
    public SimpleResponse deleteChatId(Long chatId) {
        log.info("In deleteChatId method");
        chatRepository.deleteMessage(chatId);
        chatRepository.deleteLink(chatId);
        chatRepository.deleteById(chatId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно удалено чат !")
                .build();
    }

    @Override
    public SimpleResponse deleteMessageId(Long messageId) {
        log.info("In deleteMessageId method");
        messageContentRepository.deleteById(messageId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно удалено сообщение  !")
                .build();
    }

    @Override
    public SimpleResponse edit(Long messageId, String newContent) {
        log.info("In edit method");
        Optional<MessageContent> optionalMessage = messageContentRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            MessageContent message = optionalMessage.get();
            message.setContent(newContent);
            messageContentRepository.save(message);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Сообщение успешно изменено!")
                    .build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Сообщение с указанным ID не найдено")
                    .build();
        }

    }


    private User getCurrentUser() {
        log.info("In getCurrentUser method");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("email: " + email);
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Ошибка 403! \nДоступ запрещен: у вас нет необходимых прав. ");
    }
}
