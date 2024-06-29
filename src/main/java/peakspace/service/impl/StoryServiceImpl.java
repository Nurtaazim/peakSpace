package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.config.amazonS3.AwsS3Service;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MyStoriesResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryAllHomPageResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Notification;
import peakspace.entities.Story;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;
import peakspace.repository.LinkPublicationRepo;
import peakspace.repository.NotificationRepository;
import peakspace.repository.StoryRepository;
import peakspace.repository.UserRepository;
import peakspace.repository.jdbsTemplate.StoryJdbcRepository;
import peakspace.service.StoryService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final LinkPublicationRepo linkPublicationRepository;
    private final AwsS3Service storageService;
    private final StoryJdbcRepository storyJdbcTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public SimpleResponse create(StoryRequest storyRequest) {
        User current = userRepository.getCurrentUser();
        Story story = new Story();
        story.setCreatedAt(ZonedDateTime.now());
        story.setOwner(current);
        story.setLikes(new ArrayList<>());
        List<String> strings = storyRequest.photoUrlOrVideoUrl();
        Link_Publication linkPublication = new Link_Publication();
        linkPublication.setLink(strings.getFirst());
        linkPublicationRepository.save(linkPublication);
        story.setText(storyRequest.description());
        story.setLinkPublications(new ArrayList<>(List.of(linkPublication)));
        storyRepository.save(story);
        return SimpleResponse.builder()
                .message("Ваш сторис успешно добавлен!")
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long id) {
        Story byId = storyRepository.findById(id).orElseThrow(() -> new NotFoundException("Сторис с такой id не найдено!"));
        if (byId.getOwner().getId().equals(userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId())) {
            List<Link_Publication> linkPublications = byId.getLinkPublications();
            for (Link_Publication linkPublication : linkPublications) {
                storageService.deleteFile(linkPublication.getLink());
            }
            notificationRepository.deleteAllByStoryId(id);
            storyRepository.delete(byId);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Сторис успешно удалено!").build();
        } else throw new MessagingException("У вас нету прав удалить чужие сторисы!");
    }

    @Transactional
    @Override
    public List<StoryResponse> getAll(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с такой id не найден!")
        );

        return storyJdbcTemplate.getAllStoriesByUserId(user.getId());
    }

    @Override
    public List<StoryAllHomPageResponse> getAllFriendsStory() {
        return storyJdbcTemplate.getAllFriendsStory(getCurrentUser().getId());
    }

    @Override
    public List<MyStoriesResponse> getMyStories() {
        return storyJdbcTemplate.getMyStories(getCurrentUser());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
