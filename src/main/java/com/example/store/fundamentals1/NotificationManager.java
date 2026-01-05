package com.example.store.fundamentals1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationManager {
    private final NotificationService notificationService;

    public NotificationManager(@Qualifier("email") NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(String message, String recipient) {
        this.notificationService.send(message, recipient);
    }
}
