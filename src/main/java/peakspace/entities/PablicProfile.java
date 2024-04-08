package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public_p_seq")
    @SequenceGenerator(name = "public_p_seq", allocationSize = 1)
    private Long id;
    private String cover;
    private String avatar;
    private String pablicName;
    private Tematica tematica;

    @ManyToOne
    private User user;
    @ManyToMany
    private List<User> users;
    @OneToMany(mappedBy = "pablicProfile")
    private List<Publication> publications;

}