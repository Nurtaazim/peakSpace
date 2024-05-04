package peakspace.entities;

import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "public_p_seq")
    @SequenceGenerator(name = "public_p_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    private String cover;
    private String avatar;
    private String pablicName;
    private String descriptionPublic;
    @Enumerated(EnumType.STRING)
    private Tematica tematica;
    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST}) //ManyToOne
    private User user;
    @ManyToMany
    private List<User> users;
    @OneToMany(mappedBy = "pablicProfile",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Publication> publications;

}