# Notification Service

## Project Overview

A Notification Service that handles sending notifications across multiple channels (Email, SMS, Push, In-App) with template management, queue-based processing, and delivery tracking. This enterprise project introduces the Strategy pattern for channel selection, the Observer pattern for delivery tracking, and the Template Method pattern for notification processing. Students will design a scalable, reliable notification system.

## Learning Outcomes

- Implement the Strategy pattern for multi-channel delivery
- Use the Observer pattern for delivery status tracking
- Apply the Template Method pattern for notification processing
- Design queue-based asynchronous processing
- Implement retry mechanisms with exponential backoff
- Design for delivery guarantees (at-least-once, exactly-once)
- Implement template engines

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Send notifications via Email | Must |
| FR02 | Send notifications via SMS | Must |
| FR03 | Send push notifications to mobile | Must |
| FR04 | Template-based notification content | Must |
| FR05 | Delivery status tracking | Must |
| FR06 | Retry failed notifications | Must |
| FR07 | Notification scheduling | Should |
| FR08 | User preference management | Should |
| FR09 | Batch notification support | Could |
| FR10 | Notification analytics | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Process 100,000+ notifications per hour |
| NFR02 | Email delivery < 5 seconds |
| NFR03 | SMS delivery < 10 seconds |
| NFR04 | 99.9% delivery success rate |
| NFR05 | Support notification preferences |

## Architecture

```mermaid
graph TB
    subgraph Producers
        API[REST API]
        Event[Event System]
        Scheduler[Scheduler]
    end
    
    subgraph Notification Service
        NS[NotificationService]
        TS[TemplateService]
        PS[PreferenceService]
    end
    
    subgraph Processing
        Queue[Message Queue]
        Consumer[Queue Consumer]
        RetryHandler[Retry Handler]
    end
    
    subgraph Delivery
        DS[DeliveryService]
        Email[Email Channel]
        SMS[SMS Channel]
        Push[Push Channel]
        InApp[In-App Channel]
    end
    
    subgraph Storage
        NR[Notification Repository]
        TR[Template Repository]
        UR[User Preferences]
    end
    
    API --> NS
    Event --> NS
    Scheduler --> NS
    NS --> TS
    NS --> PS
    NS --> Queue
    Queue --> Consumer
    Consumer --> DS
    DS --> Email
    DS --> SMS
    DS --> Push
    DS --> InApp
    DS --> RetryHandler
    RetryHandler --> Queue
```

## Package Structure

```
notification-service/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── notification/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Notification.java
│                       │   ├── Template.java
│                       │   ├── UserPreferences.java
│                       │   ├── NotificationRequest.java
│                       │   ├── DeliveryResult.java
│                       │   └── enums/
│                       │       ├── NotificationChannel.java
│                       │       ├── NotificationStatus.java
│                       │       ├── NotificationPriority.java
│                       │       └── DeliveryStatus.java
│                       ├── strategy/
│                       │   ├── DeliveryStrategy.java
│                       │   ├── EmailDeliveryStrategy.java
│                       │   ├── SMSDeliveryStrategy.java
│                       │   ├── PushDeliveryStrategy.java
│                       │   └── InAppDeliveryStrategy.java
│                       ├── template/
│                       │   ├── TemplateEngine.java
│                       │   ├── MustacheTemplateEngine.java
│                       │   ├── TemplateService.java
│                       │   └── RenderedContent.java
│                       ├── queue/
│                       │   ├── MessageQueue.java
│                       │   ├── InMemoryMessageQueue.java
│                       │   ├── NotificationMessage.java
│                       │   └── NotificationConsumer.java
│                       ├── observer/
│                       │   ├── DeliveryObserver.java
│                       │   ├── DeliveryEventManager.java
│                       │   ├── StatusChangeHandler.java
│                       │   └── AnalyticsHandler.java
│                       ├── service/
│                       │   ├── NotificationService.java
│                       │   ├── DeliveryService.java
│                       │   ├── PreferenceService.java
│                       │   └── TemplateRepository.java
│                       └── exception/
│                           ├── NotificationException.java
│                           ├── DeliveryFailedException.java
│                           ├── TemplateNotFoundException.java
│                           └── NoEnabledChannelException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── notification/
                        ├── NotificationServiceTest.java
                        ├── DeliveryServiceTest.java
                        ├── TemplateEngineTest.java
                        └── QueueTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Notification {
        -String notificationId
        -String recipientId
        -NotificationChannel channel
        -NotificationStatus status
        -String content
        -String subject
        -NotificationPriority priority
        -LocalDateTime createdAt
        -LocalDateTime sentAt
        -String errorMessage
        -int retryCount
        +Notification(id, recipient, channel)
        +getNotificationId() String
        +getStatus() NotificationStatus
        +updateStatus(NotificationStatus) void
        +incrementRetry() void
        +shouldRetry() boolean
    }
    
    class Template {
        -String templateId
        -String name
        -Map~String,String~ channelTemplates
        -Map~String,Object~ defaultData
        +Template(id, name)
        +getContent(NotificationChannel) String
        +getDefaultData() Map
    }
    
    class UserPreferences {
        -String userId
        -Map~NotificationChannel,Boolean~ channelPreferences
        -LocalTime quietHoursStart
        -LocalTime quietHoursEnd
        -Map~String,List~String~~ topicPreferences
        +UserPreferences(userId)
        +isChannelEnabled(NotificationChannel) boolean
        +isQuietHours(LocalDateTime) boolean
        +isTopicSubscribed(String) boolean
    }
    
    class DeliveryStrategy {
        <<interface>>
        +send(Notification) DeliveryResult
        +getChannel() NotificationChannel
        +isAvailable() boolean
    }
    
    class EmailDeliveryStrategy {
        -EmailProvider emailProvider
        -TemplateEngine templateEngine
        +send(Notification) DeliveryResult
        +getChannel() NotificationChannel
    }
    
    class SMSDeliveryStrategy {
        -SMSProvider smsProvider
        +send(Notification) DeliveryResult
        +getChannel() NotificationChannel
    }
    
    class DeliveryService {
        -Map~NotificationChannel,DeliveryStrategy~ strategies
        -RetryHandler retryHandler
        +registerStrategy(DeliveryStrategy) void
        +deliver(Notification) DeliveryResult
        +deliverWithRetry(Notification) DeliveryResult
    }
    
    class NotificationService {
        -TemplateService templateService
        -PreferenceService preferenceService
        -DeliveryService deliveryService
        -MessageQueue queue
        +sendNotification(NotificationRequest) List~String~
        +sendBulkNotification(List~NotificationRequest~) List~String~
        +getNotificationStatus(String) NotificationStatus
    }
    
    class MessageQueue {
        <<interface>>
        +enqueue(NotificationMessage) void
        +dequeue(long, TimeUnit) NotificationMessage
        +moveToDeadLetter(NotificationMessage) void
    }
    
    class NotificationConsumer {
        -MessageQueue queue
        -DeliveryService deliveryService
        -DeliveryEventManager eventManager
        +run() void
        -processMessage(NotificationMessage) void
    }
    
    class DeliveryEventManager {
        -List~DeliveryObserver~ observers
        +subscribe(DeliveryObserver) void
        +unsubscribe(DeliveryObserver) void
        +notifyDeliverySuccess(Notification) void
        +notifyDeliveryFailed(Notification) void
    }
    
    Notification --> NotificationChannel
    Notification --> NotificationStatus
    Notification --> NotificationPriority
    UserPreferences --> NotificationChannel
    DeliveryStrategy <|.. EmailDeliveryStrategy
    DeliveryStrategy <|.. SMSDeliveryStrategy
    DeliveryService --> DeliveryStrategy
    NotificationService --> DeliveryService
    NotificationService --> MessageQueue
    NotificationConsumer --> MessageQueue
    NotificationConsumer --> DeliveryService
    DeliveryEventManager --> DeliveryObserver
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**