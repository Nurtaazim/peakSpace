package peakspace.dto.response;

import lombok.Builder;

@Builder
public record BlockAccountsResponse(String userName,
                                    String avatar,
                                    String cover,
                                    String aboutYourSelf,
                                    String firstName,
                                    String lastName
){
}
