import java.util.HashMap;
import java.util.Map;

public class HashCollisionDemo {
    public static void main(String[] args) {
        System.out.println("=== Hash Collision Demo ===\n");
        
        // Example 1: String hash collisions
        System.out.println("1. String Hash Collisions:");
        System.out.println("   \"Aa\" and \"BB\" have the same hashCode:");
        String a = "Aa";
        String b = "BB";
        System.out.println("   a.hashCode() = " + a.hashCode());
        System.out.println("   b.hashCode() = " + b.hashCode());
        System.out.println("   a.equals(b) = " + a.equals(b));
        System.out.println("   They are different strings despite same hashCode!\n");
        
        // Example 2: More string collisions
        System.out.println("2. More String Collisions:");
        String[] strings = {"Aa", "BB", "Ba", "CB", "Dn", "Em"};
        for (int i = 0; i < strings.length; i++) {
            for (int j = i + 1; j < strings.length; j++) {
                if (strings[i].hashCode() == strings[j].hashCode()) {
                    System.out.println("   \"" + strings[i] + "\" and \"" + strings[j] + 
                        "\" have same hashCode: " + strings[i].hashCode());
                }
            }
        }
        System.out.println();
        
        // Example 3: Custom objects with same hashCode
        System.out.println("3. Custom Objects with Same hashCode:");
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Alice", 30);
        Person p3 = new Person("Bob", 25);
        
        System.out.println("   p1: name=" + p1.getName() + ", age=" + p1.getAge());
        System.out.println("   p2: name=" + p2.getName() + ", age=" + p2.getAge());
        System.out.println("   p3: name=" + p3.getName() + ", age=" + p3.getAge());
        System.out.println("   p1.hashCode() = " + p1.hashCode());
        System.out.println("   p2.hashCode() = " + p2.hashCode());
        System.out.println("   p3.hashCode() = " + p3.hashCode());
        System.out.println("   p1.equals(p2) = " + p1.equals(p2));
        System.out.println("   p1.equals(p3) = " + p1.equals(p3));
        System.out.println();
        
        // Example 4: HashMap behavior with collisions
        System.out.println("4. HashMap Behavior:");
        Map<Person, String> map = new HashMap<>();
        map.put(p1, "Engineer");
        map.put(p3, "Designer");
        
        System.out.println("   Map size: " + map.size());
        System.out.println("   map.get(p1) = " + map.get(p1));
        System.out.println("   map.get(p2) = " + map.get(p2));  // p2 equals p1
        System.out.println("   map.get(p3) = " + map.get(p3));
        System.out.println("   Note: p2 finds p1's value because they are equal!");
        System.out.println();
        
        // Example 5: Visualizing hash buckets
        System.out.println("5. Hash Bucket Visualization:");
        System.out.println("   HashMap uses hashCode() to find bucket:");
        System.out.println("   Bucket 0: [ ]");
        System.out.println("   Bucket 1: [ ]");
        System.out.println("   Bucket 2: [p1 -> \"Engineer\", p2]  <- Collision! Same bucket");
        System.out.println("   Bucket 3: [p3 -> \"Designer\"]");
        System.out.println("   When you call get(p2), HashMap:");
        System.out.println("     1. Calculates p2.hashCode()");
        System.out.println("     2. Finds bucket 2");
        System.out.println("     3. Iterates through bucket, using equals() to find p1");
        System.out.println("     4. Returns \"Engineer\"");
    }
    
    // Simple Person class for demonstration
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
            return age == person.age && java.util.Objects.equals(name, person.name);
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, age);
        }
    }
}
