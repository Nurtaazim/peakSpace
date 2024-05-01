package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlockAccountsResponse {
    private String userName;
    private String avatar;
    private String cover;
    private String aboutYourSelf;
    private String firstName;
    private String lastName;
}
