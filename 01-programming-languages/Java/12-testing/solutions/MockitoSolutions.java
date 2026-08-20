package academy.javaengineering.testing.solutions;

import java.util.*;

/**
 * Mockito Solutions
 * Complete solutions for mocking, stubbing, and verification
 */
class MockitoSolutions {

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
    // Exercise 1: Basic Stubbing Solution
    // ============================================

    /*
     * @Mock
     * EmailService emailService;
     * 
     * @Mock
     * UserRepository userRepository;
     * 
     * @InjectMocks
     * NotificationService notificationService;
     * 
     * @Test
     * void testBasicStubbing() {
     *     // Arrange
     *     when(emailService.isValidEmail("test@example.com")).thenReturn(true);
     *     when(emailService.isValidEmail("invalid")).thenReturn(false);
     * 
     *     // Act & Assert
     *     assertTrue(emailService.isValidEmail("test@example.com"));
     *     assertFalse(emailService.isValidEmail("invalid"));
     * }
     * 
     * @Test
     * void testMultipleReturnValues() {
     *     // Arrange
     *     when(emailService.getTemplates())
     *         .thenReturn(List.of("template1", "template2"))
     *         .thenReturn(List.of("template3"));
     * 
     *     // Act & Assert
     *     assertEquals(2, emailService.getTemplates().size());
     *     assertEquals(1, emailService.getTemplates().size());
     * }
     */

    // ============================================
    // Exercise 2: Exception Stubbing Solution
    // ============================================

    /*
     * @Test
     * void testExceptionStubbing() {
     *     // Arrange
     *     when(emailService.sendEmail(anyString(), anyString(), anyString()))
     *         .thenThrow(new RuntimeException("Email service unavailable"));
     * 
     *     // Act & Assert
     *     RuntimeException exception = assertThrows(RuntimeException.class, () -> {
     *         emailService.sendEmail("test@example.com", "Subject", "Body");
     *     });
     *     assertEquals("Email service unavailable", exception.getMessage());
     * }
     * 
     * @Test
     * void testDoThrowForVoidMethod() {
     *     // Arrange
     *     doThrow(new RuntimeException("Service down"))
     *         .when(emailService).sendEmail(anyString(), anyString(), anyString());
     * 
     *     // Act & Assert
     *     assertThrows(RuntimeException.class, () -> {
     *         emailService.sendEmail("test@example.com", "Subject", "Body");
     *     });
     * }
     */

    // ============================================
    // Exercise 3: Verification Solution
    // ============================================

    /*
     * @Test
     * void testVerification() {
     *     // Arrange
     *     when(userRepository.findById("1")).thenReturn(
     *         Optional.of(Map.of("name", "John", "email", "john@example.com"))
     *     );
     * 
     *     // Act
     *     notificationService.sendWelcomeEmail("1");
     * 
     *     // Assert - Verify interactions
     *     verify(emailService, times(1)).sendEmail(
     *         eq("john@example.com"),
     *         eq("Welcome!"),
     *         contains("John")
     *     );
     *     verify(userRepository, times(1)).findById("1");
     * }
     * 
     * @Test
     * void testNeverCalled() {
     *     // Arrange
     *     when(userRepository.findById("999")).thenReturn(Optional.empty());
     * 
     *     // Act
     *     notificationService.sendWelcomeEmail("999");
     * 
     *     // Assert
     *     verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
     * }
     * 
     * @Test
     * void testAtLeast() {
     *     // Arrange
     *     when(userRepository.findById("1")).thenReturn(
     *         Optional.of(Map.of("name", "John", "email", "john@example.com"))
     *     );
     * 
     *     // Act
     *     notificationService.sendWelcomeEmail("1");
     * 
     *     // Assert
     *     verify(userRepository, atLeastOnce()).findById(anyString());
     * }
     */

    // ============================================
    // Exercise 4: Argument Captors Solution
    // ============================================

    /*
     * @Captor
     * ArgumentCaptor<String> emailCaptor;
     * 
     * @Captor
     * ArgumentCaptor<String> subjectCaptor;
     * 
     * @Test
     * void testArgumentCaptor() {
     *     // Arrange
     *     when(userRepository.findById("1")).thenReturn(
     *         Optional.of(Map.of("name", "John", "email", "john@example.com"))
     *     );
     * 
     *     // Act
     *     notificationService.sendWelcomeEmail("1");
     * 
     *     // Assert - Capture and verify arguments
     *     verify(emailService).sendEmail(
     *         emailCaptor.capture(),
     *         subjectCaptor.capture(),
     *         anyString()
     *     );
     * 
     *     assertEquals("john@example.com", emailCaptor.getValue());
     *     assertEquals("Welcome!", subjectCaptor.getValue());
     * }
     * 
     * @Test
     * void testMultipleCaptures() {
     *     // Arrange
     *     when(userRepository.findById("1")).thenReturn(
     *         Optional.of(Map.of("name", "John", "email", "john@example.com"))
     *     );
     *     when(userRepository.findById("2")).thenReturn(
     *         Optional.of(Map.of("name", "Jane", "email", "jane@example.com"))
     *     );
     * 
     *     // Act
     *     notificationService.sendWelcomeEmail("1");
     *     notificationService.sendWelcomeEmail("2");
     * 
     *     // Assert
     *     verify(emailService, times(2)).sendEmail(emailCaptor.capture(), anyString(), anyString());
     *     assertEquals("john@example.com", emailCaptor.getAllValues().get(0));
     *     assertEquals("jane@example.com", emailCaptor.getAllValues().get(1));
     * }
     */

    // ============================================
    // Exercise 5: Mock vs Spy Solution
    // ============================================

    /*
     * @Test
     * void testMockBehavior() {
     *     // Mock - all methods return default values
     *     List<String> mockList = mock(List.class);
     * 
     *     assertEquals(0, mockList.size());
     *     assertNull(mockList.get(0));
     *     assertTrue(mockList.isEmpty());
     * 
     *     // Stub specific methods
     *     when(mockList.size()).thenReturn(5);
     *     when(mockList.isEmpty()).thenReturn(false);
     * 
     *     assertEquals(5, mockList.size());
     *     assertFalse(mockList.isEmpty());
     * }
     * 
     * @Test
     * void testSpyBehavior() {
     *     // Spy - partial mocking of real object
     *     List<String> realList = new ArrayList<>(List.of("A", "B", "C"));
     *     List<String> spyList = spy(realList);
     * 
     *     // Real method calls work
     *     assertEquals(3, spyList.size());
     *     assertEquals("A", spyList.get(0));
     *     assertTrue(spyList.contains("B"));
     * 
     *     // Stub specific method
     *     when(spyList.size()).thenReturn(100);
     * 
     *     assertEquals(100, spyList.size());
     *     assertEquals("A", spyList.get(0)); // Real method still works
     * }
     * 
     * @Test
     * void testDoReturnForSpy() {
     *     List<String> realList = new ArrayList<>(List.of("X", "Y"));
     *     List<String> spyList = spy(realList);
     * 
     *     // doReturn is safer - doesn't call real method
     *     doReturn("Z").when(spyList).get(0);
     * 
     *     assertEquals("Z", spyList.get(0));
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Mockito Solutions ===\n");
        System.out.println("These are reference solutions for Mockito exercises.");
        System.out.println("Use @ExtendWith(MockitoExtension.class) for JUnit 5.\n");

        System.out.println("--- Basic Stubbing ---");
        System.out.println("when(mock.method(args)).thenReturn(value)");
        System.out.println("when(mock.method(args)).thenThrow(exception)\n");

        System.out.println("--- Verification ---");
        System.out.println("verify(mock, times(1)).method(args)");
        System.out.println("verify(mock, never()).method(args)");
        System.out.println("verify(mock, atLeastOnce()).method(args)\n");

        System.out.println("--- Argument Captors ---");
        System.out.println("@Captor ArgumentCaptor<T> captor");
        System.out.println("verify(mock).method(captor.capture())");
        System.out.println("assertEquals(expected, captor.getValue())\n");

        System.out.println("--- Mock vs Spy ---");
        System.out.println("mock() - returns default values for all methods");
        System.out.println("spy() - calls real methods, can stub specific ones");

        System.out.println("\n=== All solutions completed ===");
    }
}
