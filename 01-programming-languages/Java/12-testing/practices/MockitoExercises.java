package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Mockito Exercises
 * Practice mocking, stubbing, and verification
 */
class MockitoExercises {

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
    }

    // ============================================
    // Exercise 1: Basic Stubbing
    // ============================================
    // TODO: Mock EmailService and stub methods

    /*
     * TODO: Implement tests
     * 
     * @Test
     * void testBasicStubbing() {
     *     // Mock EmailService
     *     // Stub isValidEmail to return true for valid emails
     *     // Verify the behavior
     * }
     */

    // ============================================
    // Exercise 2: Exception Stubbing
    // ============================================
    // TODO: Stub methods to throw exceptions

    /*
     * @Test
     * void testExceptionStubbing() {
     *     // Stub sendEmail to throw RuntimeException
     *     // Verify exception is thrown
     * }
     */

    // ============================================
    // Exercise 3: Verification
    // ============================================
    // TODO: Verify method calls

    /*
     * @Test
     * void testVerification() {
     *     // Stub repository to return user
     *     // Call sendWelcomeEmail
     *     // Verify sendEmail was called once with correct arguments
     *     // Verify findById was called once
     * }
     */

    // ============================================
    // Exercise 4: Argument Captors
    // ============================================
    // TODO: Capture and verify arguments

    /*
     * @Test
     * void testArgumentCaptor() {
     *     // Stub repository to return user
     *     // Call sendWelcomeEmail
     *     // Capture the email argument
     *     // Assert captured email matches expected
     * }
     */

    // ============================================
    // Exercise 5: Mock vs Spy
    // ============================================
    // TODO: Understand difference between mock and spy

    /*
     * @Test
     * void testMockVsSpy() {
     *     // Create a mock List
     *     // Create a spy on a real ArrayList
     *     // Show that mock returns defaults
     *     // Show that spy calls real methods
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Mockito Exercises ===");
        System.out.println("Practice mocking, stubbing, and verification.");
    }
}
