package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Tematica;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pablic_profiles")
public class PablicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "public_p_seq", allocationSize = 1)
    private Long id;
    private String cover;
    private String avatar;
    private String pablicName;
    private Tematica tematica;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private User user;
    @ManyToMany
    private List<User> users;
    @OneToMany(mappedBy = "pablicProfile",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Publication> publications;

}