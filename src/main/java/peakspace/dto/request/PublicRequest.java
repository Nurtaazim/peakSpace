package peakspace.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Tematica;

@Setter
@Getter
@AllArgsConstructor
public class PublicRequest {

    private String cover;
    private String avatar;
    private String pablicName;
    private String descriptionPublic;
    @Enumerated(EnumType.STRING)
    private Tematica tematica;
}
