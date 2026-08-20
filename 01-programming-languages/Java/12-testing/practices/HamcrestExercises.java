package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Hamcrest Exercises
 * Practice Hamcrest matchers
 */
class HamcrestExercises {

    // ============================================
    // Exercise 1: String Matchers
    // ============================================

    /*
     * TODO: Practice string matchers
     * 
     * String str = "Hello World";
     * 
     * assertThat(str, containsString("Hello"));
     * assertThat(str, startsWith("Hello"));
     * assertThat(str, endsWith("World"));
     * assertThat(str, equalToIgnoringCase("hello world"));
     * assertThat(str, hasLength(11));
     */

    // ============================================
    // Exercise 2: Number Matchers
    // ============================================

    static class PriceCalculator {
        double calculatePrice(double basePrice, int quantity) {
            return basePrice * quantity;
        }
    }

    /*
     * TODO: Practice number matchers
     * 
     * double price = calculator.calculatePrice(10.0, 5);
     * 
     * assertThat(price, equalTo(50.0));
     * assertThat(price, greaterThan(0.0));
     * assertThat(price, lessThan(100.0));
     * assertThat(price, closeTo(50.0, 0.01));
     */

    // ============================================
    // Exercise 3: Collection Matchers
    // ============================================

    static class Inventory {
        private final List<String> items = new ArrayList<>();

        void addItem(String item) { items.add(item); }
        void removeItem(String item) { items.remove(item); }
        List<String> getItems() { return new ArrayList<>(items); }
        int getItemCount() { return items.size(); }
    }

    /*
     * TODO: Practice collection matchers
     * 
     * assertThat(inventory.getItems(), hasSize(3));
     * assertThat(inventory.getItems(), contains("Laptop", "Phone", "Tablet"));
     * assertThat(inventory.getItems(), hasItem("Laptop"));
     * assertThat(inventory.getItems(), not(empty()));
     * assertThat(inventory.getItems(), everyItem(notNullValue()));
     */

    // ============================================
    // Exercise 4: Logical Matchers
    // ============================================

    /*
     * TODO: Practice logical matchers
     * 
     * int age = 25;
     * 
     * assertThat(age, allOf(greaterThan(18), lessThan(30)));
     * assertThat(age, anyOf(equalTo(25), equalTo(26)));
     * assertThat(age, not(equalTo(30)));
     * assertThat(age, both(greaterThan(18)).and(lessThan(30)));
     */

    // ============================================
    // Exercise 5: Object Matchers
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
     * TODO: Practice object matchers
     * 
     * Person person = new Person("John", 30);
     * 
     * assertThat(person, hasProperty("name", "John"));
     * assertThat(person, hasProperty("age", 30));
     * assertThat(person, instanceOf(Person.class));
     */

    public static void main(String[] args) {
        System.out.println("=== Hamcrest Exercises ===");
        System.out.println("Practice Hamcrest matchers.");
    }
}
