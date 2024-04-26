package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import peakspace.dto.response.ChatResponse;
import peakspace.entities.Chat;
import peakspace.entities.MessageContent;
import peakspace.entities.User;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    @Query("select c from Chat c " +
            "join  c.sender sender " +
            "join  c.receiver recipient " +
            "join  c.messageContents mc " +
            "where sender = :sender and recipient = :recipient")
        Chat findByUsers(User sender, User recipient);


    @Query("select new peakspace.dto.response.ChatResponse(u.id,p.avatar,u.userName,p.aboutYourSelf) from User u left join u.profile p where u.id =:foundUserId")
    ChatResponse findByChatId(Long foundUserId);

    @Modifying
    @Transactional
    @Query(value = "delete from message_content where chat_id =:chatId",nativeQuery = true)
    void deleteMessage(Long chatId);

    @Modifying
    @Transactional
    @Query(value = "delete from chats_link_publications where chat_id =:chatId",nativeQuery = true)
    void deleteLink(Long chatId);

    @Query("select mc from Chat c " +
            "join c.messageContents mc " +
            "where (c.sender = :user1 AND c.receiver = :user2) or" +
            "(c.sender = :user2 and c.receiver = :user1)")
    List<MessageContent> findMessagesBetweenUsers(User user1, User user2);

}