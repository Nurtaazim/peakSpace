package peakspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;
    private String avatar;
    private String userName;
    private String aboutYourSelf;

}
