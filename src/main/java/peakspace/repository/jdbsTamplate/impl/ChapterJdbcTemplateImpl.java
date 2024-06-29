package peakspace.repository.jdbsTamplate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peakspace.repository.jdbsTamplate.ChapterJdbcTemplate;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ChapterJdbcTemplateImpl implements ChapterJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isFriendInOtherChapter(Long userId, Long foundUserId, Long chapterId) {
        String sql = "SELECT COUNT(*) FROM chapters_friends cf " +
                     "JOIN chapters c ON cf.chapter_id = c.id " +
                     "WHERE c.user_id = ? AND cf.friends_id = ? AND c.id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, foundUserId, chapterId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean removeFriendFromChapter(Long chapterId, Long foundUserId) {
        String sql = "DELETE FROM chapters_friends WHERE chapter_id = ? AND friends_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, chapterId, foundUserId);
        return rowsAffected > 0;
    }

    @Override
    public void addFriendToChapter(Long chapterId, Long foundUserId) {
        String sql = "INSERT INTO chapters_friends (chapter_id, friends_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, chapterId, foundUserId);
    }

    @Override
    public void sendNotification(Long senderUserId, Long foundUserId) {
        String sql = "INSERT INTO notifications (notification_message, user_notification_id, seen, created_at, sender_user_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Timestamp createdAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Bishkek")).toInstant());
        jdbcTemplate.update(sql, "Подписался!", foundUserId, false, createdAt, senderUserId);
    }

}
