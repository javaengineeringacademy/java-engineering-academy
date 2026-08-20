package academy.javaengineering.testing.solutions;

/**
 * AssertJ Solutions
 * Complete solutions for fluent assertions exercises
 */
class AssertjSolutions {

    // ============================================
    // Exercise 1: String Assertions Solution
    // ============================================

    /*
     * String Assertions Solution:
     * 
     * String name = "John Doe";
     * String email = "john@example.com";
     * String empty = "";
     * String nullStr = null;
     * 
     * @Test
     * void testStringAssertions() {
     *     assertThat(name)
     *         .isEqualTo("John Doe")
     *         .startsWith("John")
     *         .endsWith("Doe")
     *         .contains("oh")
     *         .hasSize(8)
     *         .isNotEmpty()
     *         .matches("John.*");
     * 
     *     assertThat(email)
     *         .contains("@")
     *         .containsIgnoringCase("EXAMPLE")
     *         .doesNotContain("spam");
     * 
     *     assertThat(empty).isEmpty();
     *     assertThat(nullStr).isNull();
     *     assertThat(name).isNotNull();
     * }
     */

    // ============================================
    // Exercise 2: Collection Assertions Solution
    // ============================================

    static class ProductService {
        private final java.util.List<String> products = new java.util.ArrayList<>();

        void addProduct(String product) { products.add(product); }
        void removeProduct(String product) { products.remove(product); }
        java.util.List<String> getProducts() { return new java.util.ArrayList<>(products); }
        int getProductCount() { return products.size(); }
    }

    /*
     * Collection Assertions Solution:
     * 
     * @Test
     * void testCollectionAssertions() {
     *     ProductService service = new ProductService();
     *     service.addProduct("Laptop");
     *     service.addProduct("Phone");
     *     service.addProduct("Tablet");
     * 
     *     assertThat(service.getProducts())
     *         .hasSize(3)
     *         .contains("Laptop", "Phone")
     *         .doesNotContain("TV")
     *         .containsExactly("Laptop", "Phone", "Tablet")
     *         .filteredOn(p -> p.length() > 4)
     *         .hasSize(3);
     * 
     *     assertThat(service.getProducts())
     *         .allSatisfy(product -> {
     *             assertThat(product).isNotEmpty();
     *             assertThat(product).isNotBlank();
     *         });
     * }
     */

    // ============================================
    // Exercise 3: Object Assertions Solution
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
     * Object Assertions Solution:
     * 
     * @Test
     * void testObjectAssertions() {
     *     Employee emp = new Employee("John", "Engineering", 75000);
     * 
     *     assertThat(emp)
     *         .isNotNull()
     *         .isInstanceOf(Employee.class);
     * 
     *     assertThat(emp)
     *         .satisfies(e -> {
     *             assertThat(e.name).isEqualTo("John");
     *             assertThat(e.department).isEqualTo("Engineering");
     *             assertThat(e.salary).isGreaterThan(0);
     *         });
     * 
     *     assertThat(emp.name)
     *         .isEqualTo("John")
     *         .hasSize(4)
     *         .startsWith("J");
     * }
     */

    // ============================================
    // Exercise 4: Exception Assertions Solution
    // ============================================

    static class Validator {
        static void validateAge(int age) {
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
            if (age > 150) throw new IllegalArgumentException("Age seems unrealistic");
        }
    }

    /*
     * Exception Assertions Solution:
     * 
     * @Test
     * void testExceptionAssertions() {
     *     assertThatThrownBy(() -> Validator.validateAge(-1))
     *         .isInstanceOf(IllegalArgumentException.class)
     *         .hasMessageContaining("negative");
     * 
     *     assertThatIllegalArgumentException()
     *         .isThrownBy(() -> Validator.validateAge(200))
     *         .withMessageContaining("unrealistic");
     * 
     *     assertThatThrownBy(() -> Validator.validateAge(25))
     *         .doesNotThrowAnyException();
     * }
     */

    // ============================================
    // Exercise 5: Map Assertions Solution
    // ============================================

    static class Scores {
        private final java.util.Map<String, Integer> scores = new java.util.HashMap<>();

        void addScore(String student, int score) { scores.put(student, score); }
        int getScore(String student) { return scores.getOrDefault(student, 0); }
        java.util.Map<String, Integer> getAllScores() { return new java.util.HashMap<>(scores); }
    }

    /*
     * Map Assertions Solution:
     * 
     * @Test
     * void testMapAssertions() {
     *     Scores scores = new Scores();
     *     scores.addScore("Alice", 95);
     *     scores.addScore("Bob", 87);
     *     scores.addScore("Charlie", 92);
     * 
     *     assertThat(scores.getAllScores())
     *         .hasSize(3)
     *         .containsEntry("Alice", 95)
     *         .containsKey("Bob")
     *         .containsValue(87)
     *         .doesNotContainKey("David")
     *         .allSatisfy((name, score) -> {
     *             assertThat(score).isPositive();
     *             assertThat(name).isNotEmpty();
     *         });
     * 
     *     assertThat(scores.getAllScores().values())
     *         .allMatch(score -> score > 0 && score <= 100);
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== AssertJ Solutions ===\n");

        System.out.println("--- String Assertions ---");
        System.out.println("assertThat(str).isEqualTo(\"X\").startsWith(\"X\").contains(\"X\")\n");

        System.out.println("--- Collection Assertions ---");
        System.out.println("assertThat(list).hasSize(3).contains(\"X\").filteredOn(...)\n");

        System.out.println("--- Object Assertions ---");
        System.out.println("assertThat(obj).isNotNull().satisfies(o -> { ... })\n");

        System.out.println("--- Exception Assertions ---");
        System.out.println("assertThatThrownBy(() -> ...).isInstanceOf(X.class)\n");

        System.out.println("--- Map Assertions ---");
        System.out.println("assertThat(map).containsEntry(\"key\", value).hasSize(3)");

        System.out.println("\n=== All solutions completed ===");
    }
}
