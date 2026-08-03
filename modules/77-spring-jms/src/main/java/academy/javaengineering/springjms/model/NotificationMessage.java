package academy.javaengineering.springjms.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Notification message for JMS examples.
 */
public class NotificationMessage implements Serializable {

    private String notificationId;
    private String userId;
    private String type;
    private String subject;
    private String body;
    private LocalDateTime timestamp;

    public NotificationMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public NotificationMessage(String userId, String type, String subject, String body) {
        this.notificationId = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
