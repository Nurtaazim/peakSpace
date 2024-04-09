package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "message_content")
public class MessageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "message_content_seq", allocationSize = 1)
    private long id;
    private String content;
    private ZonedDateTime timestamp;
    private boolean readOrNotRead;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private Chat chat;

}