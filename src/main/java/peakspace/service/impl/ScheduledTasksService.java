package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.config.amazonS3.AwsS3Service;
import peakspace.entities.Story;
import peakspace.entities.User;
import peakspace.repository.StoryRepository;
import peakspace.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScheduledTasksService {
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final AwsS3Service storageService;

    //@Scheduled(cron = "0 0 0 * * ?")
    public void dailyCleanup() {
        List<Story> stories = storyRepository.findAll();
        stories.stream()
                .filter(story -> story.getCreatedAt() != null &&
                                 ZonedDateTime.now().isAfter(story.getCreatedAt().plusHours(24)))
                .forEach(story -> {
                    try {
                        story.getLinkPublications().forEach(linkPublication -> {
                            try {
                                storageService.deleteFile(linkPublication.getLink());
                            } catch (Exception e) {
                                log.error("Ошибка при удалении файла: " + linkPublication.getLink() + " - " + e.getMessage());
                            }
                        });
                        storyRepository.delete(story);
                    } catch (Exception e) {
                        log.error("Ошибка при удалении истории: " + story.getId() + " - " + e.getMessage());
                    }
                });
    }

    //@Scheduled(fixedRate = 180000)
    public void periodicUserCleanup() {
        List<User> users = userRepository.findAll();
        users.stream()
                .filter(user -> user.getCreatedAt() != null &&
                                ZonedDateTime.now().isAfter(user.getCreatedAt().plusMinutes(3)) &&
                                user.getBlockAccount())
                .forEach(user -> {
                    try {
                        userRepository.delete(user);
                    } catch (Exception e) {
                        System.err.println("Ошибка при удалении пользователя: " + user.getId() + " - " + e.getMessage());
                    }
                });
    }
}

