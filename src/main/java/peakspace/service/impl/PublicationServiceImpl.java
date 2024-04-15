package peakspace.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.repository.PublicationRepository;
import peakspace.service.PublicationService;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;

    @Override
    public GetAllPostsResponse getAllPosts(Principal principal) {
        return null;
    }
}
