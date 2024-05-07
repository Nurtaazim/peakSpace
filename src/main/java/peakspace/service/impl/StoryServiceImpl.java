package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.config.amazonS3.StorageService;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.StoryResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Story;
import peakspace.entities.User;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;
import peakspace.repository.LinkPublicationRepository;
import peakspace.repository.StoryRepository;
import peakspace.repository.UserRepository;
import peakspace.service.StoryService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final LinkPublicationRepository linkPublicationRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public SimpleResponse create(StoryRequest storyRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        Story story = new Story();
        story.setCreatedAt(ZonedDateTime.now());
        story.setOwner(current);
        List<User> all = userRepository.findAll();
        List<User> tagFriends = new ArrayList<>();
        for (Long i : storyRequest.idsOfTaggedPeople()) {
            for (User user : all) {
                if (i.equals(user.getId())) tagFriends.add(user);
            }
        }
        story.setTagFriends(tagFriends);
        story.setLikes(new ArrayList<>());
        List<String> strings = storyRequest.photoUrlOrVideoUrl();
        List<Link_Publication> videosOrPhotosUrl = new ArrayList<>();
        for (String string : strings) {
            Link_Publication linkPublication = new Link_Publication();
            linkPublication.setLink(string);
            linkPublicationRepository.save(linkPublication);
            videosOrPhotosUrl.add(linkPublication);
        }
        story.setText(storyRequest.description());
        story.setLinkPublications(videosOrPhotosUrl);
        storyRepository.save(story);
        return SimpleResponse.builder()
                .message("Ваш сторис успешно добавлен!")
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse delete(long id) {
        Story byId = storyRepository.findById(id).orElseThrow(()->new NotFoundException("Сторис с такой id не найдено!"));
        if (byId.getOwner().getId().equals(userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId())){
            List<Link_Publication> linkPublications = byId.getLinkPublications();
            for (Link_Publication linkPublication : linkPublications) {
                storageService.deleteFile(linkPublication.getLink());
            }
            storyRepository.delete(byId);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Сторис успешно удалено!").build();
        }
        else throw new MessagingException("У вас нету прав удалить чужие сторисы!");

    }

    @Transactional
    @Override
    public List<StoryResponse> getAll(long userId) {
        List<StoryResponse> story = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с такой id не найдено!"));
        List<Story> stories = user.getStories();
        for (Story story1 : stories) {
            StoryResponse storyResponse = new StoryResponse();
            storyResponse.setText(story1.getText());
            storyResponse.setUserName(user.getThisUserName());
            storyResponse.setUserPhoto(user.getProfile().getAvatar());
            storyResponse.setCreatedAt(story1.getCreatedAt().toLocalDate());
            List<String> photoOrVideo = new ArrayList<>();
            List<Link_Publication> linkPublications = story1.getLinkPublications();
            for (Link_Publication linkPublication : linkPublications) {
                photoOrVideo.add(linkPublication.getLink());
            }
            storyResponse.setPhotosOrVideosLink(photoOrVideo);
            story.add(storyResponse);
        }
        return story;
    }
}
