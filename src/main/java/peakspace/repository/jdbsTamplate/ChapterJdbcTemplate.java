package peakspace.repository.jdbsTamplate;

public interface ChapterJdbcTemplate {
    boolean isFriendInOtherChapter(Long id, Long foundUserId, Long chapterId);

    boolean removeFriendFromChapter(Long id, Long foundUserId);

    void addFriendToChapter(Long id, Long foundUserId);

    void sendNotification(Long id, Long foundUserId);

}
