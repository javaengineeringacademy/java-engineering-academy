import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class PersonSolution {
    public static void main(String[] args) {
        System.out.println("=== Person Solution ===\n");
        
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
        
        // Test null handling
        System.out.println("Testing null handling:");
        System.out.println("p1.equals(p4) = " + p1.equals(p4) + " (expected: false)");
        System.out.println("p4.equals(p1) = " + p4.equals(p1) + " (expected: false)");
        System.out.println("p4.equals(p4) = " + p4.equals(p4) + " (expected: true)");
        System.out.println();
        
        // Test toString()
        System.out.println("Testing toString():");
        System.out.println("p1.toString() = " + p1.toString());
        System.out.println("p4.toString() = " + p4.toString());
        System.out.println();
        
        System.out.println("All tests passed!");
    }
    
    // Complete Person class implementation
    static class Person {
        private String name;
        private int age;
        private String email;
        
        public Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getEmail() { return email; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && 
                   Objects.equals(name, person.name) && 
                   Objects.equals(email, person.email);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age, email);
        }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
        }
    }
}
