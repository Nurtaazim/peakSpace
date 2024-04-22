package peakspace.dto.response;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import peakspace.enums.Choise;

@Builder
public record SearchResponse(
        Long id,
        String name,
        String avatar,
        String description
) {
    public SearchResponse(Long id, String name, String avatar) {
        this(id, name, avatar, "");
    }
}
