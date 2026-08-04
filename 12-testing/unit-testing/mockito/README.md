# Mockito - Mocking Framework

## Overview

Mockito is a popular mocking framework for Java that allows you to create mock objects, stub method calls, and verify interactions. It helps isolate the unit under test by replacing dependencies with controlled substitutes.

## Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.8.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.8.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Configuration

```groovy
dependencies {
    testImplementation 'org.mockito:mockito-core:5.8.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.8.0'
}
```

## Creating Mocks

### Using @Mock Annotation

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldCreateUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(new User(1, "John"));

        // Act
        UserService service = new UserService(userRepository);
        User user = service.createUser("John");

        // Assert
        assertNotNull(user);
        assertEquals("John", user.getName());
    }
}
```

### Using Mockito.mock()

```java
class UserServiceTest {

    @Test
    void shouldCreateUser() {
        // Create mock manually
        UserRepository mockRepository = mock(UserRepository.class);

        // Stub
        when(mockRepository.save(any())).thenReturn(new User(1, "John"));

        // Use
        UserService service = new UserService(mockRepository);
        User user = service.createUser("John");

        // Verify
        assertNotNull(user);
    }
}
```

## Stubbing Methods

### Basic Stubbing

```java
import org.mockito.stubbing.*;

class StubbingExamplesTest {

    @Test
    void shouldStubMethods() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Return specific value
        when(mockRepo.findById(1L)).thenReturn(new User(1, "John"));

        // Return different values for consecutive calls
        when(mockRepo.findById(anyLong()))
            .thenReturn(new User(1, "John"))
            .thenReturn(new User(2, "Jane"))
            .thenReturn(new User(3, "Bob"));

        // Return based on argument
        when(mockRepo.findById(1L)).thenReturn(new User(1, "John"));
        when(mockRepo.findById(2L)).thenReturn(new User(2, "Jane"));

        // Throw exception
        when(mockRepo.findById(999L)).thenThrow(new UserNotFoundException());

        // Call real method
        when(mockRepo.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
    }
}
```

### Stubbing with Answers

```java
class AdvancedStubbingTest {

    @Test
    void shouldUseAnswers() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Simple answer
        when(mockRepo.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Answer with multiple arguments
        when(mockRepo.findByCriteria(anyString(), anyInt()))
            .thenAnswer(invocation -> {
                String name = invocation.getArgument(0);
                int age = invocation.getArgument(1);
                return new User(name, age);
            });

        // DoReturn for void methods
        doNothing().when(mockRepo).delete(anyLong());

        // DoAnswer for void methods
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            System.out.println("Deleting user with id: " + id);
            return null;
        }).when(mockRepo).delete(anyLong());
    }
}
```

## Argument Matchers

```java
class ArgumentMatchersTest {

    @Test
    void shouldUseArgumentMatchers() {
        UserRepository mockRepo = mock(UserRepository.class);

        // any() - matches any object
        when(mockRepo.save(any(User.class))).thenReturn(new User(1, "John"));

        // anyInt(), anyString(), anyLong()
        when(mockRepo.findById(anyLong())).thenReturn(new User(1, "John"));

        // eq() - matches specific value
        when(mockRepo.findById(eq(1L))).thenReturn(new User(1, "John"));

        // startsWith(), contains(), endsWith()
        when(mockRepo.findByName(startsWith("J"))).thenReturn(List.of(new User("John")));

        // argThat() - custom matcher
        when(mockRepo.save(argThat(user -> user.getName().length() > 2)))
            .thenReturn(new User(1, "John"));

        // Multiple matchers
        when(mockRepo.findByAgeAndName(anyInt(), anyString()))
            .thenReturn(List.of());
    }
}
```

## Verification

### Basic Verification

```java
class VerificationTest {

    @Test
    void shouldVerifyInteractions() {
        UserRepository mockRepo = mock(UserRepository.class);
        EmailService mockEmail = mock(EmailService.class);

        UserService service = new UserService(mockRepo, mockEmail);

        // Create user
        service.createUser("John", "john@example.com");

        // Verify method was called once
        verify(mockRepo).save(any(User.class));

        // Verify with specific argument
        verify(mockRepo).save(argThat(user -> user.getName().equals("John")));

        // Verify method was never called
        verify(mockRepo, never()).delete(anyLong());

        // Verify exact number of times
        verify(mockRepo, times(1)).save(any());
        verify(mockEmail, times(1)).sendWelcomeEmail(any());
    }
}
```

### Advanced Verification

```java
class AdvancedVerificationTest {

    @Test
    void shouldVerifyAdvancedInteractions() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Verify at least N times
        verify(mockRepo, atLeastOnce()).findById(anyLong());
        verify(mockRepo, atLeast(1)).save(any());

        // Verify at most N times
        verify(mockRepo, atMost(3)).findById(anyLong());

        // Verify in order
        mockRepo.save(new User(1, "John"));
        mockRepo.save(new User(2, "Jane"));

        InOrder inOrder = inOrder(mockRepo);
        inOrder.verify(mockRepo).save(argThat(u -> u.getName().equals("John")));
        inOrder.verify(mockRepo).save(argThat(u -> u.getName().equals("Jane")));

        // Verify no more interactions
        verifyNoMoreInteractions(mockRepo);

        // Verify no interactions at all
        verifyNoInteractions(mock(EmailService.class));
    }
}
```

### Verification with Timeout

```java
class TimeoutVerificationTest {

    @Test
    void shouldVerifyWithTimeout() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Verify within timeout
        verify(mockRepo, timeout(1000)).findById(anyLong());

        // Verify at least once within timeout
        verify(mockRepo, timeout(1000).atLeastOnce()).save(any());
    }
}
```

## Spy

### Using @Spy

```java
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpyTest {

    @Spy
    private UserService realService;

    @Test
    void shouldSpyOnRealObject() {
        // Spy allows partial mocking
        when(realService.validateUser(anyString())).thenReturn(true);

        // Real method is called
        User user = realService.createUser("John");

        // But validation is stubbed
        verify(realService).validateUser("John");
        assertNotNull(user);
    }
}
```

### Using Mockito.spy()

```java
class SpyExampleTest {

    @Test
    void shouldCreateSpy() {
        List<String> realList = new ArrayList<>();
        List<String> spyList = spy(realList);

        // Real method call
        spyList.add("one");
        spyList.add("two");

        // Stub specific method
        when(spyList.size()).thenReturn(3);

        // Assertions
        assertEquals(3, spyList.size()); // Stubbed
        assertEquals(2, realList.size()); // Real
    }
}
```

## Mock Injection

### @InjectMocks

```java
@ExtendWith(MockitoExtension.class)
class InjectionTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldInjectMocks() {
        // Mocks are automatically injected into userService
        when(userRepository.save(any())).thenReturn(new User(1, "John"));

        User user = userService.createUser("John");

        assertNotNull(user);
        verify(userRepository).save(any());
        verify(emailService).sendWelcomeEmail(any());
    }
}
```

## Argument Capture

```java
import org.mockito.ArgumentCaptor;

class ArgumentCaptureTest {

    @Test
    void shouldCaptureArguments() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Create argument captor
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Call method
        mockRepo.save(new User("John", "john@example.com"));

        // Capture argument
        verify(mockRepo).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("John", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
    }

    @Test
    void shouldCaptureMultipleArguments() {
        EmailService mockEmail = mock(EmailService.class);

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);

        mockEmail.send("john@example.com", "Welcome", "Hello John!");

        verify(mockEmail).send(toCaptor.capture(), subjectCaptor.capture(), anyString());

        assertEquals("john@example.com", toCaptor.getValue());
        assertEquals("Welcome", subjectCaptor.getValue());
    }
}
```

## Mocking Static Methods

```java
import org.mockito.MockedStatic;

class StaticMethodMockingTest {

    @Test
    void shouldMockStaticMethods() {
        // Mock static method
        try (MockedStatic<Utility> mockedStatic = mockStatic(Utility.class)) {
            mockedStatic.when(() -> Utility.getCurrentDate())
                       .thenReturn(LocalDate.of(2024, 1, 1));

            // Use static method
            assertEquals(LocalDate.of(2024, 1, 1), Utility.getCurrentDate());
        }

        // After try-with-resources, static method is restored
    }

    @Test
    void shouldMockStaticMethodWithAnswer() {
        try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID)
                     .thenReturn(UUID.fromString("12345678-1234-1234-1234-123456789012"));

            assertEquals("12345678-1234-1234-1234-123456789012", 
                        UUID.randomUUID().toString());
        }
    }
}
```

## Mocking Final Classes

```java
// Mockito can mock final classes with inline mock maker
@ExtendWith(MockitoExtension.class)
class FinalClassTest {

    @Mock
    private FinalClass finalClassMock;

    @Test
    void shouldMockFinalClass() {
        when(finalClassMock.doSomething()).thenReturn("mocked");

        assertEquals("mocked", finalClassMock.doSomething());
    }
}
```

## Mocking Interfaces

```java
@ExtendWith(MockitoExtension.class)
class InterfaceMockingTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldMockInterface() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User(1, "John")));

        Optional<User> user = userRepository.findById(1L);

        assertTrue(user.isPresent());
        assertEquals("John", user.get().getName());
    }
}
```

## BDDMockito

```java
import org.mockito.BDDMockito;

class BDDStyleTest {

    @Test
    void shouldUseBDDStyle() {
        UserRepository mockRepo = mock(UserRepository.class);

        // Given
        BDDMockito.given(mockRepo.findById(1L))
            .willReturn(new User(1, "John"));

        // When
        UserService service = new UserService(mockRepo);
        User user = service.getUser(1L);

        // Then
        BDDMockito.then(mockRepo).should().findById(1L);
        assertNotNull(user);
    }
}
```

## Mockito Extensions

### @MockitoSettings

```java
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LenientTest {

    @Mock
    private UserRepository mockRepo;

    @Test
    void shouldAllowLenientStubbing() {
        // With LENIENT strictness, unused stubs don't cause failures
        when(mockRepo.findById(999L)).thenReturn(new User(999, "Test"));
        // This stub might not be used, but won't fail
    }
}
```

### @Captor

```java
@ExtendWith(MockitoExtension.class)
class CaptorTest {

    @Mock
    private UserRepository mockRepo;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldUseAnnotationCaptor() {
        mockRepo.save(new User("John"));

        verify(mockRepo).save(userCaptor.capture());

        assertEquals("John", userCaptor.getValue().getName());
    }
}
```

## Real vs Mock vs Spy

| Feature | Mock | Spy | Real Object |
|---------|------|-----|-------------|
| Calls real methods | No | Yes | Yes |
| Default behavior | Returns defaults | Calls real methods | Works normally |
| Stubbing | Required | Optional | N/A |
| Verification | Yes | Yes | No |

## Best Practices

### Prefer Mocking Over Stubbing

```java
// Good - clear stubbing
when(mockRepo.findById(1L)).thenReturn(user);

// Bad - unclear
when(mockRepo.findById(anyLong())).thenReturn(user);
```

### Verify Important Interactions

```java
// Verify critical behavior
verify(mockRepo).save(user);
verify(mockEmail).sendWelcomeEmail(user.getEmail());
```

### Use Argument Captors for Complex Verification

```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(mockRepo).save(captor.capture());

User savedUser = captor.getValue();
assertEquals("John", savedUser.getName());
assertTrue(savedUser.isActive());
```

## Resources

- [Mockito GitHub](https://github.com/mockito/mockito)
- [Mockito Documentation](https://site.mockito.org/)
- [Mockito Javadoc](https://javadoc.io/doc/org.mockito/mockito-core/latest/)
- [Baeldung Mockito Tutorial](https://www.baeldung.com/mockito-series)
