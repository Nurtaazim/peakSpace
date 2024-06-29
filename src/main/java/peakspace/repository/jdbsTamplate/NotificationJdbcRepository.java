package peakspace.repository.jdbsTamplate;

public interface NotificationJdbcRepository {

    void deleteNotificationByCommentId(Long postId);
}
