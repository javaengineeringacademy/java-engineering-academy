# Notification Service — Part 2: Implementation Guide

**[← Part 1: Project Overview & Design](README.md)**

---

## Implementation Guide

### Step 1: Implement Strategy Pattern for Delivery

```java
package com.academy.notification.strategy;

import com.academy.notification.model.Notification;
import com.academy.notification.model.enums.NotificationChannel;

public interface DeliveryStrategy {
    DeliveryResult send(Notification notification);
    NotificationChannel getChannel();
    boolean isAvailable();
}

package com.academy.notification.strategy;

public class EmailDeliveryStrategy implements DeliveryStrategy {
    private final EmailProvider emailProvider;
    private final TemplateEngine templateEngine;

    @Override
    public DeliveryResult send(Notification notification) {
        try {
            RenderedContent content = templateEngine.render(
                notification.getContent(),
                notification.getTemplateData()
            );

            EmailRequest request = EmailRequest.builder()
                .to(notification.getRecipientEmail())
                .subject(notification.getSubject())
                .htmlBody(content.getHtmlBody())
                .textBody(content.getTextBody())
                .build();

            String messageId = emailProvider.send(request);
            
            return DeliveryResult.success(messageId);
        } catch (Exception e) {
            return DeliveryResult.failure(e.getMessage());
        }
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public boolean isAvailable() {
        return emailProvider.isConnected();
    }
}

public class SMSDeliveryStrategy implements DeliveryStrategy {
    private final SMSProvider smsProvider;

    @Override
    public DeliveryResult send(Notification notification) {
        try {
            SMSRequest request = new SMSRequest(
                notification.getRecipientPhone(),
                notification.getContent()
            );

            String messageId = smsProvider.send(request);
            return DeliveryResult.success(messageId);
        } catch (Exception e) {
            return DeliveryResult.failure(e.getMessage());
        }
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }
}
```

### Step 2: Implement Template Engine

```java
package com.academy.notification.template;

import java.util.Map;
import java.util.regex.Pattern;

public interface TemplateEngine {
    RenderedContent render(String template, Map<String, Object> data);
}

package com.academy.notification.template;

public class MustacheTemplateEngine implements TemplateEngine {
    private final Mustache.Compiler compiler;

    public MustacheTemplateEngine() {
        this.compiler = Mustache.compiler().escapeHTML(false);
    }

    @Override
    public RenderedContent render(String template, Map<String, Object> data) {
        Mustache mustache = compiler.compile(template);
        String rendered = mustache.execute(new StringWriter(), data).toString();
        
        return new RenderedContent(rendered, rendered);
    }
}

public class TemplateService {
    private final TemplateRepository templateRepository;
    private final TemplateEngine templateEngine;
    private final CacheService cache;

    public RenderedContent renderTemplate(String templateId, 
                                         NotificationChannel channel,
                                         Map<String, Object> data) {
        String cacheKey = "template:" + templateId + ":" + channel;
        Template template = cache.get(cacheKey, () -> 
            templateRepository.findById(templateId)
        );

        String contentTemplate = template.getContent(channel);
        return templateEngine.render(contentTemplate, data);
    }
}
```

### Step 3: Implement Queue-Based Processing

```java
package com.academy.notification.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class InMemoryMessageQueue implements MessageQueue {
    private final BlockingQueue<NotificationMessage> queue;
    private final BlockingQueue<NotificationMessage> deadLetterQueue;
    private final int maxRetries;

    public InMemoryMessageQueue(int capacity, int maxRetries) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.deadLetterQueue = new LinkedBlockingQueue<>();
        this.maxRetries = maxRetries;
    }

    @Override
    public void enqueue(NotificationMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QueueException("Failed to enqueue message", e);
        }
    }

    @Override
    public NotificationMessage dequeue(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public void moveToDeadLetter(NotificationMessage message) {
        deadLetterQueue.offer(message);
    }

    @Override
    public void reprocessDeadLetter() {
        NotificationMessage message;
        while ((message = deadLetterQueue.poll()) != null) {
            if (message.getRetryCount() < maxRetries) {
                message.incrementRetry();
                queue.offer(message);
            }
        }
    }
}

package com.academy.notification.queue;

public class NotificationConsumer implements Runnable {
    private final MessageQueue queue;
    private final DeliveryService deliveryService;
    private final DeliveryEventManager eventManager;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                NotificationMessage message = queue.dequeue(1, TimeUnit.SECONDS);
                if (message != null) {
                    processMessage(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processMessage(NotificationMessage message) {
        Notification notification = message.getNotification();
        
        try {
            DeliveryResult result = deliveryService.deliver(notification);
            
            if (result.isSuccess()) {
                notification.updateStatus(DeliveryStatus.SENT);
                eventManager.notifyDeliverySuccess(notification);
            } else {
                handleFailure(message, result.getErrorMessage());
            }
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }

    private void handleFailure(NotificationMessage message, String error) {
        Notification notification = message.getNotification();
        notification.incrementRetry();
        notification.setErrorMessage(error);
        
        if (message.getRetryCount() < maxRetries) {
            long delay = retryPolicy.getNextDelay(message.getRetryCount());
            scheduleRetry(message, delay);
        } else {
            notification.updateStatus(DeliveryStatus.FAILED);
            queue.moveToDeadLetter(message);
            eventManager.notifyDeliveryFailed(notification);
        }
    }
}
```

### Step 4: Implement Notification Service

```java
package com.academy.notification.service;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationService {
    private final TemplateService templateService;
    private final PreferenceService preferenceService;
    private final DeliveryService deliveryService;
    private final MessageQueue queue;
    private final DeliveryEventManager eventManager;

    public List<String> sendNotification(NotificationRequest request) {
        String userId = request.getRecipientId();
        UserPreferences preferences = preferenceService.getPreferences(userId);

        List<NotificationChannel> enabledChannels = request.getChannels().stream()
            .filter(preferences::isChannelEnabled)
            .filter(channel -> !preferences.isQuietHours(LocalDateTime.now()))
            .collect(Collectors.toList());

        if (enabledChannels.isEmpty()) {
            throw new NoEnabledChannelException("No enabled channels for user");
        }

        List<String> notificationIds = new ArrayList<>();
        
        for (NotificationChannel channel : enabledChannels) {
            RenderedContent content = templateService.renderTemplate(
                request.getTemplateId(),
                channel,
                request.getTemplateData()
            );

            Notification notification = Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .recipientId(userId)
                .channel(channel)
                .content(content.getBody())
                .subject(content.getSubject())
                .priority(request.getPriority())
                .build();

            queue.enqueue(new NotificationMessage(notification));
            notificationIds.add(notification.getNotificationId());
        }

        return notificationIds;
    }

    public List<String> sendBulkNotification(List<NotificationRequest> requests) {
        return requests.stream()
            .flatMap(req -> sendNotification(req).stream())
            .collect(Collectors.toList());
    }
}
```

## Unit Tests

```java
package com.academy.notification;

import com.academy.notification.model.*;
import com.academy.notification.service.NotificationService;
import com.academy.notification.strategy.*;
import com.academy.notification.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    
    @Mock
    private EmailProvider emailProvider;
    
    @Mock
    private SMSProvider smsProvider;
    
    private NotificationService notificationService;
    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService();
        deliveryService.registerStrategy(new EmailDeliveryStrategy(emailProvider));
        deliveryService.registerStrategy(new SMSDeliveryStrategy(smsProvider));
        
        notificationService = new NotificationService(deliveryService);
    }

    @Test
    void testSendEmailNotification() {
        NotificationRequest request = NotificationRequest.builder()
            .recipientId("user1")
            .templateId("welcome")
            .channels(List.of(NotificationChannel.EMAIL))
            .templateData(Map.of("name", "John"))
            .build();

        when(emailProvider.send(any())).thenReturn("msg-123");

        List<String> ids = notificationService.sendNotification(request);
        
        assertEquals(1, ids.size());
        verify(emailProvider).send(any());
    }

    @Test
    void testSendSMSNotification() {
        NotificationRequest request = NotificationRequest.builder()
            .recipientId("user1")
            .templateId("otp")
            .channels(List.of(NotificationChannel.SMS))
            .templateData(Map.of("otp", "123456"))
            .build();

        when(smsProvider.send(any())).thenReturn("sms-123");

        List<String> ids = notificationService.sendNotification(request);
        
        assertEquals(1, ids.size());
        verify(smsProvider).send(any());
    }

    @Test
    void testUserPreferences() {
        UserPreferences prefs = new UserPreferences("user1");
        prefs.setChannelPreference(NotificationChannel.EMAIL, true);
        prefs.setChannelPreference(NotificationChannel.SMS, false);

        NotificationRequest request = NotificationRequest.builder()
            .recipientId("user1")
            .channels(List.of(NotificationChannel.EMAIL, NotificationChannel.SMS))
            .build();

        List<String> ids = notificationService.sendNotification(request);
        
        assertEquals(1, ids.size());
        verify(emailProvider, times(1)).send(any());
        verify(smsProvider, never()).send(any());
    }

    @Test
    void testRetryOnFailure() {
        Notification notification = createTestNotification();
        
        when(emailProvider.send(any()))
            .thenThrow(new RuntimeException("Service unavailable"))
            .thenReturn("msg-123");

        DeliveryResult result = deliveryService.deliverWithRetry(notification);
        
        assertTrue(result.isSuccess());
        verify(emailProvider, times(2)).send(any());
    }

    @Test
    void testTemplateRendering() {
        String template = "Hello {{name}}, welcome to our service!";
        Map<String, Object> data = Map.of("name", "John");
        
        TemplateEngine engine = new MustacheTemplateEngine();
        RenderedContent content = engine.render(template, data);
        
        assertEquals("Hello John, welcome to our service!", content.getBody());
    }
}
```

## Extension Challenges

1. **WebSocket Notifications**: Real-time in-app notifications via WebSocket
2. **Notification Groups**: Group notifications and send digests
3. **A/B Testing**: Test different notification content
4. **Analytics Dashboard**: Track open rates, click rates
5. **Multi-Language**: Support internationalized notifications

## Interview Questions

1. **How would you ensure exactly-once delivery?**
   - Discuss idempotency, deduplication, message acknowledgments

2. **How would you handle 1 million notifications per hour?**
   - Discuss horizontal scaling, partitioning, load balancing

3. **What are the trade-offs of synchronous vs asynchronous delivery?**
   - Discuss latency, reliability, resource usage

4. **How would you implement notification preferences at scale?**
   - Discuss caching, database design, real-time updates

5. **How would you design for multi-region deployment?**
   - Discuss data residency, latency optimization, failover

## References

- [Strategy Pattern](https://www.baeldung.com/java-strategy-pattern)
- [Template Method Pattern](https://www.baeldung.com/java-template-method-pattern)
- [Message Queue Patterns](https://www.enterpriseintegrationpatterns.com/patterns/messaging/)