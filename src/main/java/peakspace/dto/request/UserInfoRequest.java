package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Country;

@Getter @Setter
public class UserInfoRequest {
    private String avatar;
    private String cover;
    private String userName;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String aboutYourSelf;
    private Country country;
    private String location;
    private String educationalInstitution;
    private String profession;
    private boolean workOrNot;
}
