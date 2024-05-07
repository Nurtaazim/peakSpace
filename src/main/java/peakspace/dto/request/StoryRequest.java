package peakspace.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record StoryRequest(
        List <String> photoUrlOrVideoUrl,
        String description,
        List <Long> idsOfTaggedPeople
) {

}
