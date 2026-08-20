package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * AssertJ Exercises
 * Practice fluent assertions
 */
class AssertjExercises {

    // ============================================
    // Exercise 1: String Assertions
    // ============================================

    /*
     * TODO: Practice AssertJ string assertions
     * 
     * String name = "John Doe";
     * 
     * assertThat(name)
     *     .isEqualTo("John Doe")
     *     .startsWith("John")
     *     .endsWith("Doe")
     *     .contains("oh")
     *     .hasSize(8)
     *     .isNotEmpty()
     *     .matches("John.*");
     */

    // ============================================
    // Exercise 2: Collection Assertions
    // ============================================

    static class ProductService {
        private final List<String> products = new ArrayList<>();

        void addProduct(String product) { products.add(product); }
        void removeProduct(String product) { products.remove(product); }
        List<String> getProducts() { return new ArrayList<>(products); }
        int getProductCount() { return products.size(); }
    }

    /*
     * TODO: Practice collection assertions
     * 
     * assertThat(products)
     *     .hasSize(3)
     *     .contains("Laptop", "Phone")
     *     .doesNotContain("TV")
     *     .containsExactly("Laptop", "Phone", "Tablet")
     *     .filteredOn(p -> p.length() > 4)
     *     .hasSize(3);
     */

    // ============================================
    // Exercise 3: Object Assertions
    // ============================================

    static class Employee {
        String name;
        String department;
        double salary;

        Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
    }

    /*
     * TODO: Practice object assertions
     * 
     * Employee emp = new Employee("John", "Engineering", 75000);
     * 
     * assertThat(emp)
     *     .isNotNull()
     *     .isInstanceOf(Employee.class)
     *     .satisfies(e -> {
     *         assertThat(e.name).isEqualTo("John");
     *         assertThat(e.department).isEqualTo("Engineering");
     *     });
     */

    // ============================================
    // Exercise 4: Exception Assertions
    // ============================================

    static class Validator {
        static void validateAge(int age) {
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
            if (age > 150) throw new IllegalArgumentException("Age seems unrealistic");
        }
    }

    /*
     * TODO: Practice exception assertions
     * 
     * assertThatThrownBy(() -> Validator.validateAge(-1))
     *     .isInstanceOf(IllegalArgumentException.class)
     *     .hasMessageContaining("negative");
     * 
     * assertThatIllegalArgumentException()
     *     .isThrownBy(() -> Validator.validateAge(200))
     *     .withMessageContaining("unrealistic");
     */

    // ============================================
    // Exercise 5: Map Assertions
    // ============================================

    static class Scores {
        private final Map<String, Integer> scores = new HashMap<>();

        void addScore(String student, int score) { scores.put(student, score); }
        int getScore(String student) { return scores.getOrDefault(student, 0); }
        Map<String, Integer> getAllScores() { return new HashMap<>(scores); }
    }

    /*
     * TODO: Practice map assertions
     * 
     * assertThat(scores)
     *     .hasSize(3)
     *     .containsEntry("Alice", 95)
     *     .containsKey("Bob")
     *     .containsValue(87)
     *     .doesNotContainKey("Charlie")
     *     .allSatisfy((name, score) -> {
     *         assertThat(score).isPositive();
     *     });
     */

    public static void main(String[] args) {
        System.out.println("=== AssertJ Exercises ===");
        System.out.println("Practice fluent assertions with AssertJ.");
    }
}
