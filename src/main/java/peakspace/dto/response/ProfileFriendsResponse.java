package peakspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class ProfileFriendsResponse {
    private Long id;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String profession;
    private int friendsSize;
    private int publicationsSize;

    public ProfileFriendsResponse(Long id, String avatar, String cover, String aboutYourSelf, String profession) {
        this.id = id;
        this.avatar = avatar;
        this.cover = cover;
        this.aboutYourSelf = aboutYourSelf;
        this.profession = profession;
    }
}
