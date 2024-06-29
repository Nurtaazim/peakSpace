package peakspace.repository.jdbsTamplate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.repository.jdbsTamplate.RemovePublicationByCommentId;
@Service
@RequiredArgsConstructor
@Slf4j
public class RemovePublicationByCommentIdImpl implements RemovePublicationByCommentId {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void deleteNotificationByCommentId(Long postId) {
        try {
            jdbcTemplate.update("DELETE FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?", postId);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }
}
