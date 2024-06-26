package peakspace.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "message_content")
public class MessageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_content_seq")
    @SequenceGenerator(name = "message_content_seq", allocationSize = 1, initialValue = 19)
    private long id;
    private String content;
    private Date timestamp;
    private boolean readOrNotRead;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Chat chat;

}