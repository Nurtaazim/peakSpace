package peakspace.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    @OneToMany(mappedBy = "chat",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MessageContent>messageContents;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User sender;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User receiver;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Link_Publication> linkPublications;


    public MessageContent getLastMessage() {
        if (messageContents != null && !messageContents.isEmpty()) {
            return messageContents.get(messageContents.size() - 1);
        }
        return null;
    }
}