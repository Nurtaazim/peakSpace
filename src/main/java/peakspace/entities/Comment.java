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
    @OneToOne(mappedBy = "comment",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Notification notification;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private Publication publication;
    @OneToMany
    @JoinTable(name = "innerComment")
    private List<Comment> innerComments;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "comment")
    private List<Like> likes;

    @PrePersist
    public void prePersist(){
        this.createdAt = ZonedDateTime.now();
    }

}