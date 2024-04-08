package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "chat_seq", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "chat")
    private List<MessageContent>messageContents;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "chat")
    private List<Link_Publication> linkPublications;
}