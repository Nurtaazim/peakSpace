package peakspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import peakspace.enums.Role;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String userName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isBlock;
    private Boolean blockAccount;
    private String confirmationCode;
    private ZonedDateTime createdAt;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Profile profile;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Story> stories;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.EAGER)
    private List<Chat> chats;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.EAGER)
    private List<Comment> comments;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.EAGER)
    private PablicProfile pablicProfiles;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Publication> publications;
    @OneToMany(mappedBy = "userNotification", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Notification> notifications;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> searchFriendsHistory;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> blockAccounts;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> publicProfilesSize;

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
    public String getUsername() {
        return userName;
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