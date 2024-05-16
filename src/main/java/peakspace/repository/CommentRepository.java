package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import peakspace.dto.response.CommentInnerResponse;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.InnerCommentResponse;
import peakspace.entities.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new peakspace.dto.response.InnerCommentResponse(" +
           "ic.id, " +
           "ic.user.id," +
           "ic.user.profile.avatar, " +
           "ic.user.userName, " +
           "ic.message, " +
           "(select count(icl.id) from ic.likes icl), " +
           "ic.createdAt) from Comment c " +
           "inner join c.innerComments ic " +
           "where c.id = ?1")
    List<InnerCommentResponse> getInnerComments(Long id);

    @Query("select new peakspace.dto.response.CommentResponse(" +
           "c.id, " +
           "c.user.id," +
           "c.user.profile.avatar, " +
           "c.user.userName, " +
           "c.message, " +
           "(select count(lik.user) from c.likes lik), " +
           "c.createdAt) from Comment c where c.publication.id = ?1")
    List<CommentResponse> getCommentForResponse(Long idPublic);

    @Query("select new peakspace.dto.response.CommentInnerResponse(c.id,c.user.id,c.user.profile.avatar,c.user.userName,c.message,(select count(l.id) from c.likes l),c.createdAt) from Comment c where c.id =:commentId")
    CommentInnerResponse getCommentResponseInner(Long commentId);

    @Modifying
    @Transactional
    @Query(value = "delete from inner_comment where inner_comments_id =:innerCommentId",nativeQuery = true)
    void deleteInnerComment(Long innerCommentId);

    @Modifying
    @Transactional
    @Query(value = "delete from comments_likes where comment_id =:innerCommentId",nativeQuery = true)
    void deleteLikes(Long innerCommentId);

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.id =:commentId")
    void deleteByIds(Long commentId);

    @Modifying
    @Transactional
    @Query("delete from Notification n where n.comment.id =:commentId")
    void deleteNotification(Long commentId);

}