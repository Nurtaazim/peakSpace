package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.ElementCollection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    @SequenceGenerator(name = "profile_seq", allocationSize = 1)
    private Long id;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String fullName;
    private String phoneNumber;
    private String profession;

    @ElementCollection
    private List<Long> favorites;
    @OneToMany(mappedBy = "profile")
    private List<Education> educations;
    @OneToOne
    private User user;
}
