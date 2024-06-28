package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peakspace.enums.Tematica;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pablic_profiles")
@AllArgsConstructor
@NoArgsConstructor
public class PablicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public_p_seq")
    @SequenceGenerator(name = "public_p_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Column(length = 10000)
    private String cover;
    @Column(length = 10000)
    private String avatar;
    private String pablicName;
    private String descriptionPublic;
    @Enumerated(EnumType.STRING)
    private Tematica tematica;
    @OneToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    private User owner;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> users;
    @OneToMany(mappedBy = "pablicProfile", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Publication> publications = new ArrayList<>();
    @OneToMany(cascade = CascadeType.DETACH)
    private List<User> blockUsers = new ArrayList<>();
}