package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.LinkPublicationResponse;
import peakspace.dto.response.PostResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Comment;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import peakspace.repository.LinkPublicationRepo;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PostService;

import java.time.ZonedDateTime;
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

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse update(Long postId, PostUpdateRequest postUpdateRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
                if(publication.getId().equals(postId)) {
                    publication.setDescription(postUpdateRequest.getDescription());
                    publication.setLocation(postUpdateRequest.getLocation());
                    publication.setBlockComment(postUpdateRequest.isBlockComment());
                    publication.setUpdatedAt(ZonedDateTime.now());
                    break;
                }
        }
        return SimpleResponse.builder()
                .message("Successfully updated!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public SimpleResponse delete(Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if (publication.getOwner().getId().equals(user.getId())) {
                if(publication.getId().equals(postId)) {
                    publicationRepo.deleteCom(postId);
                    publicationRepo.deleteLink(postId);
                    publicationRepo.deleteTag(postId);
                    publicationRepo.deleteLike(postId);
                    publicationRepo.delete(publication);
                }
            }
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully deleted!")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse deleteLinkFromPost(Long linkId, Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        for (Publication publication : user.getPublications()) {
            if(publication.getId().equals(postId)){
                if (publication.getOwner().getId().equals(user.getId())) {
                    List<Link_Publication> photots = publication.getLinkPublications();
                    List<Link_Publication> orig = new ArrayList<>();
                    for (Link_Publication phot : photots) {
                        if (!phot.getId().equals(linkId)) {
                            orig.add(phot);
                        }
                    }
                    publication.setLinkPublications(orig);
                    linkPublicationRepo.deleteById(linkId);
                }
            }
        }
        return SimpleResponse.builder()
                .message("Successfully deleted!")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
