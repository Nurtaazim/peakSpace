package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import peakspace.enums.Role;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = LAZY)
    private Profile profile;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = LAZY)
    private List<Story> stories;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = LAZY)
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = LAZY)
    private List<Chat> chats;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = LAZY)
    private List<Publication> publications;
    @OneToMany(mappedBy = "userNotification", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = LAZY)
    private List<Notification> notifications;
    @ElementCollection(fetch = LAZY)
    private List<Long> searchFriendsHistory;
    @ElementCollection(fetch = LAZY)
    private List<Long> blockAccounts;
    @ElementCollection(fetch = LAZY)
    private List<Long> publicProfilesSize;
    @ElementCollection(fetch = LAZY)
    private List<Long> myAcceptPost;
    @OneToOne(mappedBy = "owner", fetch = LAZY)
    private PablicProfile community;
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
        return email;
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