package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "likes_seq")
    @SequenceGenerator(name = "likes_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Notification notification;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Story story;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Publication publication;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Comment comment;

}