package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import peakspace.enums.Role;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Boolean isBlock;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Profile profile;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Story> stories;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Chat> chats;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Comment> comments;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<PablicProfile> pablicProfiles;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Publication> publications;
    @OneToOne(mappedBy = "userNotification",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Notification notification;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

}