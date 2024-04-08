package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private User owner;
    @OneToMany
    private List<User> tagFiends;
    @OneToMany()
    private List<Like> likes;
    @OneToMany(mappedBy = "stories")
    private List<Link_Publication> linkPublications;


}