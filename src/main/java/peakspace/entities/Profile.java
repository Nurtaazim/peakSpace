package peakspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AA")
    @SequenceGenerator(name = "AA", allocationSize = 1, initialValue = 21)
    private Long id;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String profession;
    private String phoneNumber;
    private boolean workOrNot;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> favorites;
    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Education> educations;
    @OneToOne()
    private User user;

    public Profile(String firstName, String lastName, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.user = user;
    }
}