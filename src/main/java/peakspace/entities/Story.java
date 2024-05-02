package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "story_seq")
    @SequenceGenerator(name = "story_seq", allocationSize = 1,initialValue = 19)
    private Long id;
    private ZonedDateTime createdAt;
    private String text;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    private User owner;
    @ManyToMany
<<<<<<< HEAD
    private List<User> tagFriends;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
=======
    private List<User> tagFiends;
    @ManyToMany()
>>>>>>> 0ac0315f4a1cff4c0c42d3bfc6dea882ccb42689
    private List<Like> likes;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Link_Publication> linkPublications;
    @PrePersist
    public void prePersist(){
        this.createdAt = ZonedDateTime.now();
    }

}