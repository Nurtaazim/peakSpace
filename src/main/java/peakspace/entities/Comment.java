package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    private String message;
    private ZonedDateTime createdAt;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private User user;
    @OneToMany
    private List<Notification> notifications;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private Publication publication;
    @OneToMany
    @JoinTable(name = "innerComment")
    private List<Comment> innerComments;
    @ManyToMany()
    private List<Like> likes;

    @PrePersist
    public void prePersist(){
        this.createdAt = ZonedDateTime.now();
    }

    public void setNotification(Notification notification) {
        this.notifications.add(notification);
    }
}