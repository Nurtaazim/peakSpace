package peakspace.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String cover;
    private String avatar;
    private String profession;


}
