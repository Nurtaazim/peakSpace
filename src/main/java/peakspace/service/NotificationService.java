package peakspace.service;

import peakspace.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAllNotifications();

}
