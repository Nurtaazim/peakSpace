package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
           "c.createdAt) from Comment c where c.publication.id = :idPublic order by c.id desc ")
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

    @Modifying
    @Transactional
    @Query(value = "delete from comments_notifications where comment_id = :innerCommentId", nativeQuery = true)
    void deleteNotificationComment(Long innerCommentId);

    @Modifying
    @Transactional
    @Query(value = "select distinct c.* " +
                   "from comments c " +
                   "where not exists (" +
                   "    select 1 " +
                   "    from inner_comment ic " +
                   "    where ic.inner_comments_id = c.id" +
                   ") " +
                   "and c.publication_id = :postId", nativeQuery = true)
    List<Comment> getAllCommentById(Long postId);
    @Query(value = "SELECT EXISTS(SELECT 1 FROM comments_likes WHERE comment_id = :commentId AND likes_id = :likeId)", nativeQuery = true)
    boolean isLike(@Param("commentId") Long commentId, @Param("likeId") Long likeId);
}
