package peakspace.dto.response;

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
