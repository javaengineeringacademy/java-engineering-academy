# Kafka Project Structure

> Standard Kafka project layout for Java/Maven applications.

## Maven Project Structure

```
kafka-app/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── KafkaApplication.java
│   │   │           ├── producer/
│   │   │           │   ├── OrderProducer.java
│   │   │           │   └── ProducerConfig.java
│   │   │           ├── consumer/
│   │   │           │   ├── OrderConsumer.java
│   │   │           │   ├── ConsumerConfig.java
│   │   │           │   └── MessageListener.java
│   │   │           ├── model/
│   │   │           │   ├── Order.java
│   │   │           │   └── OrderEvent.java
│   │   │           ├── config/
│   │   │           │   ├── KafkaConfig.java
│   │   │           │   └── TopicConfig.java
│   │   │           ├── serializer/
│   │   │           │   └── JsonSerializer.java
│   │   │           └── handler/
│   │   │               └── ErrorHandler.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── logback.xml
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── producer/
│       │           │   └── OrderProducerTest.java
│       │           ├── consumer/
│       │           │   └── OrderConsumerTest.java
│       │           └── integration/
│       │               └── KafkaIntegrationTest.java
│       └── resources/
│           └── application-test.yml
└── docker-compose.yml
```

## pom.xml Dependencies

```xml
<dependencies>
    <!-- Spring Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    
    <!-- Kafka Clients -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
    </dependency>
    
    <!-- Schema Registry -->
    <dependency>
        <groupId>io.confluent</groupId>
        <artifactId>kafka-avro-serializer</artifactId>
    </dependency>
    
    <!-- Test -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Application Configuration

```yaml
# application.yml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      properties:
        enable.idempotence: true
        max.in.flight.requests.per.connection: 5
    consumer:
      group-id: order-service
      auto-offset-reset: earliest
      enable-auto-commit: false
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "com.example.model"
    listener:
      ack-mode: manual
      concurrency: 3
      type: batch
```

## Producer Pattern

```java
@Service
public class OrderProducer {
    
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    @Autowired
    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public CompletableFuture<SendResult<String, OrderEvent>> sendOrder(OrderEvent event) {
        return kafkaTemplate.send("orders", event.getOrderId(), event);
    }
    
    @KafkaListener(topics = "order-events", groupId = "audit-service")
    public void auditEvents(OrderEvent event) {
        log.info("Auditing event: {}", event.getOrderId());
    }
}
```

## Consumer Pattern

```java
@Service
public class OrderConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);
    
    @KafkaListener(
        topics = "orders",
        groupId = "order-processor",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Retryable(
        value = {RuntimeException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public void processOrder(
            @Payload OrderEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {
        
        log.info("Processing order: {} from partition: {} offset: {}", 
            event.getOrderId(), partition, offset);
        
        try {
            orderService.process(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order: {}", event.getOrderId(), e);
            throw e;  // will be retried
        }
    }
    
    @KafkaListener(
        topics = "dead-letter-topic",
        groupId = "dlq-handler"
    )
    public void handleDLQ(OrderEvent event) {
        log.error("Processing dead letter: {}", event);
        alertService.sendAlert("Failed order: " + event.getOrderId());
    }
}
```

## Error Handling

```java
@Configuration
public class KafkaErrorHandler {
    
    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<String, Object> template) {
        
        DeadLetterPublishingRecoverer recoverer = 
            new DeadLetterPublishingRecoverer(template);
        
        return new DefaultErrorHandler(recoverer,
            new FixedBackOff(1000L, 3L));  // 1s delay, 3 retries
    }
}
```

## Topic Configuration

```java
@Configuration
public class TopicConfig {
    
    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("orders")
            .partitions(3)
            .replicas(3)
            .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")
            .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
            .build();
    }
    
    @Bean
    public NewTopic deadLetterTopic() {
        return TopicBuilder.name("dead-letter-topic")
            .partitions(1)
            .replicas(1)
            .build();
    }
}
```

## References

- [Spring Kafka Documentation](https://spring.io/projects/spring-kafka)
- [Kafka Clients Documentation](https://kafka.apache.org/documentation/)

---
**Prerequisites:** [Kafka installation](installation.md)
**Related:** [Kafka configuration](configuration.md) | [Kafka best-practices](../../14-cloud/azure/best-practices.md)
**Next:** [Kafka performance](../../14-cloud/azure/performance.md)
