package academy.javaengineering.exercises;

import java.util.Optional;

/**
 * Exercises: Optional (Handle Null Values)
 *
 * Complete the TODO sections below.
 */
public class OptionalExercises {

    // TODO 1: Safely get a value or provide a default
    // If the Optional is empty, return the default value
    public <T> T getOrDefault(Optional<T> optional, T defaultValue) {
        // TODO: implement this using Optional methods
        return null;
    }

    // TODO 2: Chain multiple Optional lookups
    // Given a user ID, look up the user's department, then the department's manager
    // Use a chain of Optional methods (flatMap)
    public static class User {
        private final String id;
        private final String name;
        private final String departmentId;

        public User(String id, String name, String departmentId) {
            this.id = id;
            this.name = name;
            this.departmentId = departmentId;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDepartmentId() { return departmentId; }
    }

    public static class Department {
        private final String id;
        private final String name;
        private final String managerName;

        public Department(String id, String name, String managerName) {
            this.id = id;
            this.name = name;
            this.managerName = managerName;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getManagerName() { return managerName; }
    }

    // Given a userId, return the manager name
    // Use Optional.flatMap for chaining
    public String getManagerName(String userId) {
        // TODO: implement this
        // Look up user from findUser(userId)
        // Then department from findDepartment(user.getDepartmentId())
        // Then return department.getManagerName()
        return "";
    }

    private Optional<User> findUser(String userId) {
        // Simulated database lookup
        if ("U001".equals(userId)) return Optional.of(new User("U001", "Alice", "D001"));
        if ("U002".equals(userId)) return Optional.of(new User("U002", "Bob", "D002"));
        return Optional.empty();
    }

    private Optional<Department> findDepartment(String deptId) {
        if ("D001".equals(deptId)) return Optional.of(new Department("D001", "Engineering", "Charlie"));
        if ("D002".equals(deptId)) return Optional.of(new Department("D002", "Marketing", "Diana"));
        return Optional.empty();
    }

    // TODO 3: Filter Optional values
    // Return the value only if it's present AND meets the condition
    // Otherwise return Optional.empty()
    public Optional<Integer> filterOptional(Optional<Integer> optional, java.util.function.Predicate<Integer> predicate) {
        // TODO: implement this
        return Optional.empty();
    }

    // TODO 4: Transform Optional with fallback
    // If the Optional has a value, transform it
    // If empty, return Optional with the fallback value
    public <T, R> Optional<R> transformOrFallback(Optional<T> optional, java.util.function.Function<T, R> transformer, R fallback) {
        // TODO: implement this
        return Optional.empty();
    }

    // TODO 5: Combine two Optional values
    // If both have values, combine them using the provided function
    // If either is empty, return Optional.empty()
    public <A, B, R> Optional<R> combineOptionals(Optional<A> a, Optional<B> b, java.util.function.BiFunction<A, B, R> combiner) {
        // TODO: implement this
        return Optional.empty();
    }

    // TODO 6: Handle optional with map chain
    // Given a string that might contain a number, parse it and double it
    // Return Optional.empty() if parsing fails
    public Optional<Integer> parseAndDouble(String input) {
        // TODO: implement this using Optional.map
        return Optional.empty();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        OptionalExercises exercises = new OptionalExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== OptionalExercises Tests ===\n");

        // Test 1
        total++;
        Optional<String> present = Optional.of("hello");
        Optional<String> empty = Optional.empty();
        String r1 = exercises.getOrDefault(present, "default");
        String r2 = exercises.getOrDefault(empty, "default");
        if ("hello".equals(r1) && "default".equals(r2)) {
            System.out.println("Test 1 PASSED: getOrDefault");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: getOrDefault - got [" + r1 + ", " + r2 + "]");
        }

        // Test 2
        total++;
        String manager1 = exercises.getManagerName("U001");
        String manager2 = exercises.getManagerName("U003");
        if ("Charlie".equals(manager1) && "".equals(manager2)) {
            System.out.println("Test 2 PASSED: getManagerName");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: getManagerName - got [" + manager1 + ", " + manager2 + "]");
        }

        // Test 3
        total++;
        Optional<Integer> filtered1 = exercises.filterOptional(Optional.of(10), x -> x > 5);
        Optional<Integer> filtered2 = exercises.filterOptional(Optional.of(3), x -> x > 5);
        Optional<Integer> filtered3 = exercises.filterOptional(Optional.empty(), x -> x > 5);
        if (filtered1.isPresent() && filtered1.get() == 10
            && filtered2.isEmpty()
            && filtered3.isEmpty()) {
            System.out.println("Test 3 PASSED: filterOptional");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: filterOptional");
        }

        // Test 4
        total++;
        Optional<String> transform1 = exercises.transformOrFallback(Optional.of(5), x -> "Number: " + x, "N/A");
        Optional<String> transform2 = exercises.transformOrFallback(Optional.empty(), x -> "Number: " + x, "N/A");
        if ("Number: 5".equals(transform1.orElse(null)) && "N/A".equals(transform2.orElse(null))) {
            System.out.println("Test 4 PASSED: transformOrFallback");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: transformOrFallback");
        }

        // Test 5
        total++;
        Optional<String> combined1 = exercises.combineOptionals(
            Optional.of("Hello"), Optional.of("World"), (a, b) -> a + " " + b);
        Optional<String> combined2 = exercises.combineOptionals(
            Optional.of("Hello"), Optional.empty(), (a, b) -> a + " " + b);
        if ("Hello World".equals(combined1.orElse(null)) && combined2.isEmpty()) {
            System.out.println("Test 5 PASSED: combineOptionals");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: combineOptionals");
        }

        // Test 6
        total++;
        Optional<Integer> parsed1 = exercises.parseAndDouble("21");
        Optional<Integer> parsed2 = exercises.parseAndDouble("abc");
        Optional<Integer> parsed3 = exercises.parseAndDouble(null);
        if (parsed1.isPresent() && parsed1.get() == 42
            && parsed2.isEmpty()
            && parsed3.isEmpty()) {
            System.out.println("Test 6 PASSED: parseAndDouble");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: parseAndDouble - got [" + parsed1 + ", " + parsed2 + ", " + parsed3 + "]");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
