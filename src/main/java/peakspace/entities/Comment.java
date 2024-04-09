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
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinTable;
import jakarta.persistence.CascadeType;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "comment_seq", allocationSize = 1)
    private Long id;
    private String message;
    private ZonedDateTime createdAt;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private User user;
    @OneToOne(mappedBy = "comment",cascade = {CascadeType.PERSIST})
    private Notification notification;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private Publication publication;
    @OneToMany
    @JoinTable(name = "innerComment")
    private List<Comment> innerComments;

}