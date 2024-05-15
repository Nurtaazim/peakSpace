package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Country;

@Getter
@Setter
public class AddEducationRequest {
    private Country country;
    private String educationalInstitution;
}
