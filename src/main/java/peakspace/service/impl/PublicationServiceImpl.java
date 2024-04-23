package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.LinkPublicationResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PublicationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Override
    public List<PublicationWithYouResponse> withPhoto(Long foundUserId) {
        User foundUser = userRepository.findByIds(foundUserId);
        List<PublicationWithYouResponse> publicationsWithYou = new ArrayList<>();

        if (foundUser != null) {
            for (Publication publication : foundUser.getPublications()) {
                if (publication.getTagFriends().stream().anyMatch(user -> user.getId().equals(getCurrentUser().getId()))) {
                    PublicationWithYouResponse publicationWithYouResponse = new PublicationWithYouResponse();
                    publicationWithYouResponse.setId(publication.getId());
                    publicationWithYouResponse.setDescription(publication.getDescription());
                    publicationWithYouResponse.setLocation(publication.getLocation());

                    List<Link_Publication> linkPublications = publication.getLinkPublications();
                    List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();

                    if (linkPublications != null && !linkPublications.isEmpty()) {
                        for (Link_Publication linkPublication : linkPublications) {
                            LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                            linkPublicationResponse.setId(linkPublication.getId());
                            linkPublicationResponse.setLink(linkPublication.getLink());
                            linkPublicationResponses.add(linkPublicationResponse);
                        }
                    }

                    publicationWithYouResponse.setLinks(linkPublicationResponses);
                    publicationsWithYou.add(publicationWithYouResponse);
                }
            }
        }

        return publicationsWithYou;
    }

        @Override
        public List<PublicationResponse> findAllPublic(Long friendId) {

                List<Publication> friendsPublic = userRepository.findFriendsPub(friendId);
                List<PublicationResponse> allPublications = new ArrayList<>();
                for (Publication publication : friendsPublic) {
                    PublicationResponse publicationResponse = new PublicationResponse();
                    publicationResponse.setId(publication.getId());

                    List<Link_Publication> linkPublications = publication.getLinkPublications();
                    List<LinkPublicationResponse> linkPublicationResponses = new ArrayList<>();

                    for (Link_Publication linkPublication : linkPublications) {
                        LinkPublicationResponse linkPublicationResponse = new LinkPublicationResponse();
                        linkPublicationResponse.setId(linkPublication.getId());
                        linkPublicationResponse.setLink(linkPublication.getLink());
                        linkPublicationResponses.add(linkPublicationResponse);
                    }
                    publicationResponse.setLinkPublications(linkPublicationResponses);
                    allPublications.add(publicationResponse);
                }
                return allPublications;
        }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
