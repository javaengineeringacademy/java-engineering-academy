import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class PersonExercise {
    public static void main(String[] args) {
        System.out.println("=== Person Exercise ===\n");
        
        // TODO: Implement the Person class below
        // Then run this file to test your implementation
        
        System.out.println("Exercise Instructions:");
        System.out.println("1. Implement the Person class with equals() and hashCode()");
        System.out.println("2. Uncomment the test code below");
        System.out.println("3. Run this file to verify your implementation");
        System.out.println();
        
        // Uncomment the following code after implementing Person class:
        /*
        Person p1 = new Person("Alice", 30, "alice@email.com");
        Person p2 = new Person("Alice", 30, "alice@email.com");
        Person p3 = new Person("Bob", 25, "bob@email.com");
        Person p4 = new Person(null, 30, "alice@email.com");
        
        // Test equals()
        System.out.println("Testing equals():");
        System.out.println("p1.equals(p2) = " + p1.equals(p2) + " (expected: true)");
        System.out.println("p1.equals(p3) = " + p1.equals(p3) + " (expected: false)");
        System.out.println("p1.equals(null) = " + p1.equals(null) + " (expected: false)");
        System.out.println("p1.equals(\"Alice\") = " + p1.equals("Alice") + " (expected: false)");
        System.out.println("p1.equals(p1) = " + p1.equals(p1) + " (expected: true)");
        System.out.println("p1.equals(p2) == p2.equals(p1) = " + (p1.equals(p2) == p2.equals(p1)) + " (expected: true)");
        System.out.println();
        
        // Test hashCode()
        System.out.println("Testing hashCode():");
        System.out.println("p1.hashCode() = " + p1.hashCode());
        System.out.println("p2.hashCode() = " + p2.hashCode());
        System.out.println("p3.hashCode() = " + p3.hashCode());
        System.out.println("p1.hashCode() == p2.hashCode() = " + (p1.hashCode() == p2.hashCode()) + " (expected: true)");
        System.out.println();
        
        // Test with collections
        System.out.println("Testing with HashSet:");
        Set<Person> set = new HashSet<>();
        set.add(p1);
        set.add(p2);
        System.out.println("Set size after adding p1 and p2: " + set.size() + " (expected: 1)");
        System.out.println("set.contains(p2) = " + set.contains(p2) + " (expected: true)");
        System.out.println();
        
        System.out.println("Testing with HashMap:");
        Map<Person, String> map = new HashMap<>();
        map.put(p1, "Engineer");
        System.out.println("map.get(p2) = " + map.get(p2) + " (expected: Engineer)");
        System.out.println("map.size() = " + map.size() + " (expected: 1)");
        System.out.println();
        
        System.out.println("All tests passed!");
        */
        
        System.out.println("TODO: Implement Person class and uncomment test code");
    }
    
    // TODO: Implement the Person class here
    // Requirements:
    // 1. Fields: String name, int age, String email
    // 2. Constructor with all fields
    // 3. Getters for all fields
    // 4. equals() method that compares all fields
    // 5. hashCode() method using Objects.hash()
    // 6. toString() method for debugging
}
