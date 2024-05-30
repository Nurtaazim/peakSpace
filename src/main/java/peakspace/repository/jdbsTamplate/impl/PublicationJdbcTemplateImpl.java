package peakspace.repository.jdbsTamplate.impl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.LinkPublicationResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.AccountIsBlock;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.jdbsTamplate.PublicationJdbcTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PublicationJdbcTemplateImpl implements PublicationJdbcTemplate {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;

    @Transactional
    public List<PublicationResponse> findAllPublic(Long friendId) {
        User referenceById = userRepository.getReferenceById(friendId);
        if (referenceById.getIsBlock() && !thisUserMyFriend(referenceById)){
            throw new AccountIsBlock("Закрытый аккаунт!");
        }

        String sql = "SELECT p.id AS publication_id, lp.id AS link_id, lp.link " +
                "FROM publications p " +
                "LEFT JOIN publications_link_publications plp ON p.id = plp.publication_id " +
                "LEFT JOIN link_publications lp ON plp.link_publications_id = lp.id " +
                "WHERE p.owner_id = ?";

        Map<Long, PublicationResponse> publicationMap = new HashMap<>();

        jdbcTemplate.query(sql, new Object[]{friendId}, (rs, rowNum) -> {
            Long publicationId = rs.getLong("publication_id");
            Long linkId = rs.getLong("link_id");
            String link = rs.getString("link");

            Publication publication = publicationRepository.findById(publicationId).orElse(null);
            if (publication != null && publication.getPablicProfile() == null) {
                PublicationResponse publicationResponse = publicationMap.get(publicationId);
                if (publicationResponse == null) {
                    publicationResponse = new PublicationResponse();
                    publicationResponse.setId(publicationId);
                    publicationResponse.setLinkPublications(new ArrayList<>());
                    publicationMap.put(publicationId, publicationResponse);
                }

                LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                linkPublicationResponse.setId(linkId);
                linkPublicationResponse.setLink(link);

                publicationResponse.getLinkPublications().add(linkPublicationResponse);
            }

            return null;
        });

        return new ArrayList<>(publicationMap.values());
    }

    @Override
    public List<PublicationWithYouResponse> withPhoto(Long foundUserId) {
        User foundUser = userRepository.findByIds(foundUserId);

        if (foundUser.getIsBlock() && !thisUserMyFriend(foundUser)) {
            throw new AccountIsBlock("Закрытый аккаунт!");
        }

        List<Long> myAcceptPost = foundUser.getMyAcceptPost();
        List<PublicationWithYouResponse> publicationsWithYou = new ArrayList<>();

        for (Long postId : myAcceptPost) {
            String sql = "SELECT p.id AS publication_id, p.description, p.location, lp.id AS link_id, lp.link " +
                    "FROM publications p " +
                    "LEFT JOIN publications_link_publications plp ON p.id = plp.publication_id " +
                    "LEFT JOIN link_publications lp ON plp.link_publications_id = lp.id " +
                    "WHERE p.id = ?";

            PublicationWithYouResponse publicationWithYouResponse = jdbcTemplate.query(sql, new Object[]{postId}, rs -> {
                PublicationWithYouResponse result = null;
                List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();
                while (rs.next()) {
                    if (result == null) {
                        result = new PublicationWithYouResponse();
                        result.setId(rs.getLong("publication_id"));
                        result.setDescription(rs.getString("description"));
                        result.setLocation(rs.getString("location"));
                    }
                    LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                    linkPublicationResponse.setId(rs.getLong("link_id"));
                    linkPublicationResponse.setLink(rs.getString("link"));
                    linkPublicationResponses.add(linkPublicationResponse);
                }
                result.setLinks(linkPublicationResponses);
                return result;
            });

            if (publicationWithYouResponse != null) {
                publicationsWithYou.add(publicationWithYouResponse);
            }
        }
        return publicationsWithYou;
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