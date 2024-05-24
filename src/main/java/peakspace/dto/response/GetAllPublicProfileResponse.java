package peakspace.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import peakspace.enums.Tematica;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllPublicProfileResponse {

    private String avatar;
    private String pablicName;
    @Enumerated(EnumType.STRING)
    private Tematica tematica;
    private String owner;
}
