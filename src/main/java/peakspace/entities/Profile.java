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
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "AA")
    @SequenceGenerator(name = "AA", allocationSize = 1,initialValue = 21)
    private Long id;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String profession;
    private boolean workOrNot;
    @ElementCollection
    private List<Long> favorites;
    @OneToMany(mappedBy = "profile",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Education> educations;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private User user;

}