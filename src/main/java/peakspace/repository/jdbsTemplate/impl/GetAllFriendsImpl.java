package peakspace.repository.jdbsTemplate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peakspace.dto.response.AllFriendsResponse;
import peakspace.entities.User;
import peakspace.repository.UserRepository;
import peakspace.repository.jdbsTemplate.GetAllFriends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAllFriendsImpl implements GetAllFriends {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Override
    public List<AllFriendsResponse> getAllFriendsById(Long userId, String userName) {
        User user = userRepository.getCurrentUser();
        List<AllFriendsResponse> allFriendsResponses = new ArrayList<>();

        String sql = "SELECT u.id, u.user_name, p.avatar, p.about_your_self, p.first_name, p.last_name, p.patronymic_name " +
                     "FROM users u " +
                     "JOIN profiles p ON u.id = p.user_id " +
                     "JOIN chapters_friends cf ON u.id = cf.friends_id " +
                     "JOIN chapters c ON cf.chapter_id = c.id " +
                     "WHERE c.user_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);
        if (userId.equals(user.getId())) {
            for (Map<String, Object> row : rows) {
                if (userName != null) {
                    String trim = userName.trim();
                    if (containsIgnoreCase((String) row.get("user_name"), trim) ||
                        containsIgnoreCase((String) row.get("first_name"), trim) ||
                        containsIgnoreCase((String) row.get("last_name"), trim) ||
                        containsIgnoreCase((String) row.get("patronymic_name"), trim)) {

                        allFriendsResponses.add(buildAllFriendsResponse(row, true));
                    }
                } else {
                    allFriendsResponses.add(buildAllFriendsResponse(row, true));
                }
            }
        } else {
            for (Map<String, Object> row : rows) {
                Long friendId = (Long) row.get("id");
                if (userName != null) {
                    String trim = userName.trim();
                    if (containsIgnoreCase((String) row.get("user_name"), trim) ||
                        containsIgnoreCase((String) row.get("first_name"), trim) ||
                        containsIgnoreCase((String) row.get("last_name"), trim) ||
                        containsIgnoreCase((String) row.get("patronymic_name"), trim)) {

                        if (!user.getBlockAccounts().contains(friendId)) {
                            allFriendsResponses.add(buildAllFriendsResponse(row, isFriend(user, friendId)));
                        }
                    }
                } else {
                    if (!user.getBlockAccounts().contains(friendId)) {
                        allFriendsResponses.add(buildAllFriendsResponse(row, isFriend(user, friendId)));
                    }
                }
            }
        }
        return allFriendsResponses;
    }

    private boolean containsIgnoreCase(String str, String searchStr) {
        return str != null && searchStr != null && str.toLowerCase().contains(searchStr.toLowerCase());
    }

    private boolean isFriend(User user, Long friendId) {
        String sql = "SELECT COUNT(*) FROM chapters_friends cf " +
                     "JOIN chapters c ON cf.chapter_id = c.id " +
                     "WHERE c.user_id = ? AND cf.friends_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{user.getId(), friendId}, Integer.class);
        return count != null && count > 0;
    }

    private AllFriendsResponse buildAllFriendsResponse(Map<String, Object> row, boolean isMyFriend) {
        return AllFriendsResponse.builder()
                .idUser((Long) row.get("id"))
                .avatar((String) row.get("avatar"))
                .userName((String) row.get("user_name"))
                .aboutMe((String) row.get("about_your_self"))
                .isMyFriend(isMyFriend)
                .build();
    }

}
