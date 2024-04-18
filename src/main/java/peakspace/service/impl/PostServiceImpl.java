package peakspace.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import peakspace.dto.request.PostRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.repository.LinkPublicationRepo;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PostService;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final LinkPublicationRepo linkPublicationRepo;
    private final PublicationRepository publicationRepo;

    @Override
    public SimpleResponse savePost(PostRequest postRequest) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.getByEmail(email);

        Link_Publication linkPublication = new Link_Publication();
        linkPublication.setLink(postRequest.getLink());
        linkPublicationRepo.save(linkPublication);

        Publication publication = new Publication();
        publication.setDescription(postRequest.getDescription());
        publication.setLocation(postRequest.getLocation());
        publication.getLinkPublications().add(linkPublication);

        Publication publication1 = publicationRepo.save(publication);

        user.getPublications().add(publication1);
        userRepository.save(user);

        return null;
    }



}
