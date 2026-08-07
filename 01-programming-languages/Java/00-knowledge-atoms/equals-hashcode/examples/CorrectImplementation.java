import java.util.Objects;

public class CorrectImplementation {
    public static void main(String[] args) {
        System.out.println("=== Correct equals() and hashCode() Implementation ===\n");
        
        // Example 1: Basic equality
        System.out.println("1. Basic Equality:");
        Person p1 = new Person("Alice", 30, "alice@email.com");
        Person p2 = new Person("Alice", 30, "alice@email.com");
        Person p3 = new Person("Bob", 25, "bob@email.com");
        
        System.out.println("   p1: " + p1);
        System.out.println("   p2: " + p2);
        System.out.println("   p3: " + p3);
        System.out.println("   p1.equals(p2) = " + p1.equals(p2));
        System.out.println("   p1.equals(p3) = " + p1.equals(p3));
        System.out.println("   p1.hashCode() == p2.hashCode() = " + (p1.hashCode() == p2.hashCode()));
        System.out.println();
        
        // Example 2: Reflexive property
        System.out.println("2. Reflexive Property (x.equals(x) must be true):");
        System.out.println("   p1.equals(p1) = " + p1.equals(p1));
        System.out.println();
        
        // Example 3: Symmetric property
        System.out.println("3. Symmetric Property (if x.equals(y) then y.equals(x)):");
        System.out.println("   p1.equals(p2) = " + p1.equals(p2));
        System.out.println("   p2.equals(p1) = " + p2.equals(p1));
        System.out.println();
        
        // Example 4: Null handling
        System.out.println("4. Null Handling:");
        System.out.println("   p1.equals(null) = " + p1.equals(null));
        System.out.println("   p1.equals(\"Alice\") = " + p1.equals("Alice"));
        System.out.println("   p1.equals(42) = " + p1.equals(42));
        System.out.println();
        
        // Example 5: HashMap behavior
        System.out.println("5. HashMap Behavior with Correct Implementation:");
        java.util.Map<Person, String> map = new java.util.HashMap<>();
        map.put(p1, "Engineer");
        map.put(p3, "Designer");
        
        System.out.println("   Map size: " + map.size());
        System.out.println("   map.get(p1) = " + map.get(p1));
        System.out.println("   map.get(p2) = " + map.get(p2));
        System.out.println("   map.get(p3) = " + map.get(p3));
        System.out.println("   map.containsKey(p2) = " + map.containsKey(p2));
        System.out.println();
        
        // Example 6: HashSet behavior
        System.out.println("6. HashSet Behavior with Correct Implementation:");
        java.util.Set<Person> set = new java.util.HashSet<>();
        set.add(p1);
        set.add(p2);
        set.add(p3);
        
        System.out.println("   Set size: " + set.size());
        System.out.println("   set.contains(p1) = " + set.contains(p1));
        System.out.println("   set.contains(p2) = " + set.contains(p2));
        System.out.println("   set.contains(p3) = " + set.contains(p3));
        System.out.println("   Note: p2 is not added because p1 already exists and equals(p2) is true!");
    }
    
    // Correctly implemented Person class
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
