package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "likes_seq", allocationSize = 1)
    private Long id;
    private boolean isLike;

    @OneToOne
    private User user;
//    @ManyToOne
//    private Story story;
//    @ManyToOne
//    private Comment comment;
//    @OneToOne(mappedBy = "like")
//    private Notification notification;
//    @ManyToOne
//    private Publication publication;


}