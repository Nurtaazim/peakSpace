package peakspace.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.entities.Link_Publication;
import peakspace.repository.CommentRepository;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PublicationService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public GetAllPostsResponse getAllPosts(Principal principal) {
        User user = userRepository.getByEmail(principal.getName());
        Map<Long, String> publics = user.getPublications().stream()
                .collect(Collectors.toMap(
                        Publication::getId,
                        publication -> publication.getLinkPublications().getFirst().getLink()
                ));
        return GetAllPostsResponse.builder()
                .cover(user.getProfile().getCover())
                .avatar(user.getProfile().getAvatar())
                .userName(user.getUsername())
                .aboutMe(user.getProfile().getAboutYourSelf())
                .major(user.getProfile().getProfession())
                .countFriends(user.getChapters().size())
                .countPablics(user.getPablicProfiles().size())
                .publications(publics)
                .build();
    }

    @Override
    public MyPostResponse getById(Long postId) {
        MyPostResponse myPost = getMyPost(postId);
        for (CommentResponse commentResponse : myPost.commentResponses()) {
            commentResponse.setInnerComments(commentRepository.getInnerComments(commentResponse.getId()));
        }
        return myPost;
    }

    public MyPostResponse getMyPost(Long postId){
        Publication publication = publicationRepository.getReferenceById(postId);
        List<CommentResponse> commentForResponse = commentRepository.getCommentForResponse(publication.getId());
        commentForResponse.reversed();
        return MyPostResponse.builder()
                .id(publication.getId())
                .userId(publication.getOwner().getId())
                .links(publication.getLinkPublications().stream().map(Link_Publication::getLink).collect(Collectors.toList()))
                .countLikes(publication.getLikes().size())
                .avatar(publication.getOwner().getProfile().getAvatar())
                .userName(publication.getOwner().getThisUserName())
                .location(publication.getLocation())
                .commentResponses(commentForResponse)
                .build();
    }

}
