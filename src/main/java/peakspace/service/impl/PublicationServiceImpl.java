package peakspace.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PublicationService;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Override
    public GetAllPostsResponse getAllPosts(Principal principal) {
        User user = userRepository.getByEmail(principal.getName());
        Map<Long, String> publics = user.getPublications().stream()
                .collect(Collectors.toMap(
                        Publication::getId, // ключ - ID публикации
                        publication -> publication.getLinkPublications().getFirst().getLink() // значение - первый элемент списка ссылок публикации
                ));
        return GetAllPostsResponse.builder()
                .cover(user.getProfile().getCover())
                .avatar(user.getProfile().getAvatar())
                .userName(user.getUsername())
                .aboutMe(user.getProfile().getAboutYourSelf())
                .major(user.getProfile().getProfession())
                .countFriends(user.getChapters().size())
                .countPublics(user.getPublications().size())
                .publications(publics)
                .build();
    }

    @Override
    public GetAllPostsResponse getById(Long postId, Principal principal) {
        return null;
    }

}
