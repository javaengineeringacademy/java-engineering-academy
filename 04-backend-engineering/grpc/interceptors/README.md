# gRPC Interceptors

## Comprehensive Guide to gRPC Interceptors

Interceptors in gRPC allow you to intercept and modify RPC calls on both client and server sides. This guide covers interceptor patterns, metadata handling, and error handling.

---

## Table of Contents

1. [Interceptor Overview](#interceptor-overview)
2. [Server Interceptors](#server-interceptors)
3. [Client Interceptors](#client-interceptors)
4. [Metadata Handling](#metadata-handling)
5. [Error Handling](#error-handling)
6. [Common Interceptor Patterns](#common-interceptor-patterns)
7. [Best Practices](#best-practices)

---

## Interceptor Overview

### Interceptor Chain

```
Client Request                           Server Request
      |                                        |
      v                                        v
+------------------+                   +------------------+
| Client           |                   | Server           |
| Interceptor 1    |                   | Interceptor 1    |
+--------+---------+                   +--------+---------+
         |                                       |
         v                                       v
+------------------+                   +------------------+
| Client           |                   | Server           |
| Interceptor 2    |                   | Interceptor 2    |
+--------+---------+                   +--------+---------+
         |                                       |
         v                                       v
+------------------+                   +------------------+
| Server           |                   | Application      |
| Interceptor      |                   | Logic            |
+------------------+                   +------------------+
```

---

## Server Interceptors

### Basic Server Interceptor

```java
public class LoggingInterceptor implements ServerInterceptor {

    private static final Logger log =
        LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        long startTime = System.currentTimeMillis();

        log.info("Starting call: {}", methodName);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
            next.startCall(call, headers)) {

            @Override
            public void onMessage(ReqT message) {
                log.debug("Received message for {}: {}",
                    methodName, truncate(message.toString()));
                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
                super.onHalfClose();
            }

            @Override
            public void onComplete() {
                long duration = System.currentTimeMillis() - startTime;
                log.info("Completed call: {} in {}ms",
                    methodName, duration);
                super.onComplete();
            }

            @Override
            public void onCancel() {
                long duration = System.currentTimeMillis() - startTime;
                log.warn("Cancelled call: {} after {}ms",
                    methodName, duration);
                super.onCancel();
            }

            private String truncate(String s) {
                return s.length() > 100 ? s.substring(0, 100) + "..." : s;
            }
        };
    }
}
```

### Authentication Interceptor

```java
public class AuthInterceptor implements ServerInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        if (isHealthCheck(call)) {
            return next.startCall(call, headers);
        }

        String token = extractToken(headers);

        if (token == null) {
            call.close(Status.UNAUTHENTICATED
                .withDescription("Missing authentication token"),
                new Metadata());
            return new NoopListener<>();
        }

        try {
            AuthToken authToken = tokenValidator.validate(token);
            UserContext userContext = userContextMapper.mapToContext(authToken);

            Context context = Context.current()
                .withValue(ContextKeys.USER_CONTEXT, userContext);

            return Contexts.interceptCall(context, call, headers, next);

        } catch (InvalidTokenException e) {
            call.close(Status.UNAUTHENTICATED
                .withDescription("Invalid token"), new Metadata());
            return new NoopListener<>();
        }
    }

    private String extractToken(Metadata headers) {
        String authHeader = headers.get(Metadata.Key.of(
            "authorization", Metadata.ASCII_STRING_MARSHALLER));
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
```

### Authorization Interceptor

```java
public class AuthorizationInterceptor implements ServerInterceptor {

    private final PermissionChecker permissionChecker;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        UserContext user = Context.current().get(ContextKeys.USER_CONTEXT);
        if (user == null) {
            return next.startCall(call, headers);
        }

        String methodName = call.getMethodDescriptor().getFullMethodName();
        Permission required = getRequiredPermission(methodName);

        if (required != null && !permissionChecker.hasPermission(user, required)) {
            call.close(Status.PERMISSION_DENIED
                .withDescription("Insufficient permissions"), new Metadata());
            return new NoopListener<>();
        }

        return next.startCall(call, headers);
    }
}
```

### Rate Limiting Interceptor

```java
public class RateLimitInterceptor implements ServerInterceptor {

    private final RateLimiter rateLimiter;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        if (!rateLimiter.tryAcquire()) {
            call.close(Status.RESOURCE_EXHAUSTED
                .withDescription("Rate limit exceeded"), new Metadata());
            return new NoopListener<>();
        }

        return next.startCall(call, headers);
    }
}
```

---

## Client Interceptors

### Basic Client Interceptor

```java
public class ClientLoggingInterceptor implements ClientInterceptor {

    private static final Logger log =
        LoggerFactory.getLogger(ClientLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall.Listener<ReqT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions options,
            Channel next) {

        String methodName = method.getFullMethodName();
        long startTime = System.currentTimeMillis();

        return new ForwardingClientCallListener.SimpleForwardingClientCallListener<>(
            next.newCall(method, options).start()) {

            @Override
            public void onMessage(RespT message) {
                log.debug("Received response for {}", methodName);
                super.onMessage(message);
            }

            @Override
            public void onClose(Status status, Metadata trailers) {
                long duration = System.currentTimeMillis() - startTime;
                log.debug("Completed call: {} in {}ms, status: {}",
                    methodName, duration, status.getCode());
                super.onClose(status, trailers);
            }
        };
    }
}
```

### Authentication Client Interceptor

```java
public class ClientAuthInterceptor implements ClientInterceptor {

    private final Supplier<String> tokenProvider;

    @Override
    public <ReqT, RespT> ClientCall.Listener<ReqT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions options,
            Channel next) {

        String token = tokenProvider.get();

        options = options.withCallCredentials(new CallCredentials() {
            @Override
            public void applyRequestMetadata(
                    RequestInfo requestInfo,
                    Executor appExecutor,
                    MetadataApplier applier) {
                appExecutor.execute(() -> {
                    Metadata headers = new Metadata();
                    headers.put(Metadata.Key.of("authorization",
                        Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);
                    applier.apply(headers);
                });
            }

            @Override
            public void thisUsesUnstableApi() {}
        });

        return next.newCall(method, options).start();
    }
}
```

### Retry Client Interceptor

```java
public class RetryInterceptor implements ClientInterceptor {

    private final int maxRetries;
    private final long initialBackoffMs;

    @Override
    public <ReqT, RespT> ClientCall.Listener<ReqT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions options,
            Channel next) {

        return new RetryingCallListener<ReqT, RespT>(
            method, options, next, maxRetries, initialBackoffMs);
    }

    private class RetryingCallListener<ReqT, RespT> extends
            ForwardingClientCallListener.SimpleForwardingClientCallListener<ReqT> {

        private final MethodDescriptor<ReqT, RespT> method;
        private final CallOptions options;
        private final Channel next;
        private final int maxRetries;
        private final long initialBackoffMs;
        private int retryCount = 0;

        RetryingCallListener(MethodDescriptor<ReqT, RespT> method,
                CallOptions options, Channel next,
                int maxRetries, long initialBackoffMs) {
            super(next.newCall(method, options).start());
            this.method = method;
            this.options = options;
            this.next = next;
            this.maxRetries = maxRetries;
            this.initialBackoffMs = initialBackoffMs;
        }

        @Override
        public void onClose(Status status, Metadata trailers) {
            if (status.isOk() || retryCount >= maxRetries ||
                !isRetryable(status.getCode())) {
                super.onClose(status, trailers);
                return;
            }

            retryCount++;
            long backoff = initialBackoffMs * retryCount;

            try {
                Thread.sleep(backoff);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                super.onClose(status, trailers);
                return;
            }

            next.newCall(method, options).start(this);
        }

        private boolean isRetryable(Status.Code code) {
            return code == Status.Code.UNAVAILABLE ||
                   code == Status.Code.DEADLINE_EXCEEDED ||
                   code == Status.Code.RESOURCE_EXHAUSTED;
        }
    }
}
```

---

## Metadata Handling

### Reading Metadata

```java
@Override
public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

    String requestId = headers.get(Metadata.Key.of(
        "x-request-id", Metadata.ASCII_STRING_MARSHALLER));

    byte[] traceId = headers.get(Metadata.Key.of(
        "x-trace-id", Metadata.BINARY_BYTE_MARSHALLER));

    String userId = headers.getOrDefault(
        Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER),
        "anonymous");

    log.debug("Request ID: {}, User ID: {}", requestId, userId);

    return next.startCall(call, headers);
}
```

### Writing Metadata

```java
@Override
public void getUser(GetUserRequest request,
                    StreamObserver<GetUserResponse> responseObserver) {

    Metadata trailers = new Metadata();
    trailers.put(Metadata.Key.of("x-response-time",
        Metadata.ASCII_STRING_MARSHALLER), "150ms");

    responseObserver.onNext(GetUserResponse.newBuilder()
        .setUser(user)
        .build());
    responseObserver.onCompleted();
}
```

### Custom Metadata Keys

```java
public class MetadataKeys {
    public static final Metadata.Key<String> REQUEST_ID =
        Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> CORRELATION_ID =
        Metadata.Key.of("x-correlation-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> AUTHORIZATION =
        Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<byte[]> TRACE_ID =
        Metadata.Key.of("x-trace-id-bin", Metadata.BINARY_BYTE_MARSHALLER);

    public static final Metadata.Key<Integer> RETRY_COUNT =
        Metadata.Key.of("x-retry-count", Metadata.INTEGER_MARSHALLER);
}
```

---

## Error Handling

### Server Error Handling

```java
@Override
public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
        next.startCall(call, headers)) {

        @Override
        public void onComplete() {
            super.onComplete();
        }

        @Override
        public void onCancel() {
            log.warn("Client cancelled call: {}",
                call.getMethodDescriptor().getFullMethodName());
            super.onCancel();
        }
    };
}
```

### Error with Details

```java
Metadata trailers = new Metadata();
ErrorDetail detail = ErrorDetail.newBuilder()
    .setCode("INVALID_ARGUMENT")
    .setMessage("Email format is invalid")
    .build();

trailers.put(Metadata.Key.of("error-details-bin",
    Metadata.BINARY_BYTE_MARSHALLER),
    detail.toByteArray());

call.close(Status.INVALID_ARGUMENT
    .withDescription("Invalid request"), trailers);
```

---

## Common Interceptor Patterns

### Tracing Interceptor

```java
public class TracingInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String traceId = headers.getOrDefault(MetadataKeys.TRACE_ID,
            UUID.randomUUID().toString());

        Span span = tracer.buildSpan("gRPC:" +
            call.getMethodDescriptor().getBareMethodName())
            .asChildOf(tracer.activeSpan())
            .withTag("trace.id", traceId)
            .start();

        try {
            Context context = Context.current()
                .withValue(ContextKeys.SPAN, span)
                .withValue(ContextKeys.TRACE_ID, traceId);

            return Contexts.interceptCall(context, call, headers, next);
        } catch (Exception e) {
            span.log(e.getMessage());
            span.setTag("error", true);
            throw e;
        } finally {
            span.finish();
        }
    }
}
```

### Metrics Interceptor

```java
public class MetricsInterceptor implements ServerInterceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();

        Timer.Sample sample = Timer.start(meterRegistry);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
            next.startCall(call, headers)) {

            @Override
            public void onComplete() {
                sample.stop(Timer.builder("grpc.server.calls")
                    .tag("method", methodName)
                    .tag("status", "OK")
                    .register(meterRegistry));
                super.onComplete();
            }

            @Override
            public void onCancel() {
                sample.stop(Timer.builder("grpc.server.calls")
                    .tag("method", methodName)
                    .tag("status", "CANCELLED")
                    .register(meterRegistry));
                super.onCancel();
            }
        };
    }
}
```

---

## Best Practices

### 1. Chain Interceptors Correctly

```java
// Server
Server server = ServerBuilder.forPort(50051)
    .addService(new UserServiceImpl())
    .intercept(new AuthInterceptor())        // First
    .intercept(new AuthorizationInterceptor()) // Second
    .intercept(new LoggingInterceptor())      // Third
    .intercept(new MetricsInterceptor())      // Fourth
    .build();

// Client
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
    .intercept(new ClientLoggingInterceptor())    // First
    .intercept(new ClientAuthInterceptor(token))  // Second
    .intercept(new RetryInterceptor(3))           // Third
    .build();
```

### 2. Use Context Propagation

```java
@Override
public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

    UserContext user = extractUser(headers);
    String requestId = extractRequestId(headers);

    Context context = Context.current()
        .withValue(ContextKeys.USER_CONTEXT, user)
        .withValue(ContextKeys.REQUEST_ID, requestId);

    return Contexts.interceptCall(context, call, headers, next);
}
```

### 3. Handle Errors Gracefully

```java
@Override
public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

    try {
        return next.startCall(call, headers);
    } catch (Exception e) {
        log.error("Interceptor error", e);
        call.close(Status.INTERNAL
            .withCause(e)
            .withDescription("Internal interceptor error"),
            new Metadata());
        return new NoopListener<>();
    }
}
```

### 4. Make Interceptors Configurable

```java
public class ConfigurableInterceptor implements ServerInterceptor {

    private final boolean enabled;
    private final List<String> excludedMethods;

    public ConfigurableInterceptor(boolean enabled,
                                    List<String> excludedMethods) {
        this.enabled = enabled;
        this.excludedMethods = excludedMethods;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        if (!enabled) {
            return next.startCall(call, headers);
        }

        String method = call.getMethodDescriptor().getFullMethodName();
        if (excludedMethods.contains(method)) {
            return next.startCall(call, headers);
        }

        // Apply interceptor logic
        return next.startCall(call, headers);
    }
}
```

### 5. Test Interceptors

```java
class AuthInterceptorTest {

    private AuthInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new AuthInterceptor(tokenValidator);
    }

    @Test
    void shouldRejectMissingToken() {
        Metadata headers = new Metadata();
        ServerCall<Req, Resp> call = mock(ServerCall.class);

        ServerCall.Listener<Req> listener = interceptor.interceptCall(
            call, headers, (c, h) -> new NoopListener<>());

        verify(call).close(
            eq(Status.UNAUTHENTICATED), any(Metadata.class));
    }

    @Test
    void shouldAcceptValidToken() {
        Metadata headers = new Metadata();
        headers.put(Metadata.Keys.AUTHORIZATION, "Bearer valid-token");

        when(tokenValidator.validate("valid-token"))
            .thenReturn(new AuthToken("user-123"));

        ServerCall<Req, Resp> call = mock(ServerCall.class);
        ServerCallHandler<Req, Resp> next = mock(ServerCallHandler.class);

        interceptor.interceptCall(call, headers, next);

        verify(next).startCall(eq(call), eq(headers));
    }
}
```

---

## Further Reading

- [gRPC Interceptors Documentation](https://grpc.io/docs/guides/interceptors/)
- [gRPC Java Interceptors](https://grpc.github.io/grpc-java/javadoc/io/grpc/ServerInterceptor.html)
- [Context Propagation](https://grpc.io/docs/guides/context/)
- [gRPC Error Handling](https://grpc.io/docs/guides/status-codes/)
