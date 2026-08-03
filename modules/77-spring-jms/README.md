# Module 77: Spring JMS

## Overview
Master Spring JMS (Java Message Service) for messaging integration. This module covers message producers, consumers, templates, and listeners for asynchronous communication.

## Learning Objectives
- Configure JMS with Spring
- Use JmsTemplate for sending messages
- Implement message listeners
- Handle message converters
- Configure connection factories
- Implement request-reply patterns

## Prerequisites
- Module 33: Spring Core
- Module 19: Apache Kafka (messaging concepts)
- Java fundamentals

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | JMS Overview | Java Message Service concepts |
| 02 | Configuration | ConnectionFactory, JmsTemplate setup |
| 03 | Sending Messages | JmsTemplate operations |
| 04 | Receiving Messages | @JmsListener, MessageListener |
| 05 | Message Conversion | JSON, XML converters |
| 06 | Destinations | Queues vs Topics |
| 07 | Error Handling | Dead letter queues, retry |
| 08 | Transactions | JMS transactions |
| 09 | Mini Project | Complete messaging system |

## Key Concepts

### JMS Template
```java
@Service
public class OrderMessageProducer {
    
    private final JmsTemplate jmsTemplate;
    
    public OrderMessageProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    public void sendOrder(OrderMessage message) {
        jmsTemplate.convertAndSend("order-queue", message);
    }
}
```

### Message Listener
```java
@Component
public class OrderMessageListener {
    
    @JmsListener(destination = "order-queue")
    public void receiveOrder(OrderMessage message) {
        System.out.println("Received order: " + message.getOrderId());
        // Process order
    }
}
```

## Module Structure
```
77-spring-jms/
├── README.md
├── pom.xml
├── src/main/java/academy/javaengineering/springjms/
│   ├── config/
│   ├── model/
│   ├── producer/
│   ├── consumer/
│   └── converter/
└── src/test/java/academy/javaengineering/springjms/
```

## References
- [Spring JMS Documentation](https://docs.spring.io/spring-framework/reference/integration/jms.html)
- [Spring JMS Tutorial](https://www.baeldung.com/spring-jms)
