package academy.javaengineering.testing.examples;

import java.util.*;
import java.time.*;

/**
 * AssertJ Demo - Fluent Assertions Library
 */
class AssertjDemo {

    // ============================================
    // AssertJ Style - Static Import Pattern
    // ============================================

    /*
     * In real code, you would use:
     * import static org.assertj.core.api.Assertions.*;
     * 
     * Key features of AssertJ:
     * - Fluent API with method chaining
     * - Better error messages
     * - Rich set of assertions
     * - Custom assertion support
     */

    // ============================================
    // String Assertions
    // ============================================

    static class StringAssertExamples {
        static void demonstrate() {
            String name = "John Doe";
            String email = "john@example.com";
            String empty = "";
            String nullStr = null;

            // Standard JUnit style (verbose)
            // assertEquals("John Doe", name);
            // assertTrue(name.startsWith("John"));
            // assertTrue(name.contains("Doe"));

            // AssertJ style (fluent and readable)
            /*
             * assertThat(name)
             *     .isEqualTo("John Doe")
             *     .startsWith("John")
             *     .endsWith("Doe")
             *     .contains("oh")
             *     .hasSize(8)
             *     .isNotEmpty()
             *     .matches("John.*");
             * 
             * assertThat(email)
             *     .contains("@")
             *     .containsIgnoringCase("EXAMPLE")
             *     .doesNotContain("spam");
             * 
             * assertThat(empty).isEmpty();
             * assertThat(nullStr).isNull();
             * assertThat(name).isNotNull();
             */

            System.out.println("String assertions demonstrated (see comments for AssertJ syntax)");
        }
    }

    // ============================================
    // Number Assertions
    // ============================================

    static class NumberAssertExamples {
        static void demonstrate() {
            int age = 25;
            double price = 19.99;
            long population = 7_000_000_000L;

            // JUnit style
            // assertEquals(25, age);
            // assertTrue(age > 18);

            // AssertJ style
            /*
             * assertThat(age)
             *     .isEqualTo(25)
             *     .isGreaterThan(18)
             *     .isLessThan(100)
             *     .isBetween(0, 150)
             *     .isPositive();
             * 
             * assertThat(price)
             *     .isCloseTo(20.0, within(0.1))
             *     .isGreaterThan(0.0);
             * 
             * assertThat(population)
             *     .isPositive()
             *     .isGreaterThan(1_000_000_000L);
             */

            System.out.println("Number assertions demonstrated");
        }
    }

    // ============================================
    // Collection Assertions
    // ============================================

    static class CollectionAssertExamples {
        static void demonstrate() {
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
            Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87);

            // JUnit style (verbose)
            // assertEquals(3, names.size());
            // assertTrue(names.contains("Alice"));
            // assertTrue(names.contains("Bob"));

            // AssertJ style (fluent)
            /*
             * assertThat(names)
             *     .hasSize(3)
             *     .contains("Alice", "Bob")
             *     .doesNotContain("David")
             *     .containsExactly("Alice", "Bob", "Charlie")
             *     .containsExactlyInAnyOrder("Charlie", "Alice", "Bob")
             *     .startsWith("Ali")
             *     .filteredOn(s -> s.length() > 3)
             *     .hasSize(2);
             * 
             * assertThat(numbers)
             *     .hasSize(5)
             *     .contains(1, 2, 3)
             *     .doesNotContain(6, 7)
             *     .allMatch(n -> n > 0)
             *     .noneMatch(n -> n < 0);
             * 
             * assertThat(scores)
             *     .hasSize(2)
             *     .containsEntry("Alice", 95)
             *     .containsEntry("Bob", 87)
             *     .doesNotContainKey("Charlie")
             *     .containsKeys("Alice", "Bob")
             *     .containsValues(95, 87);
             */

            System.out.println("Collection assertions demonstrated");
        }
    }

    // ============================================
    // Exception Assertions
    // ============================================

    static class ExceptionAssertExamples {
        static void divideByZero() {
            throw new ArithmeticException("Division by zero");
        }

        static void throwWithMessage() {
            throw new IllegalArgumentException("Invalid input: null");
        }

        static void demonstrate() {
            // JUnit style
            // try {
            //     divideByZero();
            //     fail("Expected exception");
            // } catch (ArithmeticException e) {
            //     assertEquals("Division by zero", e.getMessage());
            // }

            // AssertJ style
            /*
             * assertThatExceptionOfType(ArithmeticException.class)
             *     .isThrownBy(() -> divideByZero())
             *     .withMessage("Division by zero")
             *     .withNoCause();
             * 
             * assertThatIllegalArgumentException()
             *     .isThrownBy(() -> throwWithMessage())
             *     .withMessageContaining("null");
             * 
             * assertThatThrownBy(() -> throwWithMessage())
             *     .isInstanceOf(IllegalArgumentException.class)
             *     .hasMessageContaining("Invalid");
             */

            System.out.println("Exception assertions demonstrated");
        }
    }

    // ============================================
    // Object Assertions
    // ============================================

    static class ObjectAssertExamples {
        static class Person {
            String name;
            int age;
            String email;

            Person(String name, int age, String email) {
                this.name = name;
                this.age = age;
                this.email = email;
            }

            // Using field comparisons for testing
            boolean fieldEquals(Person other) {
                return Objects.equals(name, other.name) 
                    && age == other.age 
                    && Objects.equals(email, other.email);
            }
        }

        static void demonstrate() {
            Person person1 = new Person("John", 30, "john@example.com");
            Person person2 = new Person("John", 30, "john@example.com");
            Person person3 = new Person("Jane", 25, "jane@example.com");

            // JUnit style
            // assertEquals(person1.name, person2.name);
            // assertEquals(person1.age, person2.age);

            // AssertJ style
            /*
             * assertThat(person1)
             *     .isNotNull()
             *     .isInstanceOf(Person.class);
             * 
             * // Using extracting for specific properties
             * assertThat(person1)
             *     .extracting(p -> p.name)
             *     .isEqualTo("John");
             * 
             * assertThat(person1)
             *     .extracting(p -> p.name, p -> p.age)
             *     .contains("John", 30);
             * 
             * // Using satisfies for complex checks
             * assertThat(person1).satisfies(p -> {
             *     assertThat(p.name).isEqualTo("John");
             *     assertThat(p.age).isGreaterThan(18);
             * });
             * 
             * // Using isEqualToComparingFieldByField (AssertJ 3.x)
             * assertThat(person1).isEqualToComparingFieldByField(person2);
             */

            System.out.println("Object assertions demonstrated");
        }
    }

    // ============================================
    // Date/Time Assertions
    // ============================================

    static class DateTimeAssertExamples {
        static void demonstrate() {
            LocalDate today = LocalDate.now();
            LocalDateTime now = LocalDateTime.now();

            // JUnit style
            // assertEquals(LocalDate.now(), today);

            // AssertJ style
            /*
             * assertThat(today)
             *     .isToday()
             *     .isBefore(LocalDate.now().plusDays(1))
             *     .isAfter(LocalDate.now().minusDays(1))
             *     .isBetween(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
             * 
             * assertThat(now)
             *     .isBeforeOrEqualTo(LocalDateTime.now())
             *     .isAfterOrEqualTo(LocalDateTime.now().minusSeconds(1));
             */

            System.out.println("DateTime assertions demonstrated");
        }
    }

    // ============================================
    // Custom Assertions Pattern
    // ============================================

    /*
     * Custom AssertJ Assertion Example:
     * 
     * public class UserAssert extends AbstractAssert<UserAssert, User> {
     * 
     *     protected UserAssert(User actual) {
     *         super(actual, UserAssert.class);
     *     }
     * 
     *     public UserAssert hasName(String name) {
     *         isNotNull();
     *         if (!Objects.equals(actual.getName(), name)) {
     *             failWithMessage("Expected user with name '%s' but was '%s'",
     *                 name, actual.getName());
     *         }
     *         return this;
     *     }
     * 
     *     public UserAssert isActive() {
     *         isNotNull();
     *         if (!actual.isActive()) {
     *             failWithMessage("Expected user to be active");
     *         }
     *         return this;
     *     }
     * 
     *     public static UserAssert assertThat(User actual) {
     *         return new UserAssert(actual);
     *     }
     * }
     * 
     * Usage:
     * assertThat(user)
     *     .hasName("John")
     *     .isActive();
     */

    public static void main(String[] args) {
        System.out.println("=== AssertJ Demo ===\n");

        System.out.println("--- String Assertions ---");
        StringAssertExamples.demonstrate();

        System.out.println("\n--- Number Assertions ---");
        NumberAssertExamples.demonstrate();

        System.out.println("\n--- Collection Assertions ---");
        CollectionAssertExamples.demonstrate();

        System.out.println("\n--- Exception Assertions ---");
        ExceptionAssertExamples.demonstrate();

        System.out.println("\n--- Object Assertions ---");
        ObjectAssertExamples.demonstrate();

        System.out.println("\n--- DateTime Assertions ---");
        DateTimeAssertExamples.demonstrate();

        System.out.println("\n=== AssertJ Demo Complete ===");
        System.out.println("\nAssertJ provides a fluent, readable API for assertions.");
        System.out.println("Import: import static org.assertj.core.api.Assertions.*;");
    }
}
