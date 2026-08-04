# gRPC Fundamentals

## Comprehensive Guide to gRPC Communication

gRPC is a high-performance, open-source RPC framework originally developed by Google. This guide covers gRPC basics, protocol buffers, communication patterns, and best practices.

---

## Table of Contents

1. [gRPC Overview](#grpc-overview)
2. [Protocol Buffers](#protocol-buffers)
3. [Service Definition](#service-definition)
4. [Communication Patterns](#communication-patterns)
5. [Error Handling](#error-handling)
6. [Authentication](#authentication)
7. [Best Practices](#best-practices)

---

## gRPC Overview

### What is gRPC?

```
┌─────────────────────────────────────────────────────────────┐
│                      gRPC Architecture                       │
├─────────────────────────────────────────────────────────────┤
│  Client                                                     │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Generated Client Stub                              │    │
│  │  - Type-safe method calls                           │    │
│  │  - Automatic serialization                          │    │
│  │  - Channel management                               │    │
│  └─────────────────────┬───────────────────────────────┘    │
│                        │                                    │
│                        ▼                                    │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  gRPC Channel                                       │    │
│  │  - HTTP/2 multiplexing                              │    │
│  │  - TLS encryption                                   │    │
│  │  - Connection pooling                               │    │
│  └─────────────────────┬───────────────────────────────┘    │
│                        │                                    │
│  ──────────────────────┼──────────────────────────────────  │
│                        │ Network                            │
│  ──────────────────────┼──────────────────────────────────  │
│                        │                                    │
│                        ▼                                    │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  gRPC Server                                        │    │
│  │  - Request handling                                 │    │
│  │  - Load balancing                                   │    │
│  │  - Interceptors                                     │    │
│  └─────────────────────┬───────────────────────────────┘    │
│                        │                                    │
│                        ▼                                    │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Service Implementation                             │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### gRPC vs REST Comparison

| Feature | gRPC | REST |
|---------|------|------|
| Protocol | HTTP/2 | HTTP/1.1 or HTTP/2 |
| Format | Protocol Buffers | JSON/XML |
| Contract | .proto files | OpenAPI/Swagger |
| Streaming | Full duplex | Request/Response |
| Code Generation | Built-in | Manual/Tooling |
| Browser Support | Limited | Native |
| Performance | High | Moderate |
| Type Safety | Strong | Weak |
| Load Balancing | Client-side | Server-side |

---

## Protocol Buffers

### Proto3 Syntax

```protobuf
syntax = "proto3";

package userservice;

option java_package = "com.example.userservice";
option java_outer_classname = "UserProto";
option java_multiple_files = true;
```

### Message Definition

```protobuf
// Basic message types
message User {
  int64 id = 1;
  string username = 2;
  string email = 3;
  UserProfile profile = 4;
  repeated string roles = 5;
  map<string, string> metadata = 6;
  google.protobuf.Timestamp created_at = 7;
  google.protobuf.Timestamp updated_at = 8;
}

message UserProfile {
  string first_name = 1;
  string last_name = 2;
  string bio = 3;
  int32 age = 4;
  repeated string interests = 5;
}

// Request/Response messages
message GetUserRequest {
  int64 id = 1;
}

message GetUserResponse {
  User user = 1;
  bool found = 2;
}

message CreateUserRequest {
  string username = 1;
  string email = 2;
  string password = 3;
  UserProfile profile = 4;
}

message CreateUserResponse {
  User user = 1;
  string token = 2;
}

message ListUsersRequest {
  int32 page_size = 1;
  string page_token = 2;
  string filter = 3;
}

message ListUsersResponse {
  repeated User users = 1;
  string next_page_token = 2;
  int32 total_count = 3;
}

// Error response
message ErrorResponse {
  ErrorCode code = 1;
  string message = 2;
  map<string, string> details = 3;
}

enum ErrorCode {
  ERROR_CODE_UNSPECIFIED = 0;
  ERROR_CODE_NOT_FOUND = 1;
  ERROR_CODE_ALREADY_EXISTS = 2;
  ERROR_CODE_INVALID_ARGUMENT = 3;
  ERROR_CODE_UNAUTHENTICATED = 4;
  ERROR_CODE_PERMISSION_DENIED = 5;
}
```

### Enums

```protobuf
enum UserRole {
  USER_ROLE_UNSPECIFIED = 0;
  USER_ROLE_ADMIN = 1;
  USER_ROLE_MODERATOR = 2;
  USER_ROLE_USER = 3;
  USER_ROLE_GUEST = 4;
}

enum OrderStatus {
  ORDER_STATUS_UNSPECIFIED = 0;
  ORDER_STATUS_PENDING = 1;
  ORDER_STATUS_PROCESSING = 2;
  ORDER_STATUS_SHIPPED = 3;
  ORDER_STATUS_DELIVERED = 4;
  ORDER_STATUS_CANCELLED = 5;
}
```

### Well-Known Types

```protobuf
import "google/protobuf/timestamp.proto";
import "google/protobuf/duration.proto";
import "google/protobuf/struct.proto";
import "google/protobuf/empty.proto";
import "google/protobuf/field_mask.proto";

message Event {
  string id = 1;
  string title = 2;
  google.protobuf.Timestamp start_time = 3;
  google.protobuf.Timestamp end_time = 4;
  google.protobuf.Duration duration = 5;
  google.protobuf.Struct metadata = 6;
  google.protobuf.FieldMask update_mask = 7;
}

service EventService {
  rpc GetEvent(google.protobuf.Empty) returns (Event);
  rpc UpdateEvent(UpdateEventRequest) returns (Event);
}

message UpdateEventRequest {
  Event event = 1;
  google.protobuf.FieldMask update_mask = 2;
}
```

### Oneof and Any

```protobuf
// Oneof - exactly one field must be set
message Notification {
  string id = 1;
  oneof payload {
    EmailNotification email = 2;
    SMSNotification sms = 3;
    PushNotification push = 4;
    InAppNotification in_app = 5;
  }
  google.protobuf.Timestamp created_at = 6;
}

message EmailNotification {
  string subject = 1;
  string body = 2;
  repeated string recipients = 3;
}

message SMSNotification {
  string phone_number = 1;
  string message = 2;
}

message PushNotification {
  string device_token = 1;
  string title = 2;
  string body = 3;
  map<string, string> data = 4;
}

// Any - dynamic message type
import "google/protobuf/any.proto";

message Wrapper {
  string id = 1;
  google.protobuf.Any details = 2;
  google.protobuf.Timestamp timestamp = 3;
}
```

---

## Service Definition

### Service Definition

```protobuf
syntax = "proto3";

package userservice;

service UserService {
  // Unary RPC
  rpc GetUser(GetUserRequest) returns (GetUserResponse);

  // Server streaming
  rpc ListUsers(ListUsersRequest) returns (stream User);

  // Client streaming
  rpc UploadUsers(stream CreateUserRequest) returns (UploadUsersResponse);

  // Bidirectional streaming
  rpc Chat(stream ChatMessage) returns (stream ChatMessage);

  // Server streaming for real-time updates
  rpc SubscribeToUserUpdates(SubscribeRequest) returns (stream UserUpdate);
}

message ChatMessage {
  string id = 1;
  string sender_id = 2;
  string content = 3;
  google.protobuf.Timestamp timestamp = 4;
}
```

### Java Implementation

```java
// Service implementation
@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void getUser(GetUserRequest request,
                        StreamObserver<GetUserResponse> responseObserver) {

        try {
            User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new StatusException(
                    Status.NOT_FOUND
                        .withDescription("User not found")
                        .augmentDetails(StatusDetails.newBuilder()
                            .addDetails(com.google.protobuf.Any.pack(
                                ErrorDetail.newBuilder()
                                    .setCode("USER_NOT_FOUND")
                                    .setMessage("User with id " + request.getId() + " not found")
                                    .build()))
                            .build())
                ));

            responseObserver.onNext(GetUserResponse.newBuilder()
                .setUser(toProtoUser(user))
                .setFound(true)
                .build());
            responseObserver.onCompleted();
        } catch (StatusException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void listUsers(ListUsersRequest request,
                          StreamObserver<User> responseObserver) {

        Page<User> users = userRepository.findAll(
            PageRequest.of(0, request.getPageSize()));

        for (User user : users) {
            responseObserver.onNext(toProtoUser(user));
        }
        responseObserver.onCompleted();
    }
}

// Server startup
@GrpcSpringBootApplication
public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
            .addService(new UserServiceImpl())
            .intercept(new AuthInterceptor())
            .build()
            .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
```

### Client Implementation

```java
// Channel creation
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .usePlaintext() // For testing
    .intercept(new ClientAuthInterceptor())
    .build();

UserServiceGrpc.UserServiceBlockingStub blockingStub =
    UserServiceGrpc.newBlockingStub(channel);

UserServiceGrpc.UserServiceStub asyncStub =
    UserServiceGrpc.newStub(channel);

// Unary call
try {
    GetUserResponse response = blockingStub.getUser(
        GetUserRequest.newBuilder().setId(1L).build());

    System.out.println("User: " + response.getUser().getUsername());
} catch (StatusRuntimeException e) {
    System.err.println("RPC failed: " + e.getStatus());
}

// Server streaming
ListUsersRequest request = ListUsersRequest.newBuilder()
    .setPageSize(10)
    .build();

asyncStub.listUsers(request, new StreamObserver<User>() {
    @Override
    public void onNext(User user) {
        System.out.println("User: " + user.getUsername());
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Error: " + t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
    }
});
```

---

## Communication Patterns

### Unary RPC

```protobuf
// Simple request-response
rpc GetUser(GetUserRequest) returns (GetUserResponse);

// Java implementation
@Override
public void getUser(GetUserRequest request,
                    StreamObserver<GetUserResponse> responseObserver) {
    User user = userService.findById(request.getId());
    responseObserver.onNext(GetUserResponse.newBuilder()
        .setUser(toProto(user))
        .build());
    responseObserver.onCompleted();
}
```

### Server Streaming

```protobuf
// Server sends stream of responses
rpc ListUsers(ListUsersRequest) returns (stream User);

// Java implementation
@Override
public void listUsers(ListUsersRequest request,
                      StreamObserver<User> responseObserver) {
    List<User> users = userService.findAll(request.getFilter());

    for (User user : users) {
        responseObserver.onNext(toProto(user));
    }
    responseObserver.onCompleted();
}
```

### Client Streaming

```protobuf
// Client sends stream of requests
rpc UploadUsers(stream CreateUserRequest) returns (UploadUsersResponse);

// Java implementation
@Override
public StreamObserver<CreateUserRequest> uploadUsers(
        StreamObserver<UploadUsersResponse> responseObserver) {

    return new StreamObserver<>() {
        private int count = 0;
        private List<String> createdIds = new ArrayList<>();

        @Override
        public void onNext(CreateUserRequest request) {
            try {
                User user = userService.create(toDomain(request));
                createdIds.add(user.getId().toString());
                count++;
            } catch (Exception e) {
                // Log error but continue
            }
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Upload failed: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            responseObserver.onNext(UploadUsersResponse.newBuilder()
                .setCount(count)
                .addAllCreatedIds(createdIds)
                .build());
            responseObserver.onCompleted();
        }
    };
}
```

### Bidirectional Streaming

```protobuf
// Both client and server send streams
rpc Chat(stream ChatMessage) returns (stream ChatMessage);

// Java implementation
@Override
public StreamObserver<ChatMessage> chat(
        StreamObserver<ChatMessage> responseObserver) {

    return new StreamObserver<>() {
        @Override
        public void onNext(ChatMessage message) {
            // Broadcast to all connected clients
            chatRoom.broadcast(message);

            // Send acknowledgment
            responseObserver.onNext(ChatMessage.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSenderId("server")
                .setContent("Received: " + message.getContent())
                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                .build());
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Chat error: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
        }
    };
}
```

---

## Error Handling

### Status Codes

```java
// Status codes
public class GrpcErrorHandler implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
            next.startCall(call, headers)) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (StatusRuntimeException e) {
                    call.close(e.getStatus(), new Metadata());
                } catch (Exception e) {
                    call.close(Status.INTERNAL
                        .withCause(e)
                        .withDescription("Internal server error"),
                        new Metadata());
                }
            }
        };
    }
}
```

### Custom Error Details

```protobuf
import "google/rpc/error_details.proto";

message ErrorDetail {
  string code = 1;
  string message = 2;
  repeated ErrorDetail causes = 3;
}
```

```java
// Throwing errors with details
throw new StatusException(
    Status.INVALID_ARGUMENT
        .withDescription("Invalid email format")
        .augmentDetails(StatusDetails.newBuilder()
            .addDetails(com.google.protobuf.Any.pack(
                ErrorDetail.newBuilder()
                    .setCode("INVALID_EMAIL")
                    .setMessage("Email must contain @ symbol")
                    .build()))
            .build())
);
```

### Client Error Handling

```java
try {
    GetUserResponse response = blockingStub.getUser(request);
    // Process response
} catch (StatusRuntimeException e) {
    switch (e.getStatus().getCode()) {
        case NOT_FOUND:
            System.err.println("User not found");
            break;
        case INVALID_ARGUMENT:
            System.err.println("Invalid request: " +
                Status.fromThrowable(e).getDescription());
            break;
        case UNAUTHENTICATED:
            System.err.println("Authentication required");
            break;
        case PERMISSION_DENIED:
            System.err.println("Insufficient permissions");
            break;
        default:
            System.err.println("RPC failed: " + e.getStatus());
    }
}
```

---

## Authentication

### SSL/TLS Configuration

```java
// Server with TLS
Server server = ServerBuilder.forPort(50051)
    .useTransportSecurity()
    .addService(new UserServiceImpl())
    .build();

// Client with TLS
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .useTransportSecurity()
    .build();
```

### Token-Based Authentication

```java
// Server interceptor
public class AuthInterceptor implements ServerInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String token = headers.get(Metadata.Key.of(
            "authorization", Metadata.ASCII_STRING_MARSHALLER));

        if (token == null || !tokenValidator.validate(token)) {
            call.close(Status.UNAUTHENTICATED
                .withDescription("Invalid token"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        // Extract user from token
        User user = tokenValidator.extractUser(token);

        // Propagate context
        Context context = Context.current()
            .withValue(ContextKeys.USER_CONTEXT, user);

        return Contexts.interceptCall(context, call, headers, next);
    }
}
```

### Client Authentication

```java
// Add token to metadata
Metadata headers = new Metadata();
headers.put(Metadata.Key.of(
    "authorization",
    Metadata.ASCII_STRING_MARSHALLER),
    "Bearer " + token);

blockingStub.getUser(request, headers);
```

---

## Best Practices

### 1. Use Deadline Propagation

```java
// Server
@Override
public void getUser(GetUserRequest request,
                    StreamObserver<GetUserResponse> responseObserver) {

    // Get deadline from context
    Context context = Context.current();
    long deadlineMillis = context.getDeadline().timeRemaining(TimeUnit.MILLISECONDS);

    if (deadlineMillis <= 0) {
        responseObserver.onError(Status.DEADLINE_EXCEEDED
            .asRuntimeException());
        return;
    }

    // Process with deadline awareness
    // ...
}

// Client with deadline
GetUserResponse response = blockingStub
    .withDeadlineAfter(5, TimeUnit.SECONDS)
    .getUser(request);
```

### 2. Use Load Balancing

```java
// Client-side load balancing
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .defaultLoadBalancingPolicy("round-robin")
    .build();

// Or with service discovery
ManagedChannel channel = ManagedChannelBuilder
    .forTarget("dns:///user-service:50051")
    .defaultLoadBalancingPolicy("pick_first")
    .build();
```

### 3. Implement Health Checking

```protobuf
import "grpc/health/v1/health.proto";

service HealthService {
  rpc Check(HealthCheckRequest) returns (HealthCheckResponse);
  rpc Watch(HealthCheckRequest) returns (stream HealthCheckResponse);
}
```

```java
@GrpcService
public class HealthServiceImpl extends HealthGrpc.HealthImplBase {

    @Override
    public void check(HealthCheckRequest request,
                      StreamObserver<HealthCheckResponse> responseObserver) {
        responseObserver.onNext(HealthCheckResponse.newBuilder()
            .setStatus(HealthCheckResponse.ServingStatus.SERVING)
            .build());
        responseObserver.onCompleted();
    }
}
```

### 4. Use Interceptors

```java
// Logging interceptor
public class LoggingInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        long start = System.currentTimeMillis();
        String methodName = call.getMethodDescriptor().getFullMethodName();

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
            next.startCall(call, headers)) {

            @Override
            public void onHalfClose() {
                super.onHalfClose();
                log.info("Method {} called", methodName);
            }

            @Override
            public void onComplete() {
                long duration = System.currentTimeMillis() - start;
                log.info("Method {} completed in {}ms", methodName, duration);
            }
        };
    }
}
```

### 5. Implement Retry Logic

```java
// Client with retry
UserServiceGrpc.UserServiceStub stub = UserServiceGrpc.newStub(channel);

stub.withInterceptors(
    new RetryInterceptor(3, Duration.ofSeconds(1))
).getUser(request, responseObserver);
```

---

## Further Reading

- [gRPC Official Documentation](https://grpc.io/docs/)
- [Protocol Buffers Language Guide](https://developers.google.com/protocol-buffers/docs/proto3)
- [gRPC Java](https://grpc.github.io/grpc-java/)
- [gRPC Best Practices](https://grpc.io/docs/guides/best-practices/)
