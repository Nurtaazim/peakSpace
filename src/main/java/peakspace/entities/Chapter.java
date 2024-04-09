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
import jakarta.persistence.CascadeType;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "chapter_seq", allocationSize = 1)
    private Long id;
    private String groupName;

    @OneToMany
    private List<User> friends;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User user;

}