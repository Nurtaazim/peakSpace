package peakspace.repository.jdbsTamplate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peakspace.dto.response.FriendsResponse;
import peakspace.repository.jdbsTamplate.SearchFriends;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchFriendsImpl implements SearchFriends {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FriendsResponse> getAllFriendsWithJDBCTemplate(Long userId, Long chapterId, String search) {
        String str = "";
        List<Object> params = new ArrayList<>();

        if (search != null) {
            str = " AND u.user_name LIKE CONCAT('%', ?::text, '%') OR CONCAT(up.first_name, up.last_name, up.patronymic_name) LIKE CONCAT('%', ?::text, '%'))";
            params.add(search);
            params.add(search);
        }

        String sql = """
                             SELECT distinct u.id,
                             up.avatar,
                             u.user_name,
                             up.about_your_self
                             FROM chapters_friends chf
                             INNER JOIN users u ON u.id = chf.friends_id
                             INNER JOIN profiles up ON up.user_id = u.id
                             WHERE chf.chapter_id = ?
                             """ + str;

        params.add(chapterId);

        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> FriendsResponse.builder()
                .userId(rs.getLong("id"))
                .avatar(rs.getString("avatar"))
                .userName(rs.getString("user_name"))
                .aboutMe(rs.getString("about_your_self"))
                .build());
    }


}
