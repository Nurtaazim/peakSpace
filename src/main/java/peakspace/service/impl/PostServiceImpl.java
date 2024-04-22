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
    public PostResponse getById(Long postId,Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("With this id user not found!"));
        user.getProfile().getId();
        user.getProfile().getAvatar();
        for (Publication publication : user.getPublications()) {
            if(publication.getId().equals(postId)){
                List<Link_Publication> linkPublications = publication.getLinkPublications();
                List<Comment> comments = publication.getComments();
                List<CommentResponse> commentResponses = mapToComment(comments);
                List<LinkPublicationResponse> linkResponses = mapToLinkResponse(linkPublications);
                return mapToPost(publication, linkResponses, commentResponses);
            }
        }
//        Publication publication = publicationRepo.findById(postId).orElseThrow(() -> new RuntimeException("With this id not found!"));
           return  null;
    }

    public PostResponse mapToPost(Publication post, List<LinkPublicationResponse> linkPublicationResponses, List<CommentResponse> commentResponses) {
        return new PostResponse(
                post.getId(),
                post.getDescription(),
                post.getLocation(),
                post.isBlockComment(),
                linkPublicationResponses,
                commentResponses
        );
    }

    public List<LinkPublicationResponse> mapToLinkResponse(List<Link_Publication> links) {
        List<LinkPublicationResponse> linkPublicationRes = new ArrayList<>();
        for (Link_Publication link : links) {
            LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse(
                    link.getId(),
                    link.getLink()
            );
            linkPublicationRes.add(linkPublicationResponse);
        }
        return linkPublicationRes;
    }

    public List<CommentResponse> mapToComment(List<Comment> commentList) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponse commentResponse = new CommentResponse(
                    comment.getId(),
                    comment.getMessage(),
                    comment.getCreatedAt()
            );
            commentResponses.add(commentResponse);
        }
        return commentResponses;
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
//        Publication publication = publicationRepo.findById(postId).orElseThrow(() -> new RuntimeException("With this id not found!"));

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

    @Override
    public PostResponse getAll(Long userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        if(user.getId().equals(userId)){
            user.getPublications();
        }
        return null;
    }


}
