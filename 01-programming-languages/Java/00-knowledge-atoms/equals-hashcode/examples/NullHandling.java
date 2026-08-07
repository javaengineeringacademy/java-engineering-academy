import java.util.Objects;

public class NullHandling {
    public static void main(String[] args) {
        System.out.println("=== Null Handling in equals() ===\n");
        
        // Example 1: equals() with null
        System.out.println("1. equals() with null:");
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person(null, 30);
        Person p3 = new Person("Alice", 0);
        
        System.out.println("   p1: name=" + p1.getName() + ", age=" + p1.getAge());
        System.out.println("   p2: name=" + p2.getName() + ", age=" + p2.getAge());
        System.out.println("   p3: name=" + p3.getName() + ", age=" + p3.getAge());
        System.out.println();
        
        System.out.println("   p1.equals(null) = " + p1.equals(null));
        System.out.println("   p1.equals(\"Alice\") = " + p1.equals("Alice"));
        System.out.println("   p1.equals(42) = " + p1.equals(42));
        System.out.println();
        
        // Example 2: Comparing with null fields
        System.out.println("2. Comparing Objects with Null Fields:");
        System.out.println("   p1.equals(p2) = " + p1.equals(p2));
        System.out.println("   p2.equals(p1) = " + p2.equals(p1));
        System.out.println("   p1.hashCode() = " + p1.hashCode());
        System.out.println("   p2.hashCode() = " + p2.hashCode());
        System.out.println();
        
        // Example 3: Multiple null fields
        System.out.println("3. Multiple Null Fields:");
        Person p4 = new Person(null, 0);
        Person p5 = new Person(null, 0);
        Person p6 = new Person(null, 30);
        
        System.out.println("   p4: name=" + p4.getName() + ", age=" + p4.getAge());
        System.out.println("   p5: name=" + p5.getName() + ", age=" + p5.getAge());
        System.out.println("   p6: name=" + p6.getName() + ", age=" + p6.getAge());
        System.out.println("   p4.equals(p5) = " + p4.equals(p5));
        System.out.println("   p4.equals(p6) = " + p4.equals(p6));
        System.out.println("   p4.hashCode() = " + p4.hashCode());
        System.out.println("   p5.hashCode() = " + p5.hashCode());
        System.out.println();
        
        // Example 4: Demonstrating Objects.equals() behavior
        System.out.println("4. Objects.equals() Behavior:");
        System.out.println("   Objects.equals(null, null) = " + Objects.equals(null, null));
        System.out.println("   Objects.equals(\"Alice\", null) = " + Objects.equals("Alice", null));
        System.out.println("   Objects.equals(null, \"Alice\") = " + Objects.equals(null, "Alice"));
        System.out.println("   Objects.equals(\"Alice\", \"Alice\") = " + Objects.equals("Alice", "Alice"));
        System.out.println();
        
        // Example 5: Common null-related bugs
        System.out.println("5. Common Null-Related Bugs:");
        System.out.println("   Bug 1: Using .equals() on potentially null field");
        System.out.println("     WRONG: return this.name.equals(other.name);  // NPE if name is null");
        System.out.println("     RIGHT: return Objects.equals(this.name, other.name);");
        System.out.println();
        System.out.println("   Bug 2: Forgetting null check in equals()");
        System.out.println("     WRONG: return this.name.equals(other.name) && this.age == other.age;");
        System.out.println("     RIGHT: if (o == null || getClass() != o.getClass()) return false;");
        System.out.println();
        System.out.println("   Bug 3: Not handling null in hashCode()");
        System.out.println("     WRONG: return Objects.hash(name, age);  // Works fine with null!");
        System.out.println("     Note: Objects.hash() handles null values correctly.");
        System.out.println();
        
        // Example 6: HashSet and HashMap with null keys
        System.out.println("6. Collections with Null Keys:");
        java.util.Set<Person> set = new java.util.HashSet<>();
        set.add(p1);
        set.add(p2);
        set.add(p4);
        set.add(p5);
        
        System.out.println("   Set size: " + set.size());
        System.out.println("   set.contains(p1) = " + set.contains(p1));
        System.out.println("   set.contains(p2) = " + set.contains(p2));
        System.out.println("   set.contains(p4) = " + set.contains(p4));
        System.out.println("   set.contains(p5) = " + set.contains(p5));
        System.out.println();
        
        java.util.Map<Person, String> map = new java.util.HashMap<>();
        map.put(p1, "Alice");
        map.put(p2, "Alice with null name");
        map.put(p4, "Null person");
        map.put(p5, "Another null person");
        
        System.out.println("   Map size: " + map.size());
        System.out.println("   map.get(p1) = " + map.get(p1));
        System.out.println("   map.get(p2) = " + map.get(p2));
        System.out.println("   map.get(p4) = " + map.get(p4));
        System.out.println("   map.get(p5) = " + map.get(p5));
    }
    
    // Person class with proper null handling
    static class Person {
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}
