package academy.javaengineering.testing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mockito examples - mocking, stubbing, verification
 *
 * This file covers:
 * - @Mock, @InjectMocks, @Captor
 * - when().thenReturn(), when().thenThrow()
 * - verify(), times(), never()
 * - Argument matchers: any(), eq(), argThat()
 * - @Spy vs @Mock
 * - BDD style: given().will().given()
 */
@ExtendWith(MockitoExtension.class)
class MockitoExamplesTest {

    // =========================================================
    // 1. INTERFACES AND IMPLEMENTATIONS UNDER TEST
    // =========================================================

    interface UserRepository {
        String findById(int id);
        List<String> findAll();
        void save(String name);
        void delete(int id);
        int count();
    }

    interface EmailService {
        void sendWelcomeEmail(String to);
        void sendNotificationEmail(String to, String message);
    }

    static class UserService {
        private final UserRepository repository;
        private final EmailService emailService;

        UserService(UserRepository repository, EmailService emailService) {
            this.repository = repository;
            this.emailService = emailService;
        }

        String findUserById(int id) {
            return repository.findById(id);
        }

        boolean saveUser(String name) {
            if (name == null || name.isBlank()) {
                return false;
            }
            repository.save(name);
            emailService.sendWelcomeEmail(name.toLowerCase() + "@example.com");
            return true;
        }

        void deleteUser(int id) {
            repository.delete(id);
        }

        int getUserCount() {
            return repository.count();
        }

        List<String> getAllUsers() {
            return repository.findAll();
        }
    }

    // =========================================================
    // 2. @MOCK - Create mock objects
    // =========================================================

    @Mock
    UserRepository mockRepository;

    @Mock
    EmailService mockEmailService;

    @InjectMocks
    UserService userService;

    @Captor
    ArgumentCaptor<String> emailCaptor;

    // =========================================================
    // 3. STUBBING - Define mock behavior
    // =========================================================

    @Test
    @DisplayName("Stubbing: when().thenReturn() returns predefined value")
    void shouldReturnStubbedValue() {
        when(mockRepository.findById(1)).thenReturn("Alice");

        String result = userService.findUserById(1);

        assertEquals("Alice", result);
        verify(mockRepository).findById(1);
    }

    @Test
    @DisplayName("Stubbing: when().thenThrow() simulates exceptions")
    void shouldThrowOnStubbedException() {
        when(mockRepository.findById(999))
            .thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class,
            () -> userService.findUserById(999));
    }

    @Test
    @DisplayName("Stubbing: multiple returns in sequence")
    void shouldReturnMultipleValues() {
        when(mockRepository.findById(1))
            .thenReturn("Alice")
            .thenReturn("Bob")
            .thenReturn("Charlie");

        assertEquals("Alice", userService.findUserById(1));
        assertEquals("Bob", userService.findUserById(1));
        assertEquals("Charlie", userService.findUserById(1));
    }

    // =========================================================
    // 4. ARGUMENT MATCHERS
    // =========================================================

    @Test
    @DisplayName("Argument matchers: any() matches any argument")
    void shouldMatchAnyArgument() {
        when(mockRepository.findById(anyInt())).thenReturn("AnyUser");

        assertEquals("AnyUser", userService.findUserById(1));
        assertEquals("AnyUser", userService.findUserById(100));
    }

    @Test
    @DisplayName("Argument matchers: argThat() with custom predicate")
    void shouldMatchWithCustomPredicate() {
        when(mockRepository.findById(argThat(id -> id > 0 && id < 100)))
            .thenReturn("ValidUser");

        assertEquals("ValidUser", userService.findUserById(50));
    }

    // =========================================================
    // 5. VERIFICATION - Check interactions happened
    // =========================================================

    @Test
    @DisplayName("Verification: verify() confirms method was called")
    void shouldVerifyMethodCalled() {
        when(mockRepository.findById(1)).thenReturn("Alice");

        userService.findUserById(1);

        verify(mockRepository).findById(1);
    }

    @Test
    @DisplayName("Verification: times() checks call count")
    void shouldVerifyCallCount() {
        when(mockRepository.findById(anyInt())).thenReturn("User");

        userService.findUserById(1);
        userService.findUserById(1);
        userService.findUserById(1);

        verify(mockRepository, times(3)).findById(anyInt());
    }

    @Test
    @DisplayName("Verification: never() confirms method was not called")
    void shouldVerifyMethodNeverCalled() {
        userService.findUserById(1);

        verify(mockRepository, never()).delete(anyInt());
    }

    @Test
    @DisplayName("Verification: never() on email service when save fails validation")
    void shouldNotSendEmailForInvalidInput() {
        boolean result = userService.saveUser("");

        assertFalse(result);
        verify(mockEmailService, never()).sendWelcomeEmail(anyString());
    }

    // =========================================================
    // 6. ARGUMENT CAPTORS
    // =========================================================

    @Test
    @DisplayName("ArgumentCaptor: capture and inspect arguments")
    void shouldCaptureArgument() {
        when(mockRepository.findById(1)).thenReturn("Alice");

        userService.findUserById(1);

        verify(mockRepository).findById(argThat(id -> id == 1));
    }

    @Test
    @DisplayName("ArgumentCaptor: capture email address sent")
    void shouldCaptureEmailAddress() {
        when(mockRepository.save(anyString())).thenReturn(null);

        userService.saveUser("Alice");

        verify(mockEmailService).sendWelcomeEmail(emailCaptor.capture());
        assertEquals("alice@example.com", emailCaptor.getValue());
    }

    // =========================================================
    // 7. BDD STYLE (Given-When-Then)
    // =========================================================

    @Test
    @DisplayName("BDD style: given().willReturn()")
    void bddStyleStubbing() {
        given(mockRepository.findById(1)).willReturn("Alice");

        String result = userService.findUserById(1);

        then(mockRepository).should().findById(1);
        assertEquals("Alice", result);
    }

    // =========================================================
    // 8. SPY - Partial mocking (real + mock)
    // =========================================================

    @Test
    @DisplayName("Spy: partial mock that preserves real behavior")
    void shouldUseSpy() {
        List<String> realList = new ArrayList<>(List.of("Alice", "Bob"));
        List<String> spyList = spy(realList);

        when(spyList.size()).thenReturn(100);

        assertEquals(100, spyList.size());
        assertEquals("Alice", spyList.get(0));

        verify(spyList).size();
    }

    // =========================================================
    // 9. REALISTIC SCENARIO: Service with dependencies
    // =========================================================

    @Test
    @DisplayName("Realistic: save user with validation and email")
    void shouldSaveUserAndSendEmail() {
        when(mockRepository.save(anyString())).thenReturn(null);

        boolean saved = userService.saveUser("Alice");

        assertTrue(saved);
        verify(mockRepository).save("Alice");
        verify(mockEmailService).sendWelcomeEmail("alice@example.com");
    }

    @Test
    @DisplayName("Realistic: reject null user name")
    void shouldRejectNullName() {
        boolean saved = userService.saveUser(null);

        assertFalse(saved);
        verify(mockRepository, never()).save(any());
        verify(mockEmailService, never()).sendWelcomeEmail(any());
    }

    @Test
    @DisplayName("Realistic: reject blank user name")
    void shouldRejectBlankName() {
        boolean saved = userService.saveUser("   ");

        assertFalse(saved);
        verify(mockRepository, never()).save(any());
        verify(mockEmailService, never()).sendWelcomeEmail(any());
    }
}
