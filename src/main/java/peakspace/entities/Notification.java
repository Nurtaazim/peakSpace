package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "not_seq")
    @SequenceGenerator(name = "not_seq", allocationSize = 1, initialValue = 11)
    private Long id;
    private ZonedDateTime createdAt;
    private boolean seen;
    private String notificationMessage;
    @ManyToOne(fetch = FetchType.LAZY)
    private Like like;
    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    private User userNotification;   // получатель
    private Long senderUserId;       // отправитель
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
    @ManyToOne(fetch = FetchType.LAZY)
    private Publication publication;
    @ManyToOne(fetch = FetchType.LAZY)
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