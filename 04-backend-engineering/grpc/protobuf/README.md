# Protocol Buffers (protobuf)

## Comprehensive Guide to Protocol Buffer Design

Protocol Buffers (protobuf) is a language-neutral, platform-neutral, extensible mechanism for serializing structured data. This guide covers proto3 syntax, message design, and best practices.

---

## Table of Contents

1. [Proto3 Syntax](#proto3-syntax)
2. [Scalar Types](#scalar-types)
3. [Message Types](#message-types)
4. [Services](#services)
5. [Options and Annotations](#options-and-annotations)
6. [Code Generation](#code-generation)
7. [Best Practices](#best-practices)

---

## Proto3 Syntax

### Basic Structure

```protobuf
syntax = "proto3";

package example.v1;

option java_package = "com.example.v1";
option java_outer_classname = "ExampleProto";
option java_multiple_files = true;
option java_generate_equals_and_hash = true;
option go_package = "github.com/example/v1;examplev1";

import "google/protobuf/timestamp.proto";
import "google/protobuf/field_mask.proto";
import "google/protobuf/struct.proto";
import "google/protobuf/wrappers.proto";
import "google/protobuf/any.proto";

// Service definition
service UserService {
  rpc GetUser(GetUserRequest) returns (GetUserResponse);
  rpc ListUsers(ListUsersRequest) returns (ListUsersResponse);
  rpc CreateUser(CreateUserRequest) returns (CreateUserResponse);
  rpc UpdateUser(UpdateUserRequest) returns (UpdateUserResponse);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteUserResponse);
}
```

### Field Numbers

```protobuf
message Example {
  // Field numbers 1-15 use 1 byte
  // Field numbers 16-2047 use 2 bytes
  // Field numbers 2048-65535 use 3 bytes
  // Reserved for future use
  reserved 100 to 200;

  // Reserved field names
  reserved "old_field";

  // Field 1-15: compact encoding
  int32 compact_field = 1;
  string short_string = 2;

  // Field 16-2047: normal encoding
  int64 normal_field = 16;

  // Never change field numbers after deployment
  string name = 1;
  // string old_name = 2; // DEPRECATED: Use reserved
}
```

---

## Scalar Types

### Numeric Types

```protobuf
message NumericTypes {
  // Integers
  int32 int32_value = 1;       // Variable-length, -2^31 to 2^31-1
  int64 int64_value = 2;       // Variable-length, -2^63 to 2^63-1
  uint32 uint32_value = 3;     // Variable-length, 0 to 2^32-1
  uint64 uint64_value = 4;     // Variable-length, 0 to 2^64-1
  sint32 sint32_value = 5;     // ZigZag encoding, efficient for negatives
  sint64 sint64_value = 6;     // ZigZag encoding, efficient for negatives

  // Fixed-width integers
  fixed32 fixed32_value = 7;   // 4 bytes, > 2^28
  fixed64 fixed64_value = 8;   // 8 bytes, > 2^56
  sfixed32 sfixed32_value = 9; // 4 bytes, signed
  sfixed64 sfixed64_value = 10;// 8 bytes, signed

  // Floating point
  float float_value = 11;      // 4 bytes, ±3.4E38, 7 decimal digits
  double double_value = 12;    // 8 bytes, ±1.7E308, 15 decimal digits
}
```

### String and Bytes

```protobuf
message StringType {
  // String (UTF-8)
  string name = 1;

  // Bytes (arbitrary data)
  bytes data = 2;
}
```

### Boolean

```protobuf
message BooleanType {
  bool enabled = 1;
}
```

---

## Message Types

### Basic Messages

```protobuf
message User {
  int64 id = 1;
  string username = 2;
  string email = 3;
  bool active = 4;
  UserProfile profile = 5;
  repeated string roles = 6;
  map<string, string> metadata = 7;
  google.protobuf.Timestamp created_at = 8;
  google.protobuf.Timestamp updated_at = 9;
}

message UserProfile {
  string first_name = 1;
  string last_name = 2;
  string bio = 3;
  int32 age = 4;
  repeated string interests = 5;
}
```

### Nested Messages

```protobuf
message Order {
  int64 id = 1;
  int64 user_id = 2;
  repeated OrderItem items = 3;
  OrderStatus status = 4;
  Money total = 5;
  ShippingAddress shipping = 6;
  google.protobuf.Timestamp created_at = 7;

  message OrderItem {
    int64 product_id = 1;
    int32 quantity = 2;
    Money price = 3;
    string name = 4;
  }

  message Money {
    int64 amount = 1;
    string currency = 2;
  }

  message ShippingAddress {
    string street = 1;
    string city = 2;
    string state = 3;
    string zip = 4;
    string country = 5;
  }
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

### Oneof

```protobuf
message Notification {
  string id = 1;
  string title = 2;
  google.protobuf.Timestamp created_at = 3;

  oneof payload {
    EmailNotification email = 10;
    SMSNotification sms = 11;
    PushNotification push = 12;
    InAppNotification in_app = 13;
  }

  oneof status {
    NotificationStatus pending = 20;
    NotificationStatus delivered = 21;
    NotificationStatus failed = 22;
  }
}

message EmailNotification {
  string from = 1;
  string to = 2;
  string subject = 3;
  string body = 4;
  repeated string attachments = 5;
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

message InAppNotification {
  string user_id = 1;
  string message = 2;
  bool read = 3;
}

enum NotificationStatus {
  NOTIFICATION_STATUS_UNSPECIFIED = 0;
  NOTIFICATION_STATUS_PENDING = 1;
  NOTIFICATION_STATUS_DELIVERED = 2;
  NOTIFICATION_STATUS_FAILED = 3;
}
```

### Any

```protobuf
import "google/protobuf/any.proto";

message Event {
  string id = 1;
  string event_type = 2;
  google.protobuf.Timestamp timestamp = 3;
  google.protobuf.Any payload = 4;
  map<string, string> metadata = 5;
}

// Usage in Java
Event event = Event.newBuilder()
    .setId("event-1")
    .setEventType("user.created")
    .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
    .setPayload(Any.pack(CreateUserEvent.newBuilder()
        .setUserId(123)
        .setEmail("user@example.com")
        .build()))
    .build();

// Unpacking
if (event.getPayload().is(CreateUserEvent.class)) {
    CreateUserEvent createUser = event.getPayload()
        .unpack(CreateUserEvent.class);
}
```

### Map Types

```protobuf
message Configuration {
  map<string, string> settings = 1;
  map<string, int32> counters = 2;
  map<int64, User> users = 3;
  map<string, repeated string> tags = 4; // Not supported, use repeated
}

// Note: Maps are syntactic sugar for repeated messages
message ConfigEntry {
  string key = 1;
  string value = 2;
}
```

### Maps with Complex Values

```protobuf
message FeatureFlags {
  map<string, FeatureFlag> flags = 1;
}

message FeatureFlag {
  bool enabled = 1;
  string description = 2;
  User owner = 3;
  google.protobuf.Timestamp expires_at = 4;
}
```

---

## Services

### Service Definition

```protobuf
service UserService {
  // Unary RPC
  rpc GetUser(GetUserRequest) returns (GetUserResponse);

  // Server streaming
  rpc ListUsers(ListUsersRequest) returns (stream User);

  // Client streaming
  rpc ImportUsers(stream CreateUserRequest) returns (ImportUsersResponse);

  // Bidirectional streaming
  rpc Chat(stream ChatMessage) returns (stream ChatMessage);

  // Custom methods
  rpc SearchUsers(SearchUsersRequest) returns (SearchUsersResponse);
}
```

### HTTP Mapping

```protobuf
import "google/api/annotations.proto";

service UserService {
  option (google.api.http) = {
    get: "/v1/users"
  };

  rpc GetUser(GetUserRequest) returns (GetUserResponse) {
    option (google.api.http) = {
      get: "/v1/users/{id}"
    };
  }

  rpc CreateUser(CreateUserRequest) returns (CreateUserResponse) {
    option (google.api.http) = {
      post: "/v1/users"
      body: "*"
    };
  }

  rpc UpdateUser(UpdateUserRequest) returns (UpdateUserResponse) {
    option (google.api.http) = {
      put: "/v1/users/{user.id}"
      body: "user"
    };
  }

  rpc DeleteUser(DeleteUserRequest) returns (google.protobuf.Empty) {
    option (google.api.http) = {
      delete: "/v1/users/{id}"
    };
  }
}
```

---

## Options and Annotations

### Message Options

```protobuf
import "google/protobuf/descriptor.proto";

extend google.protobuf.MessageOptions {
  string my_custom_option = 51234;
}

message MyMessage {
  option (my_custom_option) = "hello";

  int32 id = 1;
}
```

### Field Options

```protobuf
message ValidationExample {
  // Field-level validation
  string email = 1 [(validate.rules).string.email = true];
  int32 age = 2 [(validate.rules).int32 = {gte: 0, lte: 150}];
  string name = 3 [(validate.rules).string = {min_len: 1, max_len: 100}];

  // Custom options
  string username = 4 [(my_field_option) = "required"];

  // JSON serialization options
  string created_at = 5 [
    (google.api.field_behavior) = IMMUTABLE,
    (google.api.resource_reference) = {
      type: "google.protobuf.Timestamp"
    }
  ];
}

extend google.protobuf.FieldOptions {
  string my_field_option = 51235;
}
```

### Service Options

```protobuf
import "google/api/annotations.proto";

service UserService {
  option (google.api.default_host) = "example.googleapis.com";
  option (google.api.oauth_scopes) =
    "https://www.googleapis.com/auth/userinfo.email,"
    "https://www.googleapis.com/auth/userinfo.profile";

  rpc GetUser(GetUserRequest) returns (GetUserResponse) {
    option (google.api.http) = {
      get: "/v1/users/{id}"
      additional_bindings {
        get: "/v1/users:me"
      }
    };
  }
}
```

### Deprecated Fields

```protobuf
message OldMessage {
  int32 id = 1;

  // Deprecated field
  string old_field = 2 [deprecated = true];

  // Use new_field instead
  string new_field = 3;
}
```

### Reserved Fields

```protobuf
message Example {
  // Reserve field numbers
  reserved 100 to 200;

  // Reserve field names
  reserved "old_field", "another_old_field";

  string name = 1;
  int32 id = 2;
}
```

---

## Code Generation

### Protoc Compiler

```bash
# Install protoc
# Ubuntu/Debian
sudo apt install protobuf-compiler

# macOS
brew install protobuf

# Java with protobuf gradle plugin
# build.gradle
plugins {
    id 'com.google.protobuf' version '0.9.4'
}

protobuf {
    protoc {
        artifact = 'com.google.protobuf:protoc:3.24.0'
    }
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.58.0'
        }
    }
    generateProtoTasks {
        all()*.plugins {
            grpc {}
        }
    }
}
```

### Maven Configuration

```xml
<plugin>
    <groupId>org.xolstice.maven.plugins</groupId>
    <artifactId>protobuf-maven-plugin</artifactId>
    <version>0.6.1</version>
    <configuration>
        <protocArtifact>com.google.protobuf:protoc:3.24.0:exe:${os.detected.classifier}</protocArtifact>
        <pluginId>grpc-java</pluginId>
        <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.58.0:exe:${os.detected.classifier}</pluginArtifact>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>compile-custom</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Generated Code

```java
// Generated message class
public final class User extends
    com.google.protobuf.GeneratedMessageV3 implements UserOrBuilder {

  // Builder pattern
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      UserOrBuilder {
    // ...
  }

  // Static factory methods
  public static User parseFrom(byte[] data) throws InvalidProtocolBufferException { ... }
  public static User parseFrom(InputStream input) throws IOException { ... }
}

// Generated service class
public class UserServiceGrpc {

  public static class UserServiceBlockingStub extends AbstractBlockingStub<UserServiceBlockingStub> {
    public GetUserResponse getUser(GetUserRequest request) { ... }
    public List<User> listUsers(ListUsersRequest request) { ... }
  }

  public static class UserServiceStub extends AbstractAsyncStub<UserServiceStub> {
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) { ... }
  }
}
```

---

## Best Practices

### 1. Use Meaningful Field Names

```protobuf
// Good
message User {
  int64 user_id = 1;
  string display_name = 2;
  string email_address = 3;
  google.protobuf.Timestamp registration_date = 4;
}

// Bad
message User {
  int64 id = 1;
  string name = 2;
  string email = 3;
  google.protobuf.Timestamp dt = 4;
}
```

### 2. Use Enums for State Values

```protobuf
// Good
enum OrderStatus {
  ORDER_STATUS_UNSPECIFIED = 0;
  ORDER_STATUS_PENDING = 1;
  ORDER_STATUS_PROCESSING = 2;
  ORDER_STATUS_SHIPPED = 3;
  ORDER_STATUS_DELIVERED = 4;
}

// Bad
message Order {
  int64 id = 1;
  int32 status = 2; // Unclear meaning
}
```

### 3. Use Oneof for Union Types

```protobuf
// Good
message PaymentMethod {
  oneof method {
    CreditCard credit_card = 1;
    BankTransfer bank_transfer = 2;
    PayPal paypal = 3;
  }
}

// Bad
message PaymentMethod {
  CreditCard credit_card = 1;
  BankTransfer bank_transfer = 2;
  PayPal paypal = 3;
}
```

### 4. Design for Versioning

```protobuf
// Versioned message
message UserV1 {
  int64 id = 1;
  string name = 2;
}

message UserV2 {
  int64 id = 1;
  string display_name = 2;
  string email = 3;
}

// Versioned service
service UserServiceV1 {
  rpc GetUser(GetUserRequest) returns (UserV1);
}

service UserServiceV2 {
  rpc GetUser(GetUserRequest) returns (UserV2);
}
```

### 5. Use Field Masks for Partial Updates

```protobuf
import "google/protobuf/field_mask.proto";

message UpdateUserRequest {
  User user = 1;
  google.protobuf.FieldMask update_mask = 2;
}

// Java usage
UpdateUserRequest request = UpdateUserRequest.newBuilder()
    .setUser(User.newBuilder()
        .setId(123)
        .setEmail("new@example.com")
        .build())
    .setUpdateMask(FieldMask.newBuilder()
        .addPaths("email")
        .build())
    .build();
```

### 6. Use Timestamps and Durations

```protobuf
import "google/protobuf/timestamp.proto";
import "google/protobuf/duration.proto";

message Event {
  string id = 1;
  google.protobuf.Timestamp start_time = 2;
  google.protobuf.Timestamp end_time = 3;
  google.protobuf.Duration duration = 4;
}

// Java usage
Event event = Event.newBuilder()
    .setStartTime(Timestamps.fromMillis(System.currentTimeMillis()))
    .setDuration(Duration.newBuilder()
        .setSeconds(3600)
        .build())
    .build();
```

### 7. Document Your Proto Files

```protobuf
// UserService provides CRUD operations for user management.
//
// It supports:
// - User creation with email validation
// - User retrieval by ID or email
// - User updates with partial update support
// - User deletion with soft delete
service UserService {
  // GetUser returns a user by their ID.
  //
  // Returns NOT_FOUND if the user does not exist.
  rpc GetUser(GetUserRequest) returns (GetUserResponse);

  // CreateUser creates a new user.
  //
  // Returns ALREADY_EXISTS if a user with the same email exists.
  rpc CreateUser(CreateUserRequest) returns (CreateUserResponse);
}
```

---

## Further Reading

- [Protocol Buffers Language Guide](https://developers.google.com/protocol-buffers/docs/proto3)
- [Protobuf Best Practices](https://developers.google.com/protocol-buffers/docs/reference/java-generated)
- [gRPC with Protocol Buffers](https://grpc.io/docs/what-is-grpc/introduction/)
- [Google API Design Guide](https://cloud.google.com/apis/design)
