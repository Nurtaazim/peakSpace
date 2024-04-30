package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.request.StoryRequest;
import peakspace.dto.response.MessageResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.Story;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import peakspace.repository.StoryRepository;
import peakspace.repository.UserRepository;
import peakspace.service.StoryService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse create(StoryRequest storyRequest, List<Long> id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        Story story = new Story();
        story.setCreatedAt(ZonedDateTime.now());
        story.setOwner(current);
        List<User> all = userRepository.findAll();
        List<User> tagFriends = new ArrayList<>();
        for (Long i : id) {
            for (User user : all) {
                if (i.equals(user.getId())) tagFriends.add(user);
            }
        }
        story.setTagFiends(tagFriends);
        story.setLikes(new ArrayList<>());

        storyRepository.save(story);
        return SimpleResponse.builder()
                .message("Story success added!")
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse delete(long id) {
        Story byId = storyRepository.findById(id).orElseThrow(()->new NotFoundException("Story with this id not found!"));
        storyRepository.delete(byId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Story successfully deleted!").build();
    }
}
