package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Studies;

@Getter @Setter
public class UserInfoRequest {
    private String avatar;
    private String cover;
    private String userName;
    private String firstName;
    private String lastName;
    private String aboutYourSelf;
    private Studies avgAndHigher;
    private String city;
    private String educationalInstitution;
    private String profession;
    private boolean workOrNot;
}
