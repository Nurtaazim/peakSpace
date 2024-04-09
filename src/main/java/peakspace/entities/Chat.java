package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "chat",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<MessageContent>messageContents;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User user;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Link_Publication> linkPublications;

}