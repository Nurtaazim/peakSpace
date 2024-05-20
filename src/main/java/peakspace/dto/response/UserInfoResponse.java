package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Country;

import java.util.List;

@Getter
@Setter
@Builder
public class UserInfoResponse {
    private String avatar;
    private String cover;
    private String userName;
    private String fullName;
    private String aboutYourSelf;
    private List<EducationResponse> educationResponses;
    private String profession;
    private boolean workOrNot;
}
