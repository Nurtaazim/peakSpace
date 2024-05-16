package peakspace.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import peakspace.enums.Tematica;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicProfileResponse {
    private Long publicId;
    private String cover;
    private String avatar;
    private String pablicName;
    private String descriptionPublic;
    @Enumerated(EnumType.STRING)
    private Tematica tematica;
    private int countFollower;
}
