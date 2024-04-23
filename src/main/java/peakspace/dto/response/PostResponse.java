package peakspace.dto.response;

import peakspace.entities.Link_Publication;

import java.util.List;


public record PostResponse(Long id,
                           String description,
                           String location,
                           boolean isBlockComment,
                           List <LinkPublicationResponse> linkPublication,
                           List <CommentResponse> comments
){}

