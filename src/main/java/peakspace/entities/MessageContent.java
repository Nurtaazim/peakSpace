package peakspace.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "message_content")
public class MessageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "message_content_seq")
    @SequenceGenerator(name = "message_content_seq", allocationSize = 1, initialValue = 19)
    private long id;
    private String content;
    private ZonedDateTime timestamp;
    private boolean readOrNotRead;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    private Chat chat;

}