package peakspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "not_seq")
    @SequenceGenerator(name = "not_seq", allocationSize = 1, initialValue = 11)
    private Long id;
    private ZonedDateTime createdAt;
    private boolean seen;
    private String notificationMessage;
    @OneToOne
    private Like like;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private User userNotification;   // получатель
    private Long senderUserId;       // отправитель
    @ManyToOne
    private Comment comment;
    @ManyToOne
    private Publication publication;
    @ManyToOne
    private Story story;

    public Notification(Like like, User userNotification, Long senderUserId) {
        this.like = like;
        this.userNotification = userNotification;
        this.senderUserId = senderUserId;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = ZonedDateTime.now();
    }
}