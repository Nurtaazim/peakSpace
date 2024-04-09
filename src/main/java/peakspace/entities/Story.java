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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_seq")
    @SequenceGenerator(name = "story_seq", allocationSize = 1)
    private Long id;
    private ZonedDateTime createdAd;
    private ZonedDateTime expDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    private User owner;
    @OneToMany
    private List<User> tagFiends;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Like> likes;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Link_Publication> linkPublications;


}