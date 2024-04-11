package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "profile_seq", allocationSize = 1,initialValue = 11)
    private Long id;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String fullName;
    private String phoneNumber;
    private String profession;
    @ElementCollection
    private List<Long> favorites;
    @OneToMany(mappedBy = "profile",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Education> educations;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private User user;

}