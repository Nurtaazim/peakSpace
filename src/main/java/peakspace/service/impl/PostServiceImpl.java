package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
import java.util.List;

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
        List<Link_Publication> linkPublications = new ArrayList<>();
        for (int i = 0; i < postRequest.getLinks().size(); i++) {
        Link_Publication linkPublication = new Link_Publication();
        linkPublication.setLink(postRequest.getLinks().get(i));
        linkPublications.add(linkPublication);
        linkPublicationRepo.save(linkPublication);
        }
        Publication publication = new Publication();
        publication.setDescription(postRequest.getDescription());
        publication.setLocation(postRequest.getLocation());
        publication.setBlockComment(postRequest.isBlockComment());
        publication.setLinkPublications(linkPublications);
        publication.setBlockComment(postRequest.isBlockComment());
        publication.setOwner(user);

        publicationRepo.save(publication);
        user.getPublications().add(publication);
        userRepository.save(user);

        return  SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();
    }



}
