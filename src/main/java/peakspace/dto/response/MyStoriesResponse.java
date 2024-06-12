package peakspace.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MyStoriesResponse(Long idStory,
                                String linkPublic,
                                LocalDate createdAt,
                                String text) {
}
