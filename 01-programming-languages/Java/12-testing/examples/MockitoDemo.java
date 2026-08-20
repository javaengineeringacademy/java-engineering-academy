package academy.javaengineering.testing.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mockito Demo - Mocking, Stubbing, Verification
 */
@ExtendWith(MockitoExtension.class)
class MockitoDemo {

    // ============================================
    // Interfaces and Classes to Mock
    // ============================================

    interface EmailService {
        void sendEmail(String to, String subject, String body);
        boolean isValidEmail(String email);
        List<String> getTemplates();
    }

    interface UserRepository {
        Optional<Map<String, Object>> findById(String id);
        Map<String, Object> save(Map<String, Object> user);
        List<Map<String, Object>> findAll();
        void deleteById(String id);
    }

    static class NotificationService {
        private final EmailService emailService;
        private final UserRepository userRepository;

        NotificationService(EmailService emailService, UserRepository userRepository) {
            this.emailService = emailService;
            this.userRepository = userRepository;
        }

        void sendWelcomeEmail(String userId) {
            userRepository.findById(userId).ifPresent(user -> {
                String email = (String) user.get("email");
                String name = (String) user.get("name");
                emailService.sendEmail(email, "Welcome!", "Hello " + name);
            });
        }

        boolean isRegisteredUser(String userId) {
            return userRepository.findById(userId).isPresent();
        }

        void sendBulkEmail(List<String> userIds, String subject, String body) {
            for (String userId : userIds) {
                userRepository.findById(userId).ifPresent(user -> {
                    String email = (String) user.get("email");
                    emailService.sendEmail(email, subject, body);
                });
            }
        }
    }

    // ============================================
    // Basic Mocking
    // ============================================

    @Mock
    EmailService emailService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    NotificationService notificationService;

    @Captor
    ArgumentCaptor<String> emailCaptor;

    @Test
    @DisplayName("Basic stubbing - when().thenReturn()")
    void testBasicStubbing() {
        // Arrange - Stub the mock
        when(emailService.isValidEmail("test@example.com")).thenReturn(true);
        when(emailService.isValidEmail("invalid")).thenReturn(false);

        // Act & Assert
        assertTrue(emailService.isValidEmail("test@example.com"));
        assertFalse(emailService.isValidEmail("invalid"));
    }

    @Test
    @DisplayName("Stubbing with multiple return values")
    void testMultipleReturnValues() {
        // Arrange - First call returns "a", second returns "b", third returns "c"
        when(emailService.getTemplates())
            .thenReturn(List.of("template1", "template2"))
            .thenReturn(List.of("template3"));

        // Act & Assert
        assertEquals(2, emailService.getTemplates().size());
        assertEquals(1, emailService.getTemplates().size());
    }

    // ============================================
    // Stubbing Exceptions
    // ============================================

    @Test
    @DisplayName("Stubbing exceptions")
    void testStubbingExceptions() {
        // Arrange
        when(emailService.isValidEmail(anyString()))
            .thenThrow(new IllegalArgumentException("Invalid email"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.isValidEmail("test@example.com");
        });
    }

    // ============================================
    // Argument Matchers
    // ============================================

    @Test
    @DisplayName("Argument matchers - any, eq, contains")
    void testArgumentMatchers() {
        // Arrange
        when(emailService.isValidEmail(anyString())).thenReturn(true);
        when(emailService.isValidEmail(argThat(email -> email.contains("spam")))).thenReturn(false);

        // Act & Assert
        assertTrue(emailService.isValidEmail("test@example.com"));
        assertFalse(emailService.isValidEmail("spam@example.com"));
    }

    // ============================================
    // Verification
    // ============================================

    @Test
    @DisplayName("Verification - verifying method calls")
    void testVerification() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(
            Optional.of(Map.of("name", "John", "email", "john@example.com"))
        );

        // Act
        notificationService.sendWelcomeEmail("1");

        // Assert - Verify interactions
        verify(userRepository, times(1)).findById("1");
        verify(emailService, times(1)).sendEmail(
            eq("john@example.com"),
            eq("Welcome!"),
            contains("John")
        );
    }

    @Test
    @DisplayName("Verification - never called")
    void testNeverCalled() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        notificationService.sendWelcomeEmail("999");

        // Assert - emailService.sendEmail should never be called
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userRepository, times(1)).findById("999");
    }

    // ============================================
    // Argument Captors
    // ============================================

    @Test
    @DisplayName("Argument captors - capturing arguments")
    void testArgumentCaptor() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(
            Optional.of(Map.of("name", "Jane", "email", "jane@example.com"))
        );

        // Act
        notificationService.sendWelcomeEmail("1");

        // Assert - Capture and verify the email argument
        verify(emailService).sendEmail(emailCaptor.capture(), anyString(), anyString());
        assertEquals("jane@example.com", emailCaptor.getValue());
    }

    // ============================================
    // Mock vs Spy
    // ============================================

    @Test
    @DisplayName("Mock - all methods return default values")
    void testMockBehavior() {
        List<String> mockList = mock(List.class);

        // Default behavior
        assertEquals(0, mockList.size());
        assertNull(mockList.get(0));
        assertTrue(mockList.isEmpty());

        // Stub specific methods
        when(mockList.size()).thenReturn(5);
        assertEquals(5, mockList.size());
    }

    // ============================================
    // Real Partial Mocking with Spy
    // ============================================

    @Test
    @DisplayName("Spy - partial mocking of real object")
    void testSpy() {
        List<String> realList = new ArrayList<>();
        realList.add("first");
        realList.add("second");

        List<String> spyList = spy(realList);

        // Real method calls work
        assertEquals(2, spyList.size());
        assertEquals("first", spyList.get(0));

        // Stub specific method
        when(spyList.size()).thenReturn(10);
        assertEquals(10, spyList.size());
        // Real get() still works
        assertEquals("first", spyList.get(0));
    }

    // ============================================
    // DoReturn/DoThrow vs When
    // ============================================

    @Test
    @DisplayName("doReturn().when() vs when().thenReturn()")
    void testDoReturnVsWhen() {
        List<String> mockList = mock(List.class);

        // doReturn is safer when you want to avoid calling real method
        doReturn("mocked").when(mockList).get(0);

        assertEquals("mocked", mockList.get(0));
    }

    // ============================================
    // InOrder Verification
    // ============================================

    @Test
    @DisplayName("InOrder - verify call sequence")
    void testInOrder() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(
            Optional.of(Map.of("name", "John", "email", "john@example.com"))
        );

        // Act
        notificationService.sendWelcomeEmail("1");

        // Assert - Verify order of calls
        InOrder inOrder = inOrder(userRepository, emailService);
        inOrder.verify(userRepository).findById("1");
        inOrder.verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    // ============================================
    // Timeout Verification
    // ============================================

    @Test
    @DisplayName("Timeout - verify method called within time")
    void testTimeout() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(
            Optional.of(Map.of("name", "John", "email", "john@example.com"))
        );

        // Act & Assert - Should complete within 1 second
        assertTimeout(Duration.ofSeconds(1), () -> {
            notificationService.sendWelcomeEmail("1");
            verify(emailService, timeout(1000)).sendEmail(anyString(), anyString(), anyString());
        });
    }
}
