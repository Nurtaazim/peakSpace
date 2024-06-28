package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AA")
    @SequenceGenerator(name = "AA", allocationSize = 1, initialValue = 21)
    private Long id;
    @Column(length = 10000)
    private String avatar;
    @Column(length = 10000)
    private String cover;
    @Column(length = 1000)
    private String aboutYourSelf;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String profession;
    private String phoneNumber;
    private boolean workOrNot;
    private String location;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> favorites;
    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Education> educations;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Profile(String firstName, String lastName, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.user = user;
        this.avatar = "https://bit.ly/3KMgyZ4";
        this.cover = "https://bit.ly/3VK5PUn";
    }


}