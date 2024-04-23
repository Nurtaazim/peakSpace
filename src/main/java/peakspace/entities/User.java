package peakspace.entities;
import jakarta.persistence.*;
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

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isBlock;
    private Boolean blockAccount;
    private String confirmationCode;
    private ZonedDateTime createdAt;
    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Profile profile;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Story> stories;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "sender",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Chat> chats;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Comment> comments;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<PablicProfile> pablicProfiles;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Publication> publications;
    @OneToMany(mappedBy = "userNotification",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Notification> notifications;
    @ElementCollection
    private List<Long> searchFriendsHistory;

    public String getThisUserName() {
        return this.userName;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }

    public List<User> getFriendsWithChats() {
        List<User> friends = new ArrayList<>();
        for (Chat chat : chats) {
            if (chat.getSender().equals(this)) {
                friends.add(chat.getReceiver());
            } else {
                friends.add(chat.getSender());
            }
        }
        return friends.stream().distinct().collect(Collectors.toList());
    }

}