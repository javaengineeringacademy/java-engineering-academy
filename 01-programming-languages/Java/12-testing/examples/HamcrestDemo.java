package academy.javaengineering.testing.examples;

import java.util.*;

/**
 * Hamcrest Demo - Matchers Library
 */
class HamcrestDemo {

    // ============================================
    // Core Matchers
    // ============================================

    static class CoreMatchers {
        static void demonstrate() {
            String name = "John";
            int age = 25;
            double price = 19.99;
            Object nullObj = null;
            Object nonNullObj = "hello";

            // is() - exact match
            /*
             * assertThat(name, is("John"));
             * assertThat(age, is(25));
             */

            // equalTo() - same as is()
            /*
             * assertThat(name, equalTo("John"));
             */

            // not() / isNot() - negation
            /*
             * assertThat(name, not("Jane"));
             * assertThat(name, isNot("Jane"));
             */

            // nullValue() / notNullValue()
            /*
             * assertThat(nullObj, nullValue());
             * assertThat(nonNullObj, notNullValue());
             */

            // sameInstance() / any()
            /*
             * assertThat(name, sameInstance(name));
             * assertThat(name, any(String.class));
             */

            System.out.println("Core matchers demonstrated");
        }
    }

    // ============================================
    // String Matchers
    // ============================================

    static class StringMatchers {
        static void demonstrate() {
            String str = "Hello World";

            // containsString()
            /*
             * assertThat(str, containsString("Hello"));
             * assertThat(str, containsStringIgnoringCase("hello"));
             */

            // startsWith() / endsWith()
            /*
             * assertThat(str, startsWith("Hello"));
             * assertThat(str, endsWith("World"));
             */

            // equalToIgnoringCase()
            /*
             * assertThat(str, equalToIgnoringCase("hello world"));
             */

            // equalToIgnoringWhiteSpace()
            /*
             * assertThat("  Hello  World  ", equalToIgnoringWhiteSpace("Hello World"));
             */

            // matchesPattern()
            /*
             * assertThat(str, matchesPattern("Hello.*"));
             */

            // hasLength()
            /*
             * assertThat(str, hasLength(11));
             */

            // blankOrNullString() / emptyString() / emptyOrNullString()
            /*
             * assertThat("", emptyString());
             * assertThat(null, blankOrNullString());
             * assertThat("  ", blankOrNullString());
             */

            System.out.println("String matchers demonstrated");
        }
    }

    // ============================================
    // Number Matchers
    // ============================================

    static class NumberMatchers {
        static void demonstrate() {
            int age = 25;
            double price = 19.99;

            // Comparison matchers
            /*
             * assertThat(age, greaterThan(18));
             * assertThat(age, greaterThanOrEqualTo(25));
             * assertThat(age, lessThan(30));
             * assertThat(age, lessThanOrEqualTo(25));
             */

            // CloseTo (for doubles)
            /*
             * assertThat(price, closeTo(20.0, 0.1));
             */

            // between()
            /*
             * assertThat(age, between(0, 100));
             */

            System.out.println("Number matchers demonstrated");
        }
    }

    // ============================================
    // Collection Matchers
    // ============================================

    static class CollectionMatchers {
        static void demonstrate() {
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
            Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87);

            // hasSize()
            /*
             * assertThat(names, hasSize(3));
             */

            // contains()
            /*
             * assertThat(names, contains("Alice", "Bob", "Charlie"));
             * assertThat(names, containsInAnyOrder("Charlie", "Alice", "Bob"));
             */

            // hasItem() / hasItems()
            /*
             * assertThat(names, hasItem("Alice"));
             * assertThat(names, hasItem("Alice", "Bob"));
             */

            // empty() / notEmpty()
            /*
             * assertThat(names, not(empty()));
             * assertThat(new ArrayList<>(), empty());
             */

            // hasEntry() / hasKey() / hasValue()
            /*
             * assertThat(scores, hasEntry("Alice", 95));
             * assertThat(scores, hasKey("Alice"));
             * assertThat(scores, hasValue(95));
             */

            // everyItem()
            /*
             * assertThat(names, everyItem(notNullValue()));
             * assertThat(names, everyItem(hasLength(greaterThan(0))));
             */

            System.out.println("Collection matchers demonstrated");
        }
    }

    // ============================================
    // Object Matchers
    // ============================================

    static class ObjectMatchers {
        static class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            String getName() { return name; }
            int getAge() { return age; }
        }

        static void demonstrate() {
            Person person = new Person("John", 30);

            // hasProperty()
            /*
             * assertThat(person, hasProperty("name", "John"));
             * assertThat(person, hasProperty("age", 30));
             */

            // instanceOf()
            /*
             * assertThat(person, instanceOf(Person.class));
             */

            // typeCompatibleWith()
            /*
             * assertThat(person, typeCompatibleWith(Person.class));
             */

            // samePropertyValuesAs()
            /*
             * Person expected = new Person("John", 30);
             * assertThat(person, samePropertyValuesAs(expected));
             */

            // hasToString()
            /*
             * assertThat(person.toString(), hasToString(containsString("John")));
             */

            System.out.println("Object matchers demonstrated");
        }
    }

    // ============================================
    // Logical Matchers
    // ============================================

    static class LogicalMatchers {
        static void demonstrate() {
            int age = 25;

            // and() / or() / not()
            /*
             * assertThat(age, allOf(greaterThan(18), lessThan(30)));
             * assertThat(age, anyOf(equalTo(25), equalTo(26)));
             * assertThat(age, not(equalTo(30)));
             */

            // both() / either() / neither()
            /*
             * assertThat(age, both(greaterThan(18)).and(lessThan(30)));
             * assertThat(age, either(equalTo(25)).or(equalTo(26)));
             * assertThat(age, neither(equalTo(20)).nor(equalTo(30)));
             */

            System.out.println("Logical matchers demonstrated");
        }
    }

    // ============================================
    // Custom Matcher
    // ============================================

    /*
     * Custom Matcher Example:
     * 
     * public class IsEven extends BaseMatcher<Integer> {
     *     @Override
     *     public boolean matches(Object item) {
     *         if (item instanceof Integer) {
     *             return (Integer) item % 2 == 0;
     *         }
     *         return false;
     *     }
     * 
     *     @Override
     *     public void describeTo(Description description) {
     *         description.appendText("an even number");
     *     }
     * 
     *     public static Matcher<Integer> isEven() {
     *         return new IsEven();
     *     }
     * }
     * 
     * Usage:
     * assertThat(4, isEven());
     * assertThat(5, not(isEven()));
     */

    // ============================================
    // Feature Matcher Pattern
    // ============================================

    /*
     * Feature Matcher for User:
     * 
     * public class UserMatcher extends TypeSafeMatcher<User> {
     *     private final String expectedName;
     * 
     *     public UserMatcher(String expectedName) {
     *         this.expectedName = expectedName;
     *     }
     * 
     *     @Override
     *     protected boolean matchesSafely(User user) {
     *         return user.getName().equals(expectedName);
     *     }
     * 
     *     @Override
     *     protected void describeMismatchSafely(User user, Description mismatchDescription) {
     *         mismatchDescription.appendText("was ").appendValue(user.getName());
     *     }
     * 
     *     @Override
     *     public void describeTo(Description description) {
     *         description.appendText("a user with name ").appendValue(expectedName);
     *     }
     * 
     *     public static Matcher<User> hasUserName(String name) {
     *         return new UserMatcher(name);
     *     }
     * }
     * 
     * Usage:
     * assertThat(user, hasUserName("John"));
     */

    public static void main(String[] args) {
        System.out.println("=== Hamcrest Demo ===\n");

        System.out.println("--- Core Matchers ---");
        CoreMatchers.demonstrate();

        System.out.println("\n--- String Matchers ---");
        StringMatchers.demonstrate();

        System.out.println("\n--- Number Matchers ---");
        NumberMatchers.demonstrate();

        System.out.println("\n--- Collection Matchers ---");
        CollectionMatchers.demonstrate();

        System.out.println("\n--- Object Matchers ---");
        ObjectMatchers.demonstrate();

        System.out.println("\n--- Logical Matchers ---");
        LogicalMatchers.demonstrate();

        System.out.println("\n=== Hamcrest Demo Complete ===");
        System.out.println("\nImport: import static org.hamcrest.MatcherAssert.assertThat;");
        System.out.println("Import: import static org.hamcrest.Matchers.*;");
    }
}
