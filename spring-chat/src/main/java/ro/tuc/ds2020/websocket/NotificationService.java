package ro.tuc.ds2020.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotification(String userId, String message) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("type", "alert");
        messageData.put("message", message);

        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/notification", messageData);
    }
}
