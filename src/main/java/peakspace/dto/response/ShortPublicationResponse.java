package peakspace.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortPublicationResponse {
    private Long id;
    private Long ownerId;
    private String link;
}
