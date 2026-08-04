# GraphQL Subscriptions

## Comprehensive Guide to Real-Time GraphQL

GraphQL subscriptions enable real-time communication between client and server using WebSockets. This guide covers subscription implementation, WebSocket protocols, and best practices.

---

## Table of Contents

1. [Subscription Fundamentals](#subscription-fundamentals)
2. [WebSocket Transport](#websocket-transport)
3. [Server Implementation](#server-implementation)
4. [Client Implementation](#client-implementation)
5. [Authentication](#authentication)
6. [Error Handling](#error-handling)
7. [Best Practices](#best-practices)

---

## Subscription Fundamentals

### Schema Definition

```graphql
type Subscription {
  # Real-time message delivery
  messageSent(channelId: ID!): Message!

  # User presence
  userStatusChanged(userId: ID!): UserStatus!

  # Order updates
  orderUpdated(orderId: ID!): OrderUpdate!

  # Live data stream
  sensorData(sensorId: ID!): SensorReading!

  # Notifications
  notificationReceived(userId: ID!): Notification!
}

type Message {
  id: ID!
  content: String!
  sender: User!
  channelId: ID!
  createdAt: DateTime!
}

type OrderUpdate {
  orderId: ID!
  status: OrderStatus!
  timestamp: DateTime!
  message: String
}

type Notification {
  id: ID!
  type: NotificationType!
  title: String!
  message: String!
  read: Boolean!
  createdAt: DateTime!
}
```

### Operation Types

```graphql
# Regular query - request/response
query GetUser($id: ID!) {
  user(id: $id) {
    name
    email
  }
}

# Mutation - request/response with side effects
mutation SendMessage($input: SendMessageInput!) {
  sendMessage(input: $input) {
    id
    content
    createdAt
  }
}

# Subscription - persistent connection with stream
subscription OnMessageSent($channelId: ID!) {
  messageSent(channelId: $channelId) {
    id
    content
    sender {
      name
    }
    createdAt
  }
}
```

---

## WebSocket Transport

### WebSocket Lifecycle

```
Client                          Server
  |                                |
  |--- WebSocket Init ----------->|
  |    (connection_init)           |
  |                                |
  |<-- Connection Ack -------------|
  |    (connection_ack)            |
  |                                |
  |--- Subscription Start -------->|
  |    (start, query)              |
  |                                |
  |<-- Data ----------------------|
  |    (data, result)              |
  |                                |
  |<-- Data ----------------------|
  |    (data, result)              |
  |                                |
  |--- Subscription Stop --------->|
  |    (stop)                      |
  |                                |
  |<-- Complete -------------------|
  |    (complete)                  |
  |                                |
  |--- WebSocket Close ----------->|
  |    (close)                     |
```

### Transport Protocol

```javascript
// WebSocket connection
const ws = new WebSocket('ws://localhost:8080/graphql', 'graphql-ws');

// Connection initialization
ws.onopen = () => {
  ws.send(JSON.stringify({
    type: 'connection_init',
    payload: {
      authorization: 'Bearer token123'
    }
  }));
};

// Handle connection ack
ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  if (data.type === 'connection_ack') {
    // Ready to subscribe
    startSubscription();
  }
};

// Start subscription
function startSubscription() {
  ws.send(JSON.stringify({
    id: '1',
    type: 'start',
    payload: {
      query: `subscription OnMessageSent($channelId: ID!) {
        messageSent(channelId: $channelId) {
          id
          content
          sender { name }
          createdAt
        }
      }`,
      variables: { channelId: 'channel-1' }
    }
  }));
}
```

### Protocol Messages

```json
// Client -> Server
{
  "type": "connection_init",
  "payload": { "authorization": "Bearer token" }
}

// Server -> Client
{
  "type": "connection_ack"
}

// Client -> Server
{
  "id": "sub-1",
  "type": "start",
  "payload": {
    "query": "subscription { messageSent { id content } }"
  }
}

// Server -> Client
{
  "id": "sub-1",
  "type": "data",
  "payload": {
    "data": {
      "messageSent": {
        "id": "msg-1",
        "content": "Hello!"
      }
    }
  }
}

// Server -> Client
{
  "id": "sub-1",
  "type": "complete"
}
```

---

## Server Implementation

### Spring GraphQL with WebSocket

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/graphql")
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }
}

@Component
public class MessageSubscriptionResolver {

    @SubscriptionMapping
    public Flux<Message> messageSent(
            @Argument String channelId,
            @ContextValue GraphQLContext context) {

        User currentUser = context.get("currentUser");
        if (currentUser == null) {
            return Flux.error(new GraphQLException("Not authenticated"));
        }

        return messageStream.getMessageStream(channelId)
            .filter(message -> message.getSenderId() != currentUser.getId())
            .onErrorResume(e -> Flux.error(new GraphQLException(e.getMessage())));
    }
}
```

### Reactive Subscription Implementation

```java
@Component
public class OrderSubscriptionResolver {

    private final Sinks.Many<OrderUpdate> orderUpdateSink = Sinks.many()
        .multicast()
        .onBackpressureBuffer();

    @SubscriptionMapping
    public Flux<OrderUpdate> orderUpdated(@Argument String orderId) {
        return orderUpdateSink.asFlux()
            .filter(update -> update.getOrderId().equals(orderId));
    }

    @MutationMapping
    public Order updateOrderStatus(
            @Argument String orderId,
            @Argument OrderStatus status) {

        Order order = orderService.updateStatus(orderId, status);

        OrderUpdate update = OrderUpdate.builder()
            .orderId(orderId)
            .status(status)
            .timestamp(Instant.now())
            .message("Status updated to " + status)
            .build();

        orderUpdateSink.tryEmitNext(update);
        return order;
    }
}
```

### Kotlin Coroutines Subscription

```kotlin
@Component
class NotificationSubscriptionResolver {

    private val notificationChannel = Channel<Notification>(Channel.BUFFERED)

    @SubscriptionMapping
    suspend fun notificationReceived(
        @Argument userId: String
    ): Flow<Notification> = flow {
        notificationChannel.receiveAsFlow()
            .filter { it.userId == userId }
            .collect { emit(it) }
    }

    suspend fun sendNotification(notification: Notification) {
        notificationChannel.send(notification)
    }
}
```

### Java Executor Subscription

```java
@Component
public class SensorSubscriptionResolver {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @SubscriptionMapping
    public Publisher<SensorReading> sensorData(@Argument String sensorId) {
        return subscriber -> {
            executor.submit(() -> {
                try {
                    while (subscriber.isActive()) {
                        SensorReading reading = sensorService
                            .getCurrentReading(sensorId);

                        if (reading != null) {
                            subscriber.onNext(reading);
                        }

                        Thread.sleep(1000); // Polling interval
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        };
    }
}
```

### DGS Framework Subscriptions

```kotlin
@DgsComponent
class MessageSubscriptionResolver {

    @DgsSubscription(
        field = "messageSent"
    )
    fun messageSent(
        @InputArgument channelId: String,
        env: DgsDataFetchingEnvironment
    ): Flux<Message> {
        val currentUser = env.getContext<GraphQLContext>().get<User>("currentUser")
            ?: throw GraphQLException("Not authenticated")

        return messageStream.getMessageStream(channelId)
            .filter { it.senderId != currentUser.id }
    }
}
```

---

## Client Implementation

### Apollo Client (JavaScript)

```javascript
import { gql, useSubscription } from '@apollo/client';
import { WebSocketLink } from '@apollo/client/link/ws';
import { split, HttpLink } from '@apollo/client';
import { getMainDefinition } from '@apollo/client/utilities';

// Create WebSocket link
const wsLink = new WebSocketLink({
  uri: 'ws://localhost:8080/graphql',
  options: {
    reconnect: true,
    lazy: true,
    connectionParams: () => ({
      authorization: localStorage.getItem('token'),
    }),
  },
});

// Split link: HTTP for queries/mutations, WS for subscriptions
const splitLink = split(
  ({ query }) => {
    const definition = getMainDefinition(query);
    return (
      definition.kind === 'OperationDefinition' &&
      definition.operation === 'subscription'
    );
  },
  wsLink,
  httpLink
);

// Subscription component
function MessageSubscription({ channelId }) {
  const { data, loading, error } = useSubscription(
    gql`
      subscription OnMessageSent($channelId: ID!) {
        messageSent(channelId: $channelId) {
          id
          content
          sender {
            name
            avatar
          }
          createdAt
        }
      }
    `,
    { variables: { channelId } }
  );

  if (loading) return <div>Connecting...</div>;
  if (error) return <div>Error: {error.message}</div>;
  if (data) {
    return (
      <div className="message">
        <strong>{data.messageSent.sender.name}</strong>
        <p>{data.messageSent.content}</p>
      </div>
    );
  }
  return null;
}
```

### Apollo Client Subscription with Cache

```javascript
import { gql, useSubscription } from '@apollo/client';

function OrderUpdates({ orderId }) {
  useSubscription(
    gql`
      subscription OnOrderUpdated($orderId: ID!) {
        orderUpdated(orderId: $orderId) {
          orderId
          status
          timestamp
          message
        }
      }
    `,
    {
      variables: { orderId },
      onData: ({ data }) => {
        const orderUpdate = data.data.orderUpdated;

        // Update cache
        client.cache.modify({
          id: `Order:${orderId}`,
          fields: {
            status: () => orderUpdate.status,
            updatedAt: () => orderUpdate.timestamp,
          },
        });
      },
    }
  );

  return null;
}
```

### React with Subscriptions

```typescript
import { useEffect, useState } from 'react';
import { useSubscription, gql } from '@apollo/client';

const MESSAGE_SUBSCRIPTION = gql`
  subscription OnMessage($channelId: ID!) {
    messageSent(channelId: $channelId) {
      id
      content
      sender { name }
    }
  }
`;

function ChatRoom({ channelId }: { channelId: string }) {
  const [messages, setMessages] = useState<Message[]>([]);

  const { data, error } = useSubscription(MESSAGE_SUBSCRIPTION, {
    variables: { channelId },
    onSubscriptionData: ({ subscriptionData }) => {
      setMessages(prev => [...prev, subscriptionData.data.messageSent]);
    },
  });

  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {messages.map(msg => (
        <div key={msg.id}>
          <strong>{msg.sender.name}</strong>: {msg.content}
        </div>
      ))}
    </div>
  );
}
```

### Subscription with React Query (Alternative)

```typescript
import { useSubscription } from 'react-sse';

function SensorData({ sensorId }: { sensorId: string }) {
  const { data, error } = useSubscription({
    url: 'http://localhost:8080/graphql',
    body: {
      query: `
        subscription {
          sensorData(sensorId: "${sensorId}") {
            temperature
            humidity
            timestamp
          }
        }
      `
    },
    headers: {
      Authorization: `Bearer ${getToken()}`
    }
  });

  if (error) return <div>Error</div>;
  return <div>Temperature: {data?.sensorData?.temperature}</div>;
}
```

---

## Authentication

### WebSocket Authentication

```java
@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        String token = extractToken(request);
        if (token == null) {
            return false;
        }

        try {
            User user = tokenValidator.validate(token);
            attributes.put("currentUser", user);
            return true;
        } catch (InvalidTokenException e) {
            return false;
        }
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return request.getURI().getQuery() != null
            ? Arrays.stream(request.getURI().getQuery().split("&"))
                .filter(q -> q.startsWith("token="))
                .map(q -> q.substring(6))
                .findFirst()
                .orElse(null)
            : null;
    }
}
```

### Context Propagation

```java
@Component
public class SubscriptionResolver {

    @SubscriptionMapping
    public Flux<Message> messageSent(
            @Argument String channelId,
            @ContextValue GraphQLContext context) {

        User currentUser = context.get("currentUser");

        if (currentUser == null) {
            return Flux.error(new GraphQLException("UNAUTHORIZED"));
        }

        // Propagate context to reactive stream
        return Flux.deferContextual(ctx -> {
            ctx.put("currentUser", currentUser);
            return messageService.getStream(channelId);
        });
    }
}
```

### Token Refresh

```javascript
const wsLink = new WebSocketLink({
  uri: 'ws://localhost:8080/graphql',
  options: {
    connectionParams: async () => {
      const token = await getAccessToken();
      return { authorization: `Bearer ${token}` };
    },
    on: {
      connected: (socket) => {
        console.log('WebSocket connected');
      },
      error: (error) => {
        console.error('WebSocket error:', error);
      },
    },
  },
});
```

---

## Error Handling

### Subscription Errors

```java
@Component
public class SubscriptionErrorHandler {

    @DgsExceptionHandler
    public GraphQLError handle(GraphQLException exception) {
        return GraphqlErrorBuilder.newError()
            .message(exception.getMessage())
            .errorType(ErrorType.SUBSCRIPTION_FAILED)
            .extensions(Map.of(
                "code", exception.getCode(),
                "retryable", isRetryable(exception)
            ))
            .build();
    }

    private boolean isRetryable(GraphQLException exception) {
        return switch (exception.getCode()) {
            case "TRANSIENT_ERROR" -> true;
            case "RATE_LIMITED" -> true;
            default -> false;
        };
    }
}
```

### Client Error Recovery

```javascript
import { useSubscription } from '@apollo/client';

function ResilientSubscription({ channelId }) {
  const { data, error, loading } = useSubscription(MESSAGE_SUBSCRIPTION, {
    variables: { channelId },
    onError: (error) => {
      console.error('Subscription error:', error);

      // Implement retry logic
      if (error.message.includes('NETWORK_ERROR')) {
        setTimeout(() => {
          wsLink.client.restart();
        }, 5000);
      }
    },
    onSubscriptionComplete: () => {
      console.log('Subscription completed');
    },
  });

  if (loading) return <div>Reconnecting...</div>;
  if (error) return <div>Error: {error.message}</div>;
  return <div>{JSON.stringify(data)}</div>;
}
```

### Server-Side Error Handling

```java
@Component
public class SubscriptionErrorResolver {

    @SubscriptionMapping
    public Flux<Message> messageSent(
            @Argument String channelId,
            @ContextValue GraphQLContext context) {

        return messageService.getStream(channelId)
            .doOnNext(message -> {
                // Log successful emissions
                log.debug("Emitting message: {}", message.getId());
            })
            .doOnError(error -> {
                // Log errors
                log.error("Subscription error for channel {}: {}",
                    channelId, error.getMessage());
            })
            .doOnComplete(() -> {
                // Log completion
                log.info("Subscription completed for channel: {}", channelId);
            });
    }
}
```

---

## Best Practices

### 1. Use Connection Pooling

```java
@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new TextWebSocketHandler() {
            private final Map<String, WebSocketSession> sessions =
                new ConcurrentHashMap<>();

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                sessions.put(session.getId(), session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session,
                    CloseStatus status) {
                sessions.remove(session.getId());
            }
        };
    }
}
```

### 2. Implement Heartbeat

```java
@Component
public class SubscriptionHeartbeat {

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void sendHeartbeat() {
        activeSubscriptions.forEach((id, subscription) -> {
            if (!subscription.isActive()) {
                subscription.close();
                return;
            }

            subscription.sendHeartbeat();
        });
    }
}
```

### 3. Rate Limiting

```java
@Component
public class RateLimitedSubscription {

    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    @SubscriptionMapping
    public Flux<SensorReading> sensorData(@Argument String sensorId) {
        RateLimiter limiter = limiters.computeIfAbsent(sensorId,
            id -> RateLimiter.create(10.0)); // 10 per second

        return sensorService.getStream(sensorId)
            .filter(reading -> limiter.tryAcquire())
            .onBackpressureDrop();
    }
}
```

### 4. Subscription Lifecycle

```java
@Component
public class ManagedSubscription {

    @SubscriptionMapping
    public Flux<Event> subscribeEvents(
            @Argument String eventType,
            @ContextValue GraphQLContext context) {

        return Flux.using(
            () -> {
                String subscriptionId = UUID.randomUUID().toString();
                eventService.registerSubscription(subscriptionId, eventType);

                return new SubscriptionHandle(subscriptionId, eventService);
            },
            handle -> eventService.getEventStream(handle.getEventType()),
            handle -> {
                log.info("Cleaning up subscription: {}", handle.getId());
                eventService.unregisterSubscription(handle.getId());
            }
        );
    }
}
```

### 5. Batch Updates

```java
@SubscriptionMapping
public Flux<List<Message>> messageBatch(
        @Argument String channelId) {

    return messageService.getStream(channelId)
        .bufferTimeout(10, Duration.ofSeconds(1));
}
```

### 6. Security Considerations

```java
@Component
public class SecureSubscription {

    @SubscriptionMapping
    public Flux<Message> messageSent(
            @Argument String channelId,
            @ContextValue GraphQLContext context) {

        User currentUser = context.get("currentUser");

        // Verify access
        if (!channelService.hasAccess(currentUser, channelId)) {
            return Flux.error(new GraphQLException("FORBIDDEN"));
        }

        // Filter sensitive data
        return messageService.getStream(channelId)
            .map(message -> filterSensitiveData(message, currentUser));
    }

    private Message filterSensitiveData(Message message, User user) {
        if (!user.isAdmin()) {
            message.setInternalNotes(null);
            message.setSystemMetadata(null);
        }
        return message;
    }
}
```

---

## Further Reading

- [GraphQL Subscriptions Specification](https://github.com/graphql/graphql-over-ws)
- [Apollo WebSocket Link](https://www.apollographql.com/docs/react/api/link/apollo-link-ws)
- [Spring GraphQL WebSocket](https://docs.spring.io/spring-framework/reference/web/webflux-webgraphql.html)
- [graphql-ws Protocol](https://github.com/enisdenjo/graphql-ws/blob/master/PROTOCOL.md)
