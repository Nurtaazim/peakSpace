package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.MyPostResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import java.util.List;
import java.util.stream.Collectors;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    default MyPostResponse getMyPost(Long postId){
        Publication publication = getReferenceById(postId);
        List<CommentResponse> commentForResponse = getCommentForResponse(publication.getId());
        commentForResponse.reversed();
        return MyPostResponse.builder()
                .id(publication.getId())
                .links(publication.getLinkPublications().stream().map(Link_Publication::getLink).collect(Collectors.toList()))
                .countLikes(publication.getLikes().size())
                .avatar(publication.getOwner().getProfile().getAvatar())
                .userName(publication.getOwner().getUsername())
                .location(publication.getLocation())
                .commentResponses(commentForResponse)
                .build();
    }

    @Query("select new peakspace.dto.response.CommentResponse(c.id, " +
           "c.user.profile.avatar, " +
           "c.user.userName, " +
           "c.message, " +
           "cout(cl), " +
           "c.createdAt) from Comment c inner join c.likes cl where c.publication.id = ?1")
    List<CommentResponse> getCommentForResponse(Long idPublic);

}
