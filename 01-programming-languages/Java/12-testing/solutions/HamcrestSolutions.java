package academy.javaengineering.testing.solutions;

/**
 * Hamcrest Solutions
 * Complete solutions for Hamcrest matchers exercises
 */
class HamcrestSolutions {

    // ============================================
    // Exercise 1: String Matchers Solution
    // ============================================

    /*
     * String Matchers Solution:
     * 
     * String str = "Hello World";
     * 
     * @Test
     * void testStringMatchers() {
     *     assertThat(str, containsString("Hello"));
     *     assertThat(str, containsStringIgnoringCase("hello"));
     *     assertThat(str, startsWith("Hello"));
     *     assertThat(str, endsWith("World"));
     *     assertThat(str, equalToIgnoringCase("hello world"));
     *     assertThat(str, hasLength(11));
     *     assertThat(str, not(emptyString()));
     *     assertThat(str, notNullValue());
     * }
     */

    // ============================================
    // Exercise 2: Number Matchers Solution
    // ============================================

    static class PriceCalculator {
        double calculatePrice(double basePrice, int quantity) {
            return basePrice * quantity;
        }
    }

    /*
     * Number Matchers Solution:
     * 
     * PriceCalculator calculator = new PriceCalculator();
     * 
     * @Test
     * void testNumberMatchers() {
     *     double price = calculator.calculatePrice(10.0, 5);
     * 
     *     assertThat(price, equalTo(50.0));
     *     assertThat(price, greaterThan(0.0));
     *     assertThat(price, lessThan(100.0));
     *     assertThat(price, greaterThanOrEqualTo(50.0));
     *     assertThat(price, lessThanOrEqualTo(50.0));
     *     assertThat(price, closeTo(50.0, 0.01));
     *     assertThat(price, between(0.0, 100.0));
     * }
     */

    // ============================================
    // Exercise 3: Collection Matchers Solution
    // ============================================

    static class Inventory {
        private final java.util.List<String> items = new java.util.ArrayList<>();

        void addItem(String item) { items.add(item); }
        void removeItem(String item) { items.remove(item); }
        java.util.List<String> getItems() { return new java.util.ArrayList<>(items); }
        int getItemCount() { return items.size(); }
    }

    /*
     * Collection Matchers Solution:
     * 
     * @Test
     * void testCollectionMatchers() {
     *     Inventory inventory = new Inventory();
     *     inventory.addItem("Laptop");
     *     inventory.addItem("Phone");
     *     inventory.addItem("Tablet");
     * 
     *     assertThat(inventory.getItems(), hasSize(3));
     *     assertThat(inventory.getItems(), contains("Laptop", "Phone", "Tablet"));
     *     assertThat(inventory.getItems(), containsInAnyOrder("Tablet", "Laptop", "Phone"));
     *     assertThat(inventory.getItems(), hasItem("Laptop"));
     *     assertThat(inventory.getItems(), hasItem(startsWith("Lap")));
     *     assertThat(inventory.getItems(), not(empty()));
     *     assertThat(inventory.getItems(), everyItem(notNullValue()));
     *     assertThat(inventory.getItems(), everyItem(hasLength(greaterThan(0))));
     * }
     */

    // ============================================
    // Exercise 4: Logical Matchers Solution
    // ============================================

    /*
     * Logical Matchers Solution:
     * 
     * @Test
     * void testLogicalMatchers() {
     *     int age = 25;
     * 
     *     // allOf - all conditions must match
     *     assertThat(age, allOf(greaterThan(18), lessThan(30)));
     * 
     *     // anyOf - at least one condition must match
     *     assertThat(age, anyOf(equalTo(25), equalTo(26), equalTo(27)));
     * 
     *     // not - negation
     *     assertThat(age, not(equalTo(30)));
     * 
     *     // both...and - chain two conditions
     *     assertThat(age, both(greaterThan(18)).and(lessThan(30)));
     * 
     *     // either...or - chain two conditions
     *     assertThat(age, either(equalTo(25)).or(equalTo(26)));
     * 
     *     // neither...nor - neither condition matches
     *     assertThat(age, neither(equalTo(20)).nor(equalTo(30)));
     * }
     */

    // ============================================
    // Exercise 5: Object Matchers Solution
    // ============================================

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

    /*
     * Object Matchers Solution:
     * 
     * @Test
     * void testObjectMatchers() {
     *     Person person = new Person("John", 30);
     * 
     *     // hasProperty - check bean property
     *     assertThat(person, hasProperty("name", "John"));
     *     assertThat(person, hasProperty("age", 30));
     * 
     *     // instanceOf - type checking
     *     assertThat(person, instanceOf(Person.class));
     * 
     *     // typeCompatibleWith - compatible type
     *     assertThat(person, typeCompatibleWith(Person.class));
     * 
     *     // hasToString - check toString output
     *     assertThat(person.toString(), containsString("John"));
     * 
     *     // samePropertyValuesAs - compare all properties
     *     Person expected = new Person("John", 30);
     *     assertThat(person, samePropertyValuesAs(expected));
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Hamcrest Solutions ===\n");

        System.out.println("--- String Matchers ---");
        System.out.println("assertThat(str, containsString(\"X\"))");
        System.out.println("assertThat(str, startsWith(\"X\"))");
        System.out.println("assertThat(str, endsWith(\"X\"))\n");

        System.out.println("--- Number Matchers ---");
        System.out.println("assertThat(num, greaterThan(10))");
        System.out.println("assertThat(num, closeTo(50.0, 0.01))\n");

        System.out.println("--- Collection Matchers ---");
        System.out.println("assertThat(list, hasSize(3))");
        System.out.println("assertThat(list, hasItem(\"X\"))");
        System.out.println("assertThat(list, everyItem(notNullValue()))\n");

        System.out.println("--- Logical Matchers ---");
        System.out.println("assertThat(x, allOf(condition1, condition2))");
        System.out.println("assertThat(x, anyOf(condition1, condition2))\n");

        System.out.println("--- Object Matchers ---");
        System.out.println("assertThat(obj, hasProperty(\"name\", \"John\"))");
        System.out.println("assertThat(obj, instanceOf(Class.class))");

        System.out.println("\n=== All solutions completed ===");
    }
}
