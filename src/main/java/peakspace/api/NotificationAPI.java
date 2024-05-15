package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.NotificationResponse;
import peakspace.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationAPI {

    private final NotificationService notificationService;

    @GetMapping
    List<NotificationResponse> getNotifications(){
       return notificationService.getAllNotifications();
    }

}
