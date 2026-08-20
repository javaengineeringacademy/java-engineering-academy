package academy.javaengineering.testing.examples;

import java.util.*;

/**
 * TestNG Framework Demo
 * Shows TestNG features and how they compare to JUnit 5
 */
class TestNGDemo {

    // ============================================
    // TestNG Annotations Demo (conceptual)
    // ============================================

    /*
     * TestNG Annotations:
     * 
     * @BeforeSuite / @AfterSuite - Run before/after entire suite
     * @BeforeTest / @AfterTest - Run before/after test XML config
     * @BeforeClass / @AfterClass - Run before/after class
     * @BeforeMethod / @AfterMethod - Run before/after each method
     * @BeforeGroups / @AfterGroups - Run before/after groups
     * 
     * Key Differences from JUnit 5:
     * - TestNG uses groups for organizing tests
     * - TestNG has built-in parameterized tests
     * - TestNG supports parallel execution natively
     * - TestNG has better dependency management
     * - TestNG provides soft assertions
     */

    // ============================================
    // Groups - Test Organization
    // ============================================

    static class LoginService {
        boolean login(String username, String password) {
            if ("admin".equals(username) && "admin123".equals(password)) {
                return true;
            }
            if ("user".equals(username) && "user123".equals(password)) {
                return true;
            }
            return false;
        }

        boolean logout(String sessionId) {
            return sessionId != null && !sessionId.isEmpty();
        }

        boolean isSessionValid(String sessionId) {
            return "valid-session".equals(sessionId);
        }
    }

    /*
     * TestNG Test Example with Groups:
     * 
     * public class LoginServiceTest {
     *     private LoginService loginService;
     * 
     *     @BeforeClass
     *     void setUp() {
     *         loginService = new LoginService();
     *     }
     * 
     *     @Test(groups = {"login", "smoke"})
     *     void testValidLogin() {
     *         assertTrue(loginService.login("admin", "admin123"));
     *     }
     * 
     *     @Test(groups = {"login", "regression"})
     *     void testInvalidLogin() {
     *         assertFalse(loginService.login("admin", "wrong"));
     *     }
     * 
     *     @Test(groups = {"logout"})
     *     void testLogout() {
     *         assertTrue(loginService.logout("session123"));
     *     }
     * 
     *     @Test(groups = {"session"})
     *     void testSessionValid() {
     *         assertTrue(loginService.isSessionValid("valid-session"));
     *     }
     * }
     */

    // ============================================
    // Parameterized Tests
    // ============================================

    /*
     * TestNG Parameterized Tests:
     * 
     * @Test(dataProvider = "loginData")
     * void testLogin(String username, String password, boolean expected) {
     *     assertEquals(loginService.login(username, password), expected);
     * }
     * 
     * @DataProvider(name = "loginData")
     * Object[][] loginData() {
     *     return new Object[][] {
     *         {"admin", "admin123", true},
     *         {"user", "user123", true},
     *         {"admin", "wrong", false},
     *         {"", "", false}
     *     };
     * }
     */

    // ============================================
    // Dependencies
    // ============================================

    /*
     * TestNG Test Dependencies:
     * 
     * @Test(dependsOnMethods = {"login"})
     * void dashboard() {
     *     // This runs only after login() passes
     * }
     * 
     * @Test(dependsOnMethods = {"dashboard"})
     * void logout() {
     *     // This runs only after dashboard() passes
     * }
     * 
     * @Test(dependsOnGroups = {"login.*"})
     * void fullFlow() {
     *     // This runs after all login group tests pass
     * }
     */

    // ============================================
    // Soft Assertions
    // ============================================

    static class SoftAssertions {
        private final List<String> errors = new ArrayList<>();

        void assertEquals(Object actual, Object expected, String message) {
            if (!Objects.equals(actual, expected)) {
                errors.add(message + ": expected " + expected + " but got " + actual);
            }
        }

        void assertTrue(boolean condition, String message) {
            if (!condition) {
                errors.add(message);
            }
        }

        void assertAll() {
            if (!errors.isEmpty()) {
                throw new AssertionError("Multiple failures:\n" + String.join("\n", errors));
            }
        }

        boolean hasErrors() {
            return !errors.isEmpty();
        }

        List<String> getErrors() {
            return new ArrayList<>(errors);
        }
    }

    // ============================================
    // Data Provider Pattern
    // ============================================

    static class MathUtils {
        static int add(int a, int b) { return a + b; }
        static int multiply(int a, int b) { return a * b; }
        static int divide(int a, int b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
    }

    // Simulating TestNG @DataProvider
    static Object[][] additionDataProvider() {
        return new Object[][] {
            {1, 2, 3},
            {10, 20, 30},
            {-1, 1, 0},
            {0, 0, 0},
            {100, 200, 300}
        };
    }

    // Simulating TestNG @DataProvider
    static Object[][] divisionDataProvider() {
        return new Object[][] {
            {10, 2, 5},
            {20, 4, 5},
            {100, 10, 10},
            {-10, 2, -5}
        };
    }

    // ============================================
    // Parallel Execution Demo
    // ============================================

    /*
     * TestNG Parallel Configuration (testng.xml):
     * 
     * <suite name="Parallel Suite" parallel="methods" thread-count="5">
     *     <test name="Test 1">
     *         <classes>
     *             <class name="com.example.LoginTest"/>
     *         </classes>
     *     </test>
     * </suite>
     * 
     * Or in code:
     * @Test(timeOut = 1000) // Fail if takes longer than 1 second
     * void testWithTimeout() {
     *     // test code
     * }
     */

    // ============================================
    // Listeners
    // ============================================

    /*
     * TestNG Listeners:
     * 
     * public class CustomListener implements ITestListener {
     *     @Override
     *     public void onTestStart(ITestResult result) {
     *         System.out.println("Starting: " + result.getMethod().getMethodName());
     *     }
     * 
     *     @Override
     *     public void onTestSuccess(ITestResult result) {
     *         System.out.println("PASSED: " + result.getMethod().getMethodName());
     *     }
     * 
     *     @Override
     *     public void onTestFailure(ITestResult result) {
     *         System.out.println("FAILED: " + result.getMethod().getMethodName());
     *         System.out.println("Exception: " + result.getThrowable());
     *     }
     * }
     * 
     * Usage:
     * @Listeners(CustomListener.class)
     * public class LoginTest {
     *     // tests
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== TestNG Demo ===\n");

        System.out.println("--- Data Provider Pattern ---");
        Object[][] data = additionDataProvider();
        for (Object[] row : data) {
            int a = (int) row[0];
            int b = (int) row[1];
            int expected = (int) row[2];
            int result = MathUtils.add(a, b);
            System.out.printf("%d + %d = %d (expected %d) - %s%n",
                a, b, result, expected, result == expected ? "PASSED" : "FAILED");
        }

        System.out.println("\n--- Soft Assertions ---");
        SoftAssertions sa = new SoftAssertions();
        sa.assertEquals(1, 1, "One equals one");
        sa.assertEquals(2, 2, "Two equals two");
        sa.assertTrue(1 > 0, "One is positive");
        sa.assertTrue(false, "This should fail");
        sa.assertEquals("a", "b", "Strings should match");

        if (sa.hasErrors()) {
            System.out.println("Soft assertion errors found:");
            sa.getErrors().forEach(e -> System.out.println("  - " + e));
        }

        System.out.println("\n--- Test Features Comparison ---");
        System.out.println("Feature          | TestNG    | JUnit 5");
        System.out.println("-----------------|-----------|----------");
        System.out.println("Groups           | Native    | Tags");
        System.out.println("Parameters       | DataProvider | @Parameterized");
        System.out.println("Dependencies     | Native    | @Order");
        System.out.println("Soft Assertions  | Native    | assertAll()");
        System.out.println("Parallel         | XML/Config| Config");
        System.out.println("Listeners        | Native    | Extensions");

        System.out.println("\n=== TestNG Demo Complete ===");
    }
}
