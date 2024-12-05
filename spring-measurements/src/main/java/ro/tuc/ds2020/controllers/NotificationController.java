package ro.tuc.ds2020.controllers;

import ro.tuc.ds2020.entities.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/send-notification")
    @SendTo("/topic/notification")
    public Notification sendNotification(String message) {
        System.out.println("Received message: " + message);
        return new Notification(message); // Returns a notification object
    }
}
