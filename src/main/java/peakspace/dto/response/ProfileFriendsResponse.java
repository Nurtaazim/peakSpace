package peakspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private Long friendsSize;
    private Long pablicationsSize;

    private List<PublicationResponse> friendsPublications = new ArrayList<>();
    private List<PublicationResponse> friendsFavoritesPublications = new ArrayList<>();
    private List<PublicationResponse> friendsWitMePublications = new ArrayList<>();

    public ProfileFriendsResponse(Long id, String avatar, String cover, String aboutYourSelf, String profession) {
        this.id = id;
        this.avatar = avatar;
        this.cover = cover;
        this.aboutYourSelf = aboutYourSelf;
        this.profession = profession;
    }
}
