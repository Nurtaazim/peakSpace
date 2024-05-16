package peakspace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Country;

@Getter
@Setter
@Builder
public class EducationResponse {
    private Long id;
    private Country country;
    private String city;
    private String educationalInstitution;
}
