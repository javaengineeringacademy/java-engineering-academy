# gRPC Streams

## Comprehensive Guide to gRPC Streaming Patterns

gRPC supports four streaming patterns that enable efficient bidirectional communication. This guide covers unary, server, client, and bidirectional streaming with practical examples.

---

## Table of Contents

1. [Streaming Overview](#streaming-overview)
2. [Unary RPC](#unary-rpc)
3. [Server Streaming](#server-streaming)
4. [Client Streaming](#client-streaming)
5. [Bidirectional Streaming](#bidirectional-streaming)
6. [Error Handling](#error-handling)
7. [Best Practices](#best-practices)

---

## Streaming Overview

### Comparison

```
┌─────────────────────────────────────────────────────────────┐
│                    gRPC Streaming Patterns                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Unary RPC (Standard Request/Response)                   │
│  Client ────────────────────────────────> Server            │
│  Client <──────────────────────────────── Server            │
│                                                             │
│  2. Server Streaming                                        │
│  Client ────────────────────────────────> Server            │
│  Client <════════════════════════════════ Server            │
│                                                             │
│  3. Client Streaming                                        │
│  Client ════════════════════════════════> Server            │
│  Client <──────────────────────────────── Server            │
│                                                             │
│  4. Bidirectional Streaming                                 │
│  Client ════════════════════════════════> Server            │
│  Client <════════════════════════════════ Server            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Unary RPC

### Definition

```protobuf
syntax = "proto3";

package example;

service UserService {
  // Unary RPC - single request, single response
  rpc GetUser(GetUserRequest) returns (GetUserResponse);
}
```

### Server Implementation

```java
@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUser(GetUserRequest request,
                        StreamObserver<GetUserResponse> responseObserver) {

        try {
            // Process request
            User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new StatusException(
                    Status.NOT_FOUND
                        .withDescription("User not found")));

            // Send response
            responseObserver.onNext(GetUserResponse.newBuilder()
                .setUser(toProto(user))
                .build());
            responseObserver.onCompleted();

        } catch (StatusException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withCause(e)
                .withDescription("Internal error")
                .asRuntimeException());
        }
    }
}
```

### Client Implementation

```java
// Blocking client
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .usePlaintext()
    .build();

UserServiceGrpc.UserServiceBlockingStub blockingStub =
    UserServiceGrpc.newBlockingStub(channel);

try {
    GetUserResponse response = blockingStub.getUser(
        GetUserRequest.newBuilder()
            .setId(123)
            .build());

    System.out.println("User: " + response.getUser().getUsername());

} catch (StatusRuntimeException e) {
    System.err.println("RPC failed: " + e.getStatus());
} finally {
    channel.shutdown();
}
```

---

## Server Streaming

### Definition

```protobuf
service UserService {
  // Server streaming - single request, stream of responses
  rpc ListUsers(ListUsersRequest) returns (stream User);
  rpc SubscribeToChanges(SubscribeRequest) returns (stream UserEvent);
  rpc GetLargeFile(FileRequest) returns (stream FileChunk);
}
```

### Server Implementation

```java
@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void listUsers(ListUsersRequest request,
                          StreamObserver<User> responseObserver) {

        try {
            int pageSize = request.getPageSize() > 0 ?
                request.getPageSize() : 100;
            String pageToken = request.getPageToken();

            List<User> users = userRepository.findAll(
                PageRequest.of(0, pageSize));

            for (User user : users) {
                // Check if client cancelled
                if (responseObserver.isCancelled()) {
                    log.info("Client cancelled stream");
                    return;
                }

                responseObserver.onNext(toProto(user));
            }

            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withCause(e)
                .asRuntimeException());
        }
    }

    @Override
    public void subscribeToChanges(SubscribeRequest request,
                                   StreamObserver<UserEvent> responseObserver) {

        String userId = request.getUserId();

        // Subscribe to real-time changes
        Subscription subscription = eventBus.subscribe(
            "user." + userId + ".changes",
            event -> {
                try {
                    if (!responseObserver.isCancelled()) {
                        responseObserver.onNext(
                            UserEvent.newBuilder()
                                .setUserId(userId)
                                .setEventType(event.getType())
                                .setTimestamp(Timestamps.fromMillis(
                                    event.getTimestamp()))
                                .build());
                    }
                } catch (Exception e) {
                    log.error("Error sending event", e);
                }
            }
        );

        // Cleanup on completion
        responseObserver.setOnCancelHandler(() -> {
            log.info("Client cancelled subscription for user: {}", userId);
            subscription.unsubscribe();
        });
    }

    @Override
    public void getLargeFile(FileRequest request,
                             StreamObserver<FileChunk> responseObserver) {

        try {
            Path filePath = Paths.get(request.getFilePath());

            if (!Files.exists(filePath)) {
                responseObserver.onError(Status.NOT_FOUND
                    .withDescription("File not found")
                    .asRuntimeException());
                return;
            }

            long fileSize = Files.size(filePath);
            int chunkSize = 64 * 1024; // 64KB chunks

            try (InputStream inputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[chunkSize];
                int bytesRead;
                long offset = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    if (responseObserver.isCancelled()) {
                        log.info("Client cancelled file transfer");
                        return;
                    }

                    responseObserver.onNext(FileChunk.newBuilder()
                        .setData(ByteString.copyFrom(buffer, 0, bytesRead))
                        .setOffset(offset)
                        .setFileSize(fileSize)
                        .build());

                    offset += bytesRead;
                }
            }

            responseObserver.onCompleted();

        } catch (IOException e) {
            responseObserver.onError(Status.INTERNAL
                .withCause(e)
                .withDescription("Error reading file")
                .asRuntimeException());
        }
    }
}
```

### Client Implementation

```java
// Server streaming client
UserServiceGrpc.UserServiceStub asyncStub =
    UserServiceGrpc.newStub(channel);

ListUsersRequest request = ListUsersRequest.newBuilder()
    .setPageSize(10)
    .build();

asyncStub.listUsers(request, new StreamObserver<User>() {
    private final List<User> users = new ArrayList<>();

    @Override
    public void onNext(User user) {
        users.add(user);
        System.out.println("Received user: " + user.getUsername());

        // Process each user
        processUser(user);
    }

    @Override
    public void onError(Throwable t) {
        Status status = Status.fromThrowable(t);
        System.err.println("Stream error: " + status);

        switch (status.getCode()) {
            case CANCELLED:
                System.err.println("Stream was cancelled");
                break;
            case INTERNAL:
                System.err.println("Internal error: " +
                    status.getDescription());
                break;
            default:
                System.err.println("Unexpected error");
        }
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed. Total users: " + users.size());
    }
});
```

### File Download Example

```java
// Client file download
FileOutputStream outputStream = new FileOutputStream("downloaded.file");

asyncStub.getLargeFile(
    FileRequest.newBuilder()
        .setFilePath("/path/to/large/file.zip")
        .build(),
    new StreamObserver<FileChunk>() {
        private long bytesReceived = 0;

        @Override
        public void onNext(FileChunk chunk) {
            try {
                byte[] data = chunk.getData().toByteArray();
                outputStream.write(data);
                bytesReceived += data.length;

                // Progress reporting
                long progress = (bytesReceived * 100) / chunk.getFileSize();
                System.out.println("Progress: " + progress + "%");

            } catch (IOException e) {
                onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            try {
                outputStream.close();
                Files.deleteIfExists(Paths.get("downloaded.file"));
            } catch (IOException ignored) {}
            System.err.println("Download failed: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            try {
                outputStream.close();
                System.out.println("Download completed: " + bytesReceived + " bytes");
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }
);
```

---

## Client Streaming

### Definition

```protobuf
service UserService {
  // Client streaming - stream of requests, single response
  rpc UploadUsers(stream CreateUserRequest) returns (UploadUsersResponse);
  rpc ImportData(stream DataChunk) returns (ImportResponse);
  rpc RecordMetrics(stream Metric) returns (RecordMetricsResponse);
}
```

### Server Implementation

```java
@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public StreamObserver<CreateUserRequest> uploadUsers(
            StreamObserver<UploadUsersResponse> responseObserver) {

        return new StreamObserver<>() {
            private final List<Long> createdUserIds = new ArrayList<>();
            private int errorCount = 0;

            @Override
            public void onNext(CreateUserRequest request) {
                try {
                    User user = userService.create(toDomain(request));
                    createdUserIds.add(user.getId());
                    log.info("Created user: {}", user.getId());

                } catch (DuplicateEmailException e) {
                    errorCount++;
                    log.warn("Duplicate email: {}", request.getEmail());

                } catch (ValidationException e) {
                    errorCount++;
                    log.warn("Validation failed for user: {}",
                        request.getUsername());
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Client stream error", t);
                cleanup();
            }

            @Override
            public void onCompleted() {
                // Send final response
                responseObserver.onNext(UploadUsersResponse.newBuilder()
                    .setTotalCreated(createdUserIds.size())
                    .setTotalErrors(errorCount)
                    .addAllCreatedIds(createdUserIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()))
                    .build());
                responseObserver.onCompleted();
            }

            private void cleanup() {
                // Cleanup resources if needed
                createdUserIds.clear();
            }
        };
    }

    @Override
    public StreamObserver<DataChunk> importData(
            StreamObserver<ImportResponse> responseObserver) {

        return new StreamObserver<>() {
            private long totalBytes = 0;
            private int recordCount = 0;

            @Override
            public void onNext(DataChunk chunk) {
                totalBytes += chunk.getData().size();

                // Process chunk
                List<Record> records = parseChunk(chunk);
                for (Record record : records) {
                    try {
                        dataService.importRecord(record);
                        recordCount++;
                    } catch (Exception e) {
                        log.error("Failed to import record", e);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Import stream error", t);
                responseObserver.onError(Status.INTERNAL
                    .withCause(t)
                    .asRuntimeException());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ImportResponse.newBuilder()
                    .setTotalRecords(recordCount)
                    .setTotalBytes(totalBytes)
                    .setSuccess(true)
                    .build());
                responseObserver.onCompleted();
            }
        };
    }
}
```

### Client Implementation

```java
// Client streaming
UserServiceGrpc.UserServiceStub asyncStub =
    UserServiceGrpc.newStub(channel);

StreamObserver<CreateUserRequest> requestObserver =
    asyncStub.uploadUsers(new StreamObserver<UploadUsersResponse>() {
        @Override
        public void onNext(UploadUsersResponse response) {
            System.out.println("Upload completed:");
            System.out.println("  Created: " + response.getTotalCreated());
            System.out.println("  Errors: " + response.getTotalErrors());
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Upload failed: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("Stream completed");
        }
    });

// Send users
for (User user : users) {
    try {
        requestObserver.onNext(CreateUserRequest.newBuilder()
            .setUsername(user.getUsername())
            .setEmail(user.getEmail())
            .build());

        // Rate limiting
        Thread.sleep(10);

    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
    } catch (Exception e) {
        System.err.println("Failed to send user: " + e.getMessage());
    }
}

// Complete the stream
requestObserver.onCompleted();
```

### File Upload Example

```java
// Client file upload
FileInputStream inputStream = new FileInputStream("large-file.zip");

StreamObserver<DataChunk> uploadObserver =
    asyncStub.importData(new StreamObserver<ImportResponse>() {
        @Override
        public void onNext(ImportResponse response) {
            System.out.println("Import completed:");
            System.out.println("  Records: " + response.getTotalRecords());
            System.out.println("  Bytes: " + response.getTotalBytes());
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Import failed: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("Upload stream completed");
        }
    });

byte[] buffer = new byte[64 * 1024]; // 64KB chunks
int bytesRead;
long offset = 0;

while ((bytesRead = inputStream.read(buffer)) != -1) {
    uploadObserver.onNext(DataChunk.newBuilder()
        .setData(ByteString.copyFrom(buffer, 0, bytesRead))
        .setOffset(offset)
        .setFileName("large-file.zip")
        .build());

    offset += bytesRead;

    // Progress
    System.out.printf("\rUpload progress: %.1f%%",
        (offset * 100.0) / file.length());
}

inputStream.close();
uploadObserver.onCompleted();
System.out.println("\nUpload completed");
```

---

## Bidirectional Streaming

### Definition

```protobuf
service UserService {
  // Bidirectional streaming - stream of requests, stream of responses
  rpc Chat(stream ChatMessage) returns (stream ChatMessage);
  rpc RealTimeUpdates(stream ClientCommand) returns (stream ServerEvent);
  rpc CollaborativeEditing(stream EditOperation) returns (stream EditResult);
}
```

### Server Implementation

```java
@GrpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private final ConcurrentHashMap<String, StreamObserver<ChatMessage>>
        activeClients = new ConcurrentHashMap<>();

    @Override
    public StreamObserver<ChatMessage> chat(
            StreamObserver<ChatMessage> responseObserver) {

        return new StreamObserver<>() {
            private String clientId;

            @Override
            public void onNext(ChatMessage message) {
                clientId = message.getSenderId();

                // Register client
                activeClients.put(clientId, responseObserver);

                // Broadcast to other clients
                broadcastToOthers(message, clientId);

                // Send acknowledgment
                responseObserver.onNext(ChatMessage.newBuilder()
                    .setSenderId("server")
                    .setContent("Message received")
                    .setTimestamp(Timestamps.fromMillis(
                        System.currentTimeMillis()))
                    .build());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Client {} error: {}",
                    clientId, t.getMessage());
                activeClients.remove(clientId);
            }

            @Override
            public void onCompleted() {
                log.info("Client {} disconnected", clientId);
                activeClients.remove(clientId);
                responseObserver.onCompleted();
            }
        };
    }

    private void broadcastToOthers(ChatMessage message, String senderId) {
        activeClients.forEach((id, observer) -> {
            if (!id.equals(senderId)) {
                try {
                    observer.onNext(message);
                } catch (Exception e) {
                    log.error("Failed to send to client {}: {}", id,
                        e.getMessage());
                    activeClients.remove(id);
                }
            }
        });
    }
}
```

### Server with Async Processing

```java
@GrpcService
public class RealTimeServiceImpl extends RealTimeServiceGrpc.RealTimeServiceImplBase {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Sinks.Many<ServerEvent> eventSink = Sinks.many()
        .multicast()
        .onBackpressureBuffer();

    @Override
    public StreamObserver<ClientCommand> realTimeUpdates(
            StreamObserver<ServerEvent> responseObserver) {

        return new StreamObserver<>() {
            @Override
            public void onNext(ClientCommand command) {
                // Process command asynchronously
                executor.submit(() -> {
                    try {
                        ServerEvent event = processCommand(command);
                        eventSink.tryEmitNext(event);
                    } catch (Exception e) {
                        log.error("Command processing failed", e);
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                log.error("Client error", t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
```

### Client Implementation

```java
// Bidirectional streaming client
UserServiceGrpc.UserServiceStub asyncStub =
    UserServiceGrpc.newStub(channel);

StreamObserver<ChatMessage> requestObserver =
    asyncStub.chat(new StreamObserver<>() {
        @Override
        public void onNext(ChatMessage message) {
            System.out.printf("[%s] %s: %s%n",
                formatTimestamp(message.getTimestamp()),
                message.getSenderId(),
                message.getContent());
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Chat error: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("Chat ended");
        }
    });

// Send messages from stdin
Scanner scanner = new Scanner(System.in);
while (scanner.hasNextLine()) {
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("quit")) {
        break;
    }

    requestObserver.onNext(ChatMessage.newBuilder()
        .setSenderId(currentUserId)
        .setContent(input)
        .setTimestamp(Timestamps.fromMillis(
            System.currentTimeMillis()))
        .build());
}

requestObserver.onCompleted();
```

### Real-Time Updates Client

```java
// Real-time updates with heartbeat
StreamObserver<ClientCommand> commandObserver =
    asyncStub.realTimeUpdates(new StreamObserver<>() {
        @Override
        public void onNext(ServerEvent event) {
            switch (event.getEventType()) {
                case DATA_UPDATE:
                    handleDataUpdate(event);
                    break;
                case NOTIFICATION:
                    handleNotification(event);
                    break;
                case HEARTBEAT:
                    // Reset heartbeat timer
                    resetHeartbeatTimer();
                    break;
            }
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Connection lost: " + t.getMessage());
            scheduleReconnect();
        }

        @Override
        public void onCompleted() {
            System.out.println("Updates ended");
        }
    });

// Send periodic heartbeats
ScheduledExecutorService scheduler =
    Executors.newSingleThreadScheduledExecutor();

scheduler.scheduleAtFixedRate(() -> {
    try {
        commandObserver.onNext(ClientCommand.newBuilder()
            .setCommandType(ClientCommand.CommandType.HEARTBEAT)
            .build());
    } catch (Exception e) {
        log.error("Heartbeat failed", e);
    }
}, 0, 30, TimeUnit.SECONDS);
```

---

## Error Handling

### Server Error Handling

```java
@GrpcService
public class RobustServiceImpl extends RobustServiceGrpc.RobustServiceImplBase {

    @Override
    public StreamObserver<ClientCommand> robustStream(
            StreamObserver<ServerEvent> responseObserver) {

        return new StreamObserver<>() {
            @Override
            public void onNext(ClientCommand command) {
                try {
                    ServerEvent event = processCommand(command);
                    responseObserver.onNext(event);
                } catch (ValidationException e) {
                    // Client error - send error event
                    responseObserver.onNext(ServerEvent.newBuilder()
                        .setEventType(ServerEvent.EventType.ERROR)
                        .setErrorMessage(e.getMessage())
                        .build());
                } catch (Exception e) {
                    // Server error - close stream
                    responseObserver.onError(Status.INTERNAL
                        .withCause(e)
                        .withDescription("Internal processing error")
                        .asRuntimeException());
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Stream error", t);
                // Cleanup resources
                cleanup();
            }

            @Override
            public void onCompleted() {
                log.info("Stream completed normally");
                cleanup();
            }
        };
    }
}
```

### Client Error Handling with Retry

```java
public class ResilientStreamingClient {

    private final int maxRetries = 3;
    private final Duration retryDelay = Duration.ofSeconds(1);

    public void connectWithRetry(String serverAddress, int port) {
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(serverAddress, port)
                    .usePlaintext()
                    .build();

                connectAndStream(channel);
                return; // Success

            } catch (Exception e) {
                attempt++;
                log.warn("Connection attempt {} failed: {}",
                    attempt, e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryDelay.toMillis() * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        throw new RuntimeException("Failed to connect after " +
            maxRetries + " attempts");
    }

    private void connectAndStream(ManagedChannel channel) {
        // ... streaming logic
    }
}
```

---

## Best Practices

### 1. Use Deadlines

```java
// Client with deadline
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .usePlaintext()
    .build();

UserServiceGrpc.UserServiceStub stub = UserServiceGrpc.newStub(channel);

// Set deadline for the entire streaming session
stub.withDeadlineAfter(60, TimeUnit.SECONDS)
    .chat(new StreamObserver<>() {
        // ...
    });
```

### 2. Implement Flow Control

```java
// Server with flow control
@Override
public void listUsers(ListUsersRequest request,
                      StreamObserver<User> responseObserver) {

    // Check if client can handle more data
    if (responseObserver.isCancelled()) {
        return;
    }

    // Use backpressure-aware processing
    Flux.fromIterable(users)
        .onBackpressureDrop(user -> {
            log.warn("Dropping user due to backpressure: {}",
                user.getId());
        })
        .subscribe(user -> {
            responseObserver.onNext(toProto(user));
        });
}
```

### 3. Handle Partial Failures

```java
@Override
public StreamObserver<CreateUserRequest> uploadUsers(
        StreamObserver<UploadUsersResponse> responseObserver) {

    return new StreamObserver<>() {
        private int successCount = 0;
        private int failureCount = 0;

        @Override
        public void onNext(CreateUserRequest request) {
            try {
                User user = userService.create(toDomain(request));
                successCount++;
            } catch (DuplicateEmailException e) {
                failureCount++;
                log.warn("Duplicate email: {}", request.getEmail());
                // Don't fail the stream, just count the error
            }
        }

        @Override
        public void onError(Throwable t) {
            log.error("Stream error", t);
            // Report partial success
            responseObserver.onNext(UploadUsersResponse.newBuilder()
                .setTotalCreated(successCount)
                .setTotalErrors(failureCount)
                .build());
            responseObserver.onCompleted();
        }

        @Override
        public void onCompleted() {
            responseObserver.onNext(UploadUsersResponse.newBuilder()
                .setTotalCreated(successCount)
                .setTotalErrors(failureCount)
                .build());
            responseObserver.onCompleted();
        }
    };
}
```

### 4. Use Context for Cancellation

```java
@Override
public void listUsers(ListUsersRequest request,
                      StreamObserver<User> responseObserver) {

    Context.current().addListener(() -> {
        log.info("Client cancelled stream");
        // Cleanup resources
        cancelOngoingOperations();
    }, MoreExecutors.directExecutor());

    for (User user : users) {
        // Check cancellation before each iteration
        if (Context.current().isCancelled()) {
            log.info("Stream cancelled by client");
            return;
        }

        responseObserver.onNext(toProto(user));
    }

    responseObserver.onCompleted();
}
```

### 5. Implement Heartbeats

```java
@Override
public StreamObserver<ClientCommand> realTimeUpdates(
        StreamObserver<ServerEvent> responseObserver) {

    // Start heartbeat
    ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();

    scheduler.scheduleAtFixedRate(() -> {
        try {
            if (!responseObserver.isCancelled()) {
                responseObserver.onNext(ServerEvent.newBuilder()
                    .setEventType(ServerEvent.EventType.HEARTBEAT)
                    .setTimestamp(Timestamps.fromMillis(
                        System.currentTimeMillis()))
                    .build());
            }
        } catch (Exception e) {
            log.error("Heartbeat failed", e);
            scheduler.shutdown();
        }
    }, 0, 30, TimeUnit.SECONDS);

    return new StreamObserver<>() {
        // ... handle incoming commands
    };
}
```

---

## Further Reading

- [gRPC Streaming Documentation](https://grpc.io/docs/what-is-grpc/core-concepts/#streaming-rpc)
- [gRPC Java Streaming](https://grpc.github.io/grpc-java/javadoc/)
- [Load Balancing for gRPC](https://grpc.io/docs/guides/load-balancing/)
- [gRPC Error Handling](https://grpc.io/docs/guides/status-codes/)
