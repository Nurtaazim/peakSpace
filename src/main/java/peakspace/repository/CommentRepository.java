package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peakspace.dto.response.InnerCommentResponse;
import peakspace.entities.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new peakspace.dto.response.InnerCommentResponse(" +
           "ic.id, " +
           "ic.user.profile.avatar, " +
           "ic.user.userName, " +
           "ic.message, " +
           "(select count(icl.id) from ic.likes icl), " +
           "ic.createdAt) from Comment c " +
           "inner join c.innerComments ic " +
           "where c.id = ?1")
    List<InnerCommentResponse> getInnerComments(Long id);


}