package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequest {
    private String avatar;
    private String cover;
    private String userName;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String aboutYourSelf;
    private String profession;
    private boolean workOrNot;
    private String location;
}
