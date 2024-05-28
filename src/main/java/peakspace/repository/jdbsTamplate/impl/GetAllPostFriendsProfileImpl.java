package peakspace.repository.jdbsTamplate.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.LinkPublicationResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.entities.PablicProfile;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.enums.Tematica;
import peakspace.exception.AccountIsBlock;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.jdbsTamplate.GetAllPostFriendsProfile;
import peakspace.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAllPostFriendsProfileImpl implements GetAllPostFriendsProfile {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;



    @Transactional
    public List<PublicationResponse> findAllPublic(Long friendId) {
        User friend = userRepository.getReferenceById(friendId);

        List<PublicationResponse> allPublications = new ArrayList<>();

        jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[]{friendId}, (rs, rowNum) -> {
            boolean isBlock = rs.getBoolean("is_block");
            if (isBlock && !thisUserMyFriend(friend)) {
                throw new AccountIsBlock("Закрытый аккаунт!");
            }

            jdbcTemplate.query("SELECT id, pablic_profile_id FROM publications where owner_id = ?", new Object[]{friendId}, (rsPub, rowNumPub) -> {
                boolean isPublicProfile = rsPub.getBoolean("pablic_profile_id");
                if (isPublicProfile) {
                    return null; // Skip this publication
                }

                PublicationResponse publicationResponse = new PublicationResponse();
                publicationResponse.setId(rsPub.getLong("id"));

                List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();
                jdbcTemplate.query("SELECT id, link FROM publications_link_publications WHERE publication_id = ?", new Object[]{rsPub.getLong("id")}, (rsLink, rowNumLink) -> {
                    LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                    linkPublicationResponse.setId(rsLink.getLong("id"));
                    linkPublicationResponse.setLink(rsLink.getString("link"));
                    linkPublicationResponses.add(linkPublicationResponse);
                    return null;
                });

                publicationResponse.setLinkPublications(linkPublicationResponses);
                allPublications.add(publicationResponse);
                return null;
            });

            return allPublications;
        });

    }
    private boolean thisUserMyFriend(User user) {
        Long currentUserId = getCurrentUser().getId();
        String sql = "SELECT COUNT(*) FROM chapters c " +
                "JOIN chapters_friends cf ON c.id = cf.chapter_id " +
                "WHERE c.user_id = ? AND cf.friends_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{user.getId(), currentUserId}, Integer.class);
        return count != null && count > 0;
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
