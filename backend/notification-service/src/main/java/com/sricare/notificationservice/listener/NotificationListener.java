package com.sricare.notificationservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "notifications")
    public void handleNotification(String message) {
        System.out.println("📧 Notification received: " + message);
        // In a real system, send email/SMS/push notification
        // For now, just log the notification
    }
}
