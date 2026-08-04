# Spring Integration - Channels

## Overview

Channels are the message pipes in Spring Integration. They connect endpoints and provide different delivery semantics.

## Table of Contents

1. [Channel Types](#channel-types)
2. [Direct Channel](#direct-channel)
3. [Publish-Subscribe Channel](#publish-subscribe-channel)
4. [Queue Channel](#queue-channel)
5. [Executor Channel](#executor-channel)
6. [Priority Channel](#priority-channel)
7. [Rendezvous Channel](#rendezvous-channel)
8. [Channel Configuration](#channel-configuration)

## Channel Types

### Overview

| Channel | Delivery | Threading | Use Case |
|---------|----------|-----------|----------|
| Direct | Synchronous | Caller | Point-to-point |
| Publish-Subscribe | Synchronous | Caller | Broadcasting |
| Queue | Asynchronous | Consumer | Buffered |
| Executor | Asynchronous | Pool | Parallel |
| Priority | Asynchronous | Consumer | Priority-based |
| Rendezvous | Synchronous | Handoff | Load balancing |

## Direct Channel

### Characteristics

- Synchronous delivery
- One consumer per message
- Thread of producer continues

### Configuration

```java
@Bean
public MessageChannel directChannel() {
    return new DirectChannel();
}

// With interceptor
@Bean
public MessageChannel directChannelWithInterceptor() {
    DirectChannel channel = new DirectChannel();
    channel.addInterceptor(new ChannelInterceptor() {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            System.out.println("Before send: " + message.getPayload());
            return message;
        }
        
        @Override
        public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
            System.out.println("After send: " + message.getPayload());
        }
    });
    return channel;
}
```

### Usage

```java
@ServiceActivator(inputChannel = "directChannel")
public void process(String payload) {
    System.out.println("Processing: " + payload);
}

// Send message
@Autowired
private MessageChannel directChannel;

public void send(String message) {
    directChannel.send(MessageBuilder.withPayload(message).build());
}
```

## Publish-Subscribe Channel

### Characteristics

- Broadcasts to all subscribers
- Synchronous delivery
- All subscribers receive each message

### Configuration

```java
@Bean
public MessageChannel eventChannel() {
    return new PublishSubscribeChannel();
}

// With executor
@Bean
public MessageChannel asyncEventChannel() {
    return new PublishSubscribeChannel(executorService());
}

@Bean
public Executor executorService() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    return executor;
}
```

### Subscribers

```java
@Subscriber(inputChannel = "eventChannel")
public void subscriber1(Message<?> message) {
    System.out.println("Subscriber 1: " + message.getPayload());
}

@Subscriber(inputChannel = "eventChannel")
public void subscriber2(Message<?> message) {
    System.out.println("Subscriber 2: " + message.getPayload());
}
```

## Queue Channel

### Characteristics

- Asynchronous delivery
- Buffered messages
- Consumer polls for messages

### Configuration

```java
@Bean
public MessageChannel queueChannel() {
    return new QueueChannel(100); // capacity 100
}

// With polling
@Bean
public PollerSpec queuePoller() {
    return Pollers.fixedDelay(1000)
        .maxMessagesPerPoll(10);
}
```

### Usage

```java
// Producer (async)
@Autowired
private MessageChannel queueChannel;

public void send(String message) {
    queueChannel.send(MessageBuilder.withPayload(message).build());
}

// Consumer (polling)
@ServiceActivator(inputChannel = "queueChannel")
public void process(String payload) {
    System.out.println("Processing: " + payload);
}
```

## Executor Channel

### Characteristics

- Asynchronous delivery
- Thread pool for consumers
- Parallel processing

### Configuration

```java
@Bean
public MessageChannel executorChannel() {
    return new ExecutorChannel(threadPoolTaskExecutor());
}

@Bean
public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    executor.setThreadNamePrefix("integration-");
    executor.initialize();
    return executor;
}
```

### Usage

```java
@ServiceActivator(inputChannel = "executorChannel")
public void process(String payload) {
    System.out.println("Processing in thread: " + 
        Thread.currentThread().getName());
}
```

## Priority Channel

### Characteristics

- Messages ordered by priority
- Higher priority processed first

### Configuration

```java
@Bean
public MessageChannel priorityChannel() {
    return new PriorityChannel(100, 
        Comparator.comparingInt(msg -> 
            msg.getHeaders().get("priority", Integer.class)));
}
```

### Usage

```java
// Send with priority
Message<String> message = MessageBuilder
    .withPayload("urgent")
    .setHeader("priority", 10)
    .build();

priorityChannel.send(message);

// Consumer processes highest priority first
@ServiceActivator(inputChannel = "priorityChannel")
public void process(String payload) {
    System.out.println("Processing: " + payload);
}
```

## Rendezvous Channel

### Characteristics

- Synchronous handoff
- Direct connection between producer and consumer
- No buffering

### Configuration

```java
@Bean
public MessageChannel rendezvousChannel() {
    return new RendezvousChannel();
}
```

## Channel Configuration

### With Interceptors

```java
@Bean
public MessageChannel channelWithInterceptors() {
    DirectChannel channel = new DirectChannel();
    
    channel.addInterceptor(new ChannelInterceptor() {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            log.info("Pre-send: {}", message.getPayload());
            return message;
        }
    });
    
    return channel;
}
```

### With Dispatcher

```java
@Bean
public MessageChannel channelWithDispatcher() {
    DirectChannel channel = new DirectChannel();
    channel.setBeanName("myChannel");
    return channel;
}
```

### XML Configuration

```xml
<int:channel id="directChannel"/>

<int:channel id="queueChannel">
    <int:queue capacity="100"/>
</int:channel>

<int:channel id="priorityChannel">
    <int:priority-queue capacity="100"/>
</int:channel>

<int:publish-subscribe-channel id="pubSubChannel"/>
```

## Best Practices

1. **Choose appropriate type**: Match channel to use case
2. **Use direct for sync**: When processing must be synchronous
3. **Use queue for async**: When buffering is needed
4. **Use executor for parallel**: When concurrent processing is needed
5. **Configure interceptors**: For logging and monitoring
6. **Set capacity**: Prevent memory issues
7. **Handle errors**: Configure error channels
8. **Monitor channels**: Track channel metrics

## References

- [Spring Integration Channels](https://docs.spring.io/spring-integration/reference/core/messaging-channels.html)
