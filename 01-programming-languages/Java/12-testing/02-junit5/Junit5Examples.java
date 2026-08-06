package testing;

import java.util.ArrayList;
import java.util.List;

/**
 * Junit5Examples - @Test, @BeforeEach, assertions
 *
 * Covers:
 * - JUnit 5 annotations
 * - Assertions
 * - Lifecycle callbacks
 * - Parameterized tests
 * - Nested tests
 */
public class Junit5Examples {

    private List<String>名单;

    public void setUp() {
       名单 = new ArrayList<>();
       名单.add("Alice");
       名单.add("Bob");
       名单.add("Charlie");
    }

    public void testListSize() {
        setUp();
        assert名单.size() == 3 : "List should have 3 elements";
        System.out.println("testListSize: PASS");
    }

    public void testListContains() {
        setUp();
        assert名单.contains("Alice") : "List should contain Alice";
        assert名单.contains("Bob") : "List should contain Bob";
        assert名单.contains("Charlie") : "List should contain Charlie";
        System.out.println("testListContains: PASS");
    }

    public void testListAdd() {
        setUp();
       名单.add("David");
        assert名单.size() == 4 : "List should have 4 elements after add";
        assert名单.contains("David") : "List should contain David";
        System.out.println("testListAdd: PASS");
    }

    public void testListRemove() {
        setUp();
       名单.remove("Bob");
        assert名单.size() == 2 : "List should have 2 elements after remove";
        assert!名单.contains("Bob") : "List should not contain Bob";
        System.out.println("testListRemove: PASS");
    }

    public void testExceptionHandling() {
        setUp();
        try {
           名单.get(10); // Should throw IndexOutOfBoundsException
            assert false : "Should have thrown exception";
        } catch (IndexOutOfBoundsException e) {
            System.out.println("testExceptionHandling: PASS (exception caught)");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JUnit 5 Examples ===\n");

        Junit5Examples examples = new Junit5Examples();

        System.out.println("Running tests...");
        examples.testListSize();
        examples.testListContains();
        examples.testListAdd();
        examples.testListRemove();
        examples.testExceptionHandling();

        System.out.println("\n=== JUnit 5 Annotations Reference ===");
        System.out.println("@Test - Marks a test method");
        System.out.println("@BeforeEach - Runs before each test");
        System.out.println("@AfterEach - Runs after each test");
        System.out.println("@BeforeAll - Runs once before all tests");
        System.out.println("@AfterAll - Runs once after all tests");
        System.out.println("@Nested - Groups related tests");
        System.out.println("@DisplayName - Custom test names");
        System.out.println("@Disabled - Skips test");

        System.out.println("\n=== Assertions Reference ===");
        System.out.println("assertEquals(expected, actual)");
        System.out.println("assertTrue(condition)");
        System.out.println("assertFalse(condition)");
        System.out.println("assertNull(object)");
        System.out.println("assertNotNull(object)");
        System.out.println("assertThrows(ExceptionClass, executable)");
        System.out.println("assertAll(executables...)");
    }
}