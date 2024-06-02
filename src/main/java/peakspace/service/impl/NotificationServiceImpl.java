package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.NotificationResponse;
import peakspace.entities.Notification;
import peakspace.entities.User;
import peakspace.repository.NotificationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public List<NotificationResponse> getAllNotifications() {
        List<Notification> notifications = userRepository.getCurrentUser().getNotifications();
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        notifications.sort((n1, n2) -> n2.getId().compareTo(n1.getId()));

        for (Notification notification : notifications) {
            User user = userRepository.getReferenceById(notification.getSenderUserId());
            NotificationResponse notificationResponse = new NotificationResponse();

            notificationResponse.setMessage(notification.getNotificationMessage());
            notificationResponse.setCreatedAt(notification.getCreatedAt().toLocalDate());
            notificationResponse.setSenderUserId(notification.getSenderUserId());
            notificationResponse.setSenderUserName(user.getThisUserName());
            notificationResponse.setSenderProfileImageUrl(user.getProfile().getAvatar());

            if (notification.getPublication() != null && notification.getLike() != null) {
                notificationResponse.setPublicationId(notification.getPublication().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getPublication().getLinkPublications().getFirst().getLink());
            } else if (notification.getStory() != null && notification.getLike() != null) {
                notificationResponse.setStoryId(notification.getStory().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getStory().getLinkPublications().getFirst().getLink());
            } else if (notification.getComment() != null && notification.getLike() == null) {
                notificationResponse.setCommentId(notification.getComment().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getComment().getPublication().getLinkPublications().getFirst().getLink());
            } else if (notification.getComment() != null) {
                notificationResponse.setCommentId(notification.getComment().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getComment().getPublication().getLinkPublications().getFirst().getLink());
            }
            notificationResponse.setSeen(notification.isSeen());
            notificationResponses.add(notificationResponse);
            notification.setSeen(true);
        }

        return notificationResponses;
    }
}
