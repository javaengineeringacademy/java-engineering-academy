package testing;

/**
 * TestingBasics - Why test, test types
 *
 * Covers:
 * - Why testing is important
 * - Testing pyramid
 * - Unit vs Integration vs E2E tests
 * - Test-driven development (TDD)
 */
public class TestingBasics {

    private String name;
    private int age;

    public TestingBasics(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isAdult() {
        return age >= 18;
    }

    public String greet() {
        return "Hello, " + name + "!";
    }

    public static void main(String[] args) {
        System.out.println("=== Why Testing? ===");
        whyTesting();

        System.out.println("\n=== Testing Pyramid ===");
        testingPyramid();

        System.out.println("\n=== Test Types ===");
        testTypes();

        System.out.println("\n=== TDD Cycle ===");
        tddCycle();

        System.out.println("\n=== Simple Test Example ===");
        simpleTestExample();
    }

    static void whyTesting() {
        System.out.println("Benefits of Testing:");
        System.out.println("1. Catch bugs early");
        System.out.println("2. Enable refactoring with confidence");
        System.out.println("3. Document code behavior");
        System.out.println("4. Reduce debugging time");
        System.out.println("5. Improve code design");
        System.out.println("6. Facilitate collaboration");
    }

    static void testingPyramid() {
        System.out.println("Testing Pyramid:");
        System.out.println();
        System.out.println("        /\\");
        System.out.println("       /  \\      E2E Tests");
        System.out.println("      /    \\     (Few, slow, expensive)");
        System.out.println("     /------\\");
        System.out.println("    /        \\   Integration Tests");
        System.out.println("   /          \\  (Moderate number)");
        System.out.println("  /------------\\");
        System.out.println(" /              \\ Unit Tests");
        System.out.println("/                \\(Many, fast, cheap)");
        System.out.println();
        System.out.println("Ideal ratio: 70% Unit, 20% Integration, 10% E2E");
    }

    static void testTypes() {
        System.out.println("1. Unit Tests:");
        System.out.println("   - Test individual methods/classes");
        System.out.println("   - Fast execution");
        System.out.println("   - Isolated (no external dependencies)");
        System.out.println();
        System.out.println("2. Integration Tests:");
        System.out.println("   - Test component interactions");
        System.out.println("   - May use databases, APIs");
        System.out.println("   - Slower than unit tests");
        System.out.println();
        System.out.println("3. End-to-End (E2E) Tests:");
        System.out.println("   - Test complete workflows");
        System.out.println("   - Simulate real user scenarios");
        System.out.println("   - Slowest and most expensive");
        System.out.println();
        System.out.println("4. Functional Tests:");
        System.out.println("   - Test specific functionality");
        System.out.println("   - Black-box testing");
        System.out.println();
        System.out.println("5. Regression Tests:");
        System.out.println("   - Ensure bugs don't reappear");
        System.out.println("   - Run after code changes");
    }

    static void tddCycle() {
        System.out.println("Test-Driven Development (TDD):");
        System.out.println();
        System.out.println("1. RED: Write a failing test");
        System.out.println("   - Define expected behavior");
        System.out.println("   - Test should fail initially");
        System.out.println();
        System.out.println("2. GREEN: Write minimal code to pass");
        System.out.println("   - Just enough to make test pass");
        System.out.println("   - Don't over-engineer");
        System.out.println();
        System.out.println("3. REFACTOR: Improve code");
        System.out.println("   - Clean up while tests pass");
        System.out.println("   - Improve design");
        System.out.println();
        System.out.println("Repeat cycle for each feature.");
    }

    static void simpleTestExample() {
        // Simple assertion-like test
        TestingBasics person = new TestingBasics("John", 25);

        // Test 1: Name
        boolean testName = "John".equals(person.getName());
        System.out.println("Test getName(): " + (testName ? "PASS" : "FAIL"));

        // Test 2: Age
        boolean testAge = person.getAge() == 25;
        System.out.println("Test getAge(): " + (testAge ? "PASS" : "FAIL"));

        // Test 3: isAdult
        boolean testAdult = person.isAdult();
        System.out.println("Test isAdult(): " + (testAdult ? "PASS" : "FAIL"));

        // Test 4: greet
        boolean testGreet = "Hello, John!".equals(person.greet());
        System.out.println("Test greet(): " + (testGreet ? "PASS" : "FAIL"));

        // Edge case: Minor
        TestingBasics minor = new TestingBasics("Jane", 15);
        boolean testMinor = !minor.isAdult();
        System.out.println("Test isAdult (minor): " + (testMinor ? "PASS" : "FAIL"));
    }
}