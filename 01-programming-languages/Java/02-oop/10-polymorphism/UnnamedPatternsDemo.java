/**
 * Unnamed Patterns and Variables (Java 21)
 *
 * Unnamed patterns ( _ ) allow you to ignore components in patterns
 * when you don't need their values. Unnamed variables ( _ ) allow you
 * to declare variables you don't intend to use.
 *
 * Benefits:
 * - Cleaner pattern matching (ignore unwanted components)
 * - Better iteration (skip loop variables)
 * - Clearer intent (explicitly ignore values)
 *
 * Expected output:
 * === Unnamed Pattern Variables ===
 * Person: Alice, Age: 30, City: NY
 * Person: Bob, Age: 25, City: SF
 *
 * === Unnamed in Iteration ===
 * Processing: Apple
 * Processing: Banana
 * Processing: Cherry
 *
 * === Unnamed in Lambda ===
 * Result: 42
 *
 * === Before vs After ===
 * Old: Person{name='Alice', age=30, city='NY'}
 * New: Alice from NY (age ignored)
 */
public class UnnamedPatternsDemo {

    record Point(int x, int y) {}
    record Person(String name, int age, String city) {}
    record Company(String name, Person ceo, Person cto) {}

    public static void main(String[] args) {
        unnamedPatternVariables();
        unnamedInIteration();
        unnamedInLambda();
        beforeVsAfter();
    }

    // =========================================================
    // 1. UNNAMED PATTERN VARIABLES
    // =========================================================
    static void unnamedPatternVariables() {
        System.out.println("=== Unnamed Pattern Variables ===\n");

        // --- Before Java 21: must name all variables even if unused ---
        // Person p = new Person("Alice", 30, "NY");
        // if (p instanceof Person(String name, int age, String city)) {
        //     // 'age' is unused but we had to name it
        //     System.out.println("Person: " + name + ", City: " + city);
        // }

        // --- With Java 21: unnamed pattern ( _ ) ignores components ---
        Person[] people = {
            new Person("Alice", 30, "NY"),
            new Person("Bob", 25, "SF")
        };

        for (Person p : people) {
            // _ ignores the age component - we don't need it
            if (p instanceof Person(String name, _, String city)) {
                System.out.println("Person: " + name + ", Age: [ignored], City: " + city);
            }
        }

        // Unnamed in switch
        Person person = new Person("Charlie", 35, "LA");
        String description = switch (person) {
            case Person(String name, _, _) -> "Person: " + name;
        };
        System.out.println(description);

        System.out.println();
    }

    // =========================================================
    // 2. UNNAMED IN ITERATION
    // =========================================================
    static void unnamedInIteration() {
        System.out.println("=== Unnamed in Iteration ===\n");

        // --- Before Java 21: named unused variable ---
        // for (int i = 0; i < 3; i++) {
        //     // 'i' is unused
        // }

        // String[] items = {"Apple", "Banana", "Cherry"};
        // for (int idx = 0; idx < items.length; idx++) {
        //     // 'idx' is unused
        //     System.out.println("Processing: " + items[idx]);
        // }

        // --- With Java 21: unnamed variable in for loop ---
        String[] items = {"Apple", "Banana", "Cherry"};

        for (int _ = 0; _ < items.length; _++) {
            // _ indicates we don't use the loop variable
            System.out.println("Processing: " + items[_]);
        }

        // Enhanced for loop (no index needed)
        for (String item : items) {
            System.out.println("Item: " + item);
        }

        System.out.println();
    }

    // =========================================================
    // 3. UNNAMED IN LAMBDA
    // =========================================================
    static void unnamedInLambda() {
        System.out.println("=== Unnamed in Lambda ===\n");

        // --- Before Java 21: named unused parameter ---
        // Function<Integer, Integer> func = (unused) -> 42;

        // --- With Java 21: unnamed parameter ---
        // var func = (_) -> 42;  // unnamed parameter
        // System.out.println("Result: " + func.apply(100));

        // In practice, use _ for ignored lambda parameters
        java.util.function.BiFunction<String, Integer, String> formatter =
                (name, _) -> "Hello, " + name;  // _ ignores the integer

        System.out.println("Result: " + formatter.apply("World", 42));

        // Multiple unnamed parameters
        java.util.function.BiFunction<Integer, Integer, Integer> adder =
                (a, _) -> a;  // _ ignores second param
        System.out.println("Adder: " + adder.apply(10, 20));

        System.out.println();
    }

    // =========================================================
    // 4. BEFORE VS AFTER
    // =========================================================
    static void beforeVsAfter() {
        System.out.println("=== Before vs After ===\n");

        Person person = new Person("Alice", 30, "NY");

        // Before Java 21: must name all components
        System.out.println("--- Before Java 21 ---");
        if (person instanceof Person) {
            Person p = (Person) person;
            System.out.println("Person{name='" + p.name() + "', age=" + p.age() + ", city='" + p.city() + "'}");
        }

        // With Java 21: unnamed pattern
        System.out.println("\n--- With Java 21 ---");
        if (person instanceof Person(String name, _, String city)) {
            System.out.println("Person: " + name + " from " + city + " (age ignored)");
        }

        // Nested patterns with unnamed
        Company company = new Company("Acme",
                new Person("CEO", 50, "NY"),
                new Person("CTO", 45, "SF"));

        System.out.println("\n--- Nested Patterns ---");
        // Before: Company(String n, Person ceo, Person cto) { ceo.name()... }
        // After: ignore what you don't need
        if (company instanceof Company(String companyName, Person(String ceoName, _, _), _)) {
            System.out.println("Company: " + companyName + ", CEO: " + ceoName);
        }

        System.out.println("\nBenefits:");
        System.out.println("  - Explicitly marks ignored values");
        System.out.println("  - Reduces noise in pattern matching");
        System.out.println("  - Prevents accidental use of unwanted values");
        System.out.println("  - Makes code intent clearer");
        System.out.println();
    }
}
