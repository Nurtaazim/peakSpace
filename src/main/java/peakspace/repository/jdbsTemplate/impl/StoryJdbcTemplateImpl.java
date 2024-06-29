package peakspace.repository.jdbsTemplate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.repository.StoryRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.jdbsTemplate.StoryJdbcTemplate;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoryJdbcTemplateImpl implements StoryJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<StoryAllHomPageResponse> getAllFriendsStory(Long currentUserId) {

        Timestamp cutoffTime = Timestamp.from(ZonedDateTime.now().minus(24, ChronoUnit.HOURS).toInstant());

        String sql = """
                    SELECT s.id, u.id AS userId, u.user_name AS userName, p.avatar, s.created_at AS createdAt
                    FROM stories s
                    JOIN users u ON s.owner_id = u.id
                    LEFT JOIN profiles p ON p.user_id = u.id
                    WHERE s.created_at > ?
                    AND s.owner_id IN (
                        SELECT cf.friends_id
                        FROM chapters_friends cf
                        JOIN chapters c ON cf.chapter_id = c.id
                        WHERE c.user_id = ?
                    )
                    ORDER BY s.created_at DESC
                """;

        Map<Long, StoryAllHomPageResponse> uniqueUsers = new LinkedHashMap<>();

        jdbcTemplate.query(sql, new Object[]{cutoffTime, currentUserId}, (rs, rowNum) -> {
            Long userId = rs.getLong("userId");
            String avatar = rs.getString("avatar");
            String userName = rs.getString("userName");

            StoryAllHomPageResponse response = new StoryAllHomPageResponse(userId, avatar, userName);
            uniqueUsers.put(userId, response);
            return null;
        });

        return new ArrayList<>(uniqueUsers.values());
    }

    @Override
    public List<MyStoriesResponse> getMyStories(User user) {
        Timestamp cutoffTime = Timestamp.from(ZonedDateTime.now().minus(24, ChronoUnit.HOURS).toInstant());
        List<MyStoriesResponse> myStoriesResponses = new ArrayList<>();
        String sql = """
                    SELECT s.id, lp.link, s.created_at, s.text
                    FROM stories s
                    JOIN stories_link_publications slp on slp.story_id = s.id
                    JOIN link_publications lp on lp.id = slp.link_publications_id
                    WHERE s.created_at > ?
                    AND s.owner_id = ?
                    ORDER BY s.created_at DESC
                """;

        jdbcTemplate.query(sql, new Object[]{cutoffTime, user.getId()}, (rs, rowNum) -> {
            myStoriesResponses.add(MyStoriesResponse.builder()
                    .idStory(rs.getLong(1))
                    .linkPublic(rs.getString(2))
                    .createdAt(rs.getTimestamp(3).toLocalDateTime().toLocalDate())
                    .text(rs.getString(4))
                    .build());
            return null;
        });

        return myStoriesResponses;
    }

    @Override
    public List<StoryResponse> getAllStoriesByUserId(Long userId) {
        return jdbcTemplate.query("""
                        select s.id,
                               p.avatar,
                               u.user_name,
                               s.text,
                               s.created_at
                        from stories s
                        inner join users u on u.id = s.owner_id
                        inner join profiles p on p.user_id = u.id
                        where s.owner_id = ?
                        """,
                ps -> ps.setLong(1, userId),
                (rs, rowNum) -> StoryResponse.builder()
                        .photosOrVideosLink(getPhotoOrVideoLink(rs.getLong(1)))
                        .userPhoto(rs.getString(2))
                        .userName(rs.getString(3))
                        .text(rs.getString(4))
                        .createdAt(rs.getTimestamp(5).toLocalDateTime().toLocalDate())
                        .build()
        );
    }

    private List<String> getPhotoOrVideoLink(Long storyId) {
        return jdbcTemplate.query("""
                        select lp.link
                        from link_publications lp
                        join stories_link_publications slp on slp.link_publications_id = lp.id
                        where slp.story_id = ?
                        """,
                ps -> ps.setLong(1, storyId),
                (rs, rowNum) -> rs.getString(1)
        );
    }
}
