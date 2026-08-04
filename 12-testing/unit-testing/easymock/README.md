# EasyMock - Mock Object Library

## Overview

EasyMock is a Java framework that provides an easy way to create mock objects for unit testing. It uses a record-replay-verify model where you first record expected behavior, then replay the mock, and finally verify the interactions.

## Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.easymock</groupId>
        <artifactId>easymock</artifactId>
        <version>5.2.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Configuration

```groovy
dependencies {
    testImplementation 'org.easymock:easymock:5.2.0'
}
```

## Basic Concepts

### Record-Replay-Verify Pattern

```java
import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import org.junit.jupiter.api.Test;

class EasyMockBasicTest {

    @Test
    void shouldMockRepository() {
        // 1. Create mock
        UserRepository mockRepo = EasyMock.createMock(UserRepository.class);

        // 2. Record expectations
        EasyMock.expect(mockRepo.findById(1L))
                .andReturn(new User(1, "John"))
                .times(1);

        // 3. Switch to replay mode
        EasyMock.replay(mockRepo);

        // 4. Use the mock
        UserService service = new UserService(mockRepo);
        User user = service.getUser(1L);

        // 5. Verify interactions
        EasyMock.verify(mockRepo);

        // Assertions
        assertEquals("John", user.getName());
    }
}
```

## Creating Mocks

### Different Mock Types

```java
class MockTypesTest {

    @Test
    void shouldCreateDifferentMockTypes() {
        // Regular mock
        UserRepository strictMock = EasyMock.createMock(UserRepository.class);

        // Strict mock - fails on unexpected calls
        UserRepository strictMock2 = EasyMock.createStrictMock(UserRepository.class);

        // Nice mock - unexpected calls return defaults
        UserRepository niceMock = EasyMock.createNiceMock(UserRepository.class);
    }
}
```

### Mock with JUnit Extension

```java
import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EasyMockExtension.class)
class MockInjectionTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Test
    void shouldInjectMocks() {
        // Mocks are automatically created
        EasyMock.expect(userRepository.findById(1L))
                .andReturn(new User(1, "John"));

        EasyMock.replay(userRepository, emailService);

        UserService service = new UserService(userRepository, emailService);
        User user = service.getUser(1L);

        EasyMock.verify(userRepository, emailService);
        assertEquals("John", user.getName());
    }
}
```

## Stubbing Methods

### Return Values

```java
class StubbingTest {

    @Test
    void shouldStubMethods() {
        UserRepository mockRepo = EasyMock.createMock(UserRepository.class);

        // Return specific value
        EasyMock.expect(mockRepo.findById(1L))
                .andReturn(new User(1, "John"));

        // Return different values for consecutive calls
        EasyMock.expect(mockRepo.findById(2L))
                .andReturn(new User(2, "Jane"))
                .andReturn(new User(2, "Jane Updated"));

        // Return based on argument
        EasyMock.expect(mockRepo.findById(EasyMock.anyLong()))
                .andAnswer(() -> {
                    Long id = EasyMock.getCurrentArgument(0);
                    return new User(id, "User_" + id);
                });

        // Throw exception
        EasyMock.expect(mockRepo.findById(999L))
                .andThrow(new UserNotFoundException());

        EasyMock.replay(mockRepo);

        // Use mock
        User user1 = mockRepo.findById(1L);
        User user2 = mockRepo.findById(2L);
        User user3 = mockRepo.findById(2L);

        EasyMock.verify(mockRepo);
    }
}
```

### Argument Matchers

```java
class ArgumentMatchersTest {

    @Test
    void shouldUseArgumentMatchers() {
        UserRepository mockRepo = EasyMock.createMock(UserRepository.class);

        // anyInt(), anyLong(), anyString()
        EasyMock.expect(mockRepo.findById(EasyMock.anyLong()))
                .andReturn(new User(1, "John"));

        // eq() - specific value
        EasyMock.expect(mockRepo.save(EasyMock.eq(new User(1, "John"))))
                .andReturn(new User(1, "John"));

        // startsWith(), contains(), endsWith()
        EasyMock.expect(mockRepo.findByName(EasyMock.startsWith("J")))
                .andReturn(List.of(new User(1, "John")));

        // and() - combination
        EasyMock.expect(mockRepo.findByAgeAndName(EasyMock.anyInt(), EasyMock.anyString()))
                .andReturn(List.of());

        EasyMock.replay(mockRepo);

        // Use mock
        mockRepo.findById(1L);
        mockRepo.findByName("John");

        EasyMock.verify(mockRepo);
    }
}
```

### Default Values for Nice Mock

```java
class NiceMockTest {

    @Test
    void shouldReturnDefaultsForNiceMock() {
        UserRepository niceMock = EasyMock.createNiceMock(UserRepository.class);

        // Nice mock returns default values for unexpected calls
        // No need to stub every method

        EasyMock.replay(niceMock);

        // These calls return defaults (null for objects, 0 for primitives)
        User user = niceMock.findById(1L);
        List<User> users = niceMock.findAll();

        assertNull(user);
        assertNull(users);

        EasyMock.verify(niceMock);
    }
}
```

## Verification

### Basic Verification

```java
class VerificationTest {

    @Test
    void shouldVerifyInteractions() {
        UserRepository mockRepo = EasyMock.createMock(UserRepository.class);
        EmailService mockEmail = EasyMock.createMock(EmailService.class);

        // Record
        EasyMock.expect(mockRepo.save(EasyMock.anyObject(User.class)))
                .andReturn(new User(1, "John"));

        mockEmail.sendWelcomeEmail(EasyMock.anyString());

        EasyMock.replay(mockRepo, mockEmail);

        // Use
        UserService service = new UserService(mockRepo, mockEmail);
        service.createUser("John", "john@example.com");

        // Verify
        EasyMock.verify(mockRepo, mockEmail);
    }
}
```

### Times and Expected Count

```java
class TimesVerificationTest {

    @Test
    void shouldVerifyCallCount() {
        UserRepository mockRepo = EasyMock.createMock(UserRepository.class);

        // Expect exactly 2 calls
        EasyMock.expect(mockRepo.findById(EasyMock.anyLong()))
                .andReturn(new User(1, "John"))
                .times(2);

        EasyMock.replay(mockRepo);

        mockRepo.findById(1L);
        mockRepo.findById(2L);

        EasyMock.verify(mockRepo);
    }
}
```

## Partial Mocking

### Using createPartialMock

```java
class PartialMockTest {

    @Test
    void shouldPartiallyMock() {
        // Create partial mock - only mock specified methods
        UserService mockService = EasyMock.createPartialMock(
            UserService.class,
            "validateEmail" // Method to mock
        );

        // Stub only the mocked method
        EasyMock.expect(mockService.validateEmail(EasyMock.anyString()))
                .andReturn(true);

        EasyMock.replay(mockService);

        // Real method is called, except validateEmail
        User user = mockService.createUser("John", "john@example.com");

        EasyMock.verify(mockService);
    }
}
```

## Mocking Static Methods

```java
import org.easymock.EasyMockSupport;

class StaticMethodTest {

    @Test
    void shouldMockStaticMethods() {
        EasyMockSupport support = new EasyMockSupport();

        // Mock static method
        try (var mocked = support.mockStatic(Utility.class)) {
            mocked.expect(Utility::getCurrentDate)
                  .andReturn(LocalDate.of(2024, 1, 1));

            support.replayAll();

            // Use static method
            assertEquals(LocalDate.of(2024, 1, 1), Utility.getCurrentDate());

            support.verifyAll();
        }
    }
}
```

## Mocking Interfaces and Abstract Classes

```java
interface PaymentProcessor {
    boolean processPayment(double amount);
    void refund(double amount);
}

abstract class BaseRepository {
    abstract void save(Object entity);
    void log(String message) {
        System.out.println(message);
    }
}

class InterfaceMockTest {

    @Test
    void shouldMockInterface() {
        PaymentProcessor mockProcessor = EasyMock.createMock(PaymentProcessor.class);

        EasyMock.expect(mockProcessor.processPayment(100.0))
                .andReturn(true);

        EasyMock.replay(mockProcessor);

        assertTrue(mockProcessor.processPayment(100.0));

        EasyMock.verify(mockProcessor);
    }

    @Test
    void shouldMockAbstractClass() {
        BaseRepository mockRepo = EasyMock.createMock(BaseRepository.class);

        mockRepo.save(EasyMock.anyObject());
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockRepo);

        mockRepo.save(new Object());

        EasyMock.verify(mockRepo);
    }
}
```

## Comparison with Mockito

| Feature | EasyMock | Mockito |
|---------|----------|---------|
| API Style | Record-Replay-Verify | When-Then |
| Mock Creation | `createMock()` | `mock()` |
| Stubbing | `expect().andReturn()` | `when().thenReturn()` |
| Verification | `verify()` at end | `verify()` inline |
| Learning Curve | Moderate | Easy |
| Flexibility | High | High |

## Best Practices

### Use Record-Replay-Verify

```java
// Good
EasyMock.expect(mockRepo.findById(1L))
        .andReturn(new User(1, "John"));
EasyMock.replay(mockRepo);

// Use mock
User user = mockRepo.findById(1L);

EasyMock.verify(mockRepo);
```

### Prefer Nice Mocks for Flexibility

```java
// Nice mock allows unexpected calls
UserRepository niceMock = EasyMock.createNiceMock(UserRepository.class);
```

### Use Argument Matchers for Flexibility

```java
// Flexible matching
EasyMock.expect(mockRepo.save(EasyMock.anyObject(User.class)))
        .andReturn(new User(1, "John"));
```

## Resources

- [EasyMock GitHub](https://github.com/easymock/easymock)
- [EasyMock Documentation](http://easymock.org/user-guide.html)
- [EasyMock Javadoc](https://easymock.org/api/current/)
