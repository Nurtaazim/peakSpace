package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.NotificationResponse;
import peakspace.entities.Notification;
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
    public List<NotificationResponse> getAllNotifications() {
        List<Notification> notifications = userRepository.getCurrentUser().getNotifications();
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setMassage(notification.getNotificationMessage());
            notificationResponse.setCreatedAt(notification.getCreatedAt().toLocalDate());
            notificationResponse.setSenderUserId(notification.getSenderUserId());
            notificationResponse.setSenderProfileImageUrl(userRepository.findByIds(notification.getSenderUserId()).getProfile().getAvatar());
            if (!(notification.getPublication() == null)){
                notificationResponse.setPublicationOrStoryOrCommentId(notification.getPublication().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getPublication().getLinkPublications().getFirst().getLink());
            }
            if (notification.getStory() != null){
                notificationResponse.setPublicationOrStoryOrCommentId(notification.getStory().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getStory().getLinkPublications().getFirst().getLink());
            }
            if (!(notification.getComment() == null)){
                notificationResponse.setPublicationOrStoryOrCommentId(notification.getComment().getId());
                notificationResponse.setPublicationOrStoryImageUrl(notification.getPublication().getLinkPublications().getFirst().getLink());
            }
        }
        return notificationResponses;
    }
}
