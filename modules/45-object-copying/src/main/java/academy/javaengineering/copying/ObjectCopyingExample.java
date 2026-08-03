package academy.javaengineering.copying;

import java.io.*;
import java.util.*;

/**
 * Comprehensive examples of Object Copying in Java.
 */
public class ObjectCopyingExample {

    // ==================== BASIC SHALLOW COPY ====================
    
    /**
     * Simple class demonstrating shallow copy.
     */
    static class Point implements Cloneable {
        private int x;
        private int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
        
        @Override
        public Point clone() {
            try {
                return (Point) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String toString() {
            return "Point{x=" + x + ", y=" + y + "}";
        }
    }
    
    /**
     * Demonstrates shallow copy behavior.
     */
    public static void demonstrateShallowCopy() {
        System.out.println("=== Shallow Copy Demo ===");
        
        Point original = new Point(10, 20);
        Point copy = original.clone();
        
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        System.out.println("Same reference? " + (original == copy));
        
        // Modify copy
        copy.setX(99);
        
        System.out.println("\nAfter modifying copy:");
        System.out.println("Original: " + original); // Unchanged (primitives)
        System.out.println("Copy: " + copy);
    }

    // ==================== DEEP COPY WITH NESTED OBJECTS ====================
    
    /**
     * Address class that can be deep copied.
     */
    static class Address implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;
        private String street;
        private String city;
        private String zipCode;
        
        public Address(String street, String city, String zipCode) {
            this.street = street;
            this.city = city;
            this.zipCode = zipCode;
        }
        
        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getZipCode() { return zipCode; }
        
        public void setCity(String city) { this.city = city; }
        
        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String toString() {
            return "Address{street='" + street + "', city='" + city + 
                   "', zip='" + zipCode + "'}";
        }
    }
    
    /**
     * Person class with nested mutable object.
     */
    static class Person implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;
        private Address address;
        private List<String> hobbies;
        
        public Person(String name, int age, Address address, List<String> hobbies) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.hobbies = new ArrayList<>(hobbies);
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        public Address getAddress() { return address; }
        public List<String> getHobbies() { return new ArrayList<>(hobbies); }
        
        // Shallow copy (reference copy)
        public Person shallowCopy() {
            return this; // Just returns same reference
        }
        
        // Deep copy using clone
        @Override
        public Person clone() {
            try {
                Person copy = (Person) super.clone();
                copy.address = this.address.clone(); // Deep copy address
                copy.hobbies = new ArrayList<>(this.hobbies); // Deep copy list
                return copy;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        
        // Deep copy using copy constructor
        public Person(Person other) {
            this.name = other.name;
            this.age = other.age;
            this.address = new Address(
                other.address.getStreet(),
                other.address.getCity(),
                other.address.getZipCode()
            );
            this.hobbies = new ArrayList<>(other.hobbies);
        }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + 
                   ", address=" + address + ", hobbies=" + hobbies + "}";
        }
    }
    
    /**
     * Demonstrates deep copy behavior.
     */
    public static void demonstrateDeepCopy() {
        System.out.println("\n=== Deep Copy Demo ===");
        
        Address addr = new Address("123 Main St", "New York", "10001");
        Person original = new Person("John", 30, addr, 
            Arrays.asList("reading", "coding"));
        
        // Clone-based deep copy
        Person cloneCopy = original.clone();
        
        // Constructor-based deep copy
        Person constructorCopy = new Person(original);
        
        System.out.println("Original: " + original);
        System.out.println("Clone Copy: " + cloneCopy);
        System.out.println("Constructor Copy: " + constructorCopy);
        
        // Modify original's address
        original.getAddress().setCity("Boston");
        
        System.out.println("\nAfter modifying original's city:");
        System.out.println("Original: " + original);
        System.out.println("Clone Copy: " + cloneCopy); // Independent
        System.out.println("Constructor Copy: " + constructorCopy); // Independent
    }

    // ==================== COLLECTION COPYING ====================
    
    /**
     * Demonstrates copying collections.
     */
    public static void demonstrateCollectionCopy() {
        System.out.println("\n=== Collection Copy Demo ===");
        
        List<String> original = new ArrayList<>(Arrays.asList("A", "B", "C"));
        
        // Reference copy (same list)
        List<String> referenceCopy = original;
        
        // Shallow copy (new list, same elements)
        List<String> shallowCopy = new ArrayList<>(original);
        
        // Deep copy (new list, new elements)
        List<String> deepCopy = new ArrayList<>();
        for (String s : original) {
            deepCopy.add(new String(s)); // New String objects
        }
        
        System.out.println("Original: " + original);
        System.out.println("Reference: " + referenceCopy);
        System.out.println("Shallow: " + shallowCopy);
        System.out.println("Deep: " + deepCopy);
        
        // Modify original
        original.add("D");
        
        System.out.println("\nAfter adding 'D' to original:");
        System.out.println("Original: " + original);
        System.out.println("Reference: " + referenceCopy); // Same
        System.out.println("Shallow: " + shallowCopy); // Unchanged
        System.out.println("Deep: " + deepCopy); // Unchanged
    }

    // ==================== SERIALIZATION-BASED DEEP COPY ====================
    
    /**
     * Generic deep copy using serialization.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopySerialization(T object) 
            throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }
    
    /**
     * Demonstrates serialization-based deep copy.
     */
    public static void demonstrateSerializationCopy() throws Exception {
        System.out.println("\n=== Serialization-Based Deep Copy Demo ===");
        
        Address addr = new Address("456 Oak Ave", "Chicago", "60601");
        Person original = new Person("Alice", 25, addr, 
            Arrays.asList("travel", "photography"));
        
        // Deep copy using serialization
        Person copy = deepCopySerialization(original);
        
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        System.out.println("Same object? " + (original == copy));
        
        // Modify original
        original.getAddress().setCity("Miami");
        
        System.out.println("\nAfter modifying original:");
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy); // Independent
    }

    // ==================== CIRCULAR REFERENCES ====================
    
    /**
     * Node class with circular references.
     */
    static class Node implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value;
        private List<Node> neighbors;
        
        public Node(String value) {
            this.value = value;
            this.neighbors = new ArrayList<>();
        }
        
        public void addNeighbor(Node node) {
            neighbors.add(node);
        }
        
        public String getValue() { return value; }
        public List<Node> getNeighbors() { return neighbors; }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node{value='").append(value).append("', neighbors=[");
            for (int i = 0; i < neighbors.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(neighbors.get(i).getValue());
            }
            sb.append("]}");
            return sb.toString();
        }
    }
    
    /**
     * Demonstrates handling circular references in deep copy.
     */
    public static void demonstrateCircularReferences() throws Exception {
        System.out.println("\n=== Circular References Demo ===");
        
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        
        a.addNeighbor(b);
        b.addNeighbor(c);
        c.addNeighbor(a); // Circular reference!
        
        System.out.println("Original graph:");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        
        // Deep copy using serialization (handles circular references)
        Node copyA = deepCopySerialization(a);
        
        System.out.println("\nCopied graph:");
        System.out.println(copyA);
        
        // Verify independence
        a.addNeighbor(new Node("D"));
        
        System.out.println("\nAfter adding D to original:");
        System.out.println("Original A neighbors: " + a.getNeighbors().size());
        System.out.println("Copy A neighbors: " + copyA.getNeighbors().size());
    }

    // ==================== MAP COPYING ====================
    
    /**
     * Demonstrates copying maps.
     */
    public static void demonstrateMapCopy() {
        System.out.println("\n=== Map Copy Demo ===");
        
        Map<String, List<Integer>> original = new HashMap<>();
        original.put("numbers", new ArrayList<>(Arrays.asList(1, 2, 3)));
        original.put("letters", new ArrayList<>(Arrays.asList(65, 66, 67)));
        
        // Reference copy
        Map<String, List<Integer>> referenceCopy = original;
        
        // Shallow copy (new map, same value references)
        Map<String, List<Integer>> shallowCopy = new HashMap<>(original);
        
        // Deep copy
        Map<String, List<Integer>> deepCopy = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : original.entrySet()) {
            deepCopy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        
        System.out.println("Original: " + original);
        
        // Modify original
        original.get("numbers").add(4);
        
        System.out.println("\nAfter adding 4 to numbers:");
        System.out.println("Original: " + original);
        System.out.println("Reference: " + referenceCopy);
        System.out.println("Shallow: " + shallowCopy); // Affected!
        System.out.println("Deep: " + deepCopy); // Independent
    }

    // ==================== IMMUTABLE COPY ====================
    
    /**
     * Immutable class (no copying needed for thread safety).
     */
    static final class ImmutablePoint {
        private final int x;
        private final int y;
        
        public ImmutablePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        
        @Override
        public String toString() {
            return "ImmutablePoint{x=" + x + ", y=" + y + "}";
        }
    }
    
    /**
     * Demonstrates immutable objects.
     */
    public static void demonstrateImmutableCopy() {
        System.out.println("\n=== Immutable Object Demo ===");
        
        ImmutablePoint original = new ImmutablePoint(10, 20);
        ImmutablePoint copy = original; // Reference copy is safe!
        
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        System.out.println("Same reference? " + (original == copy));
        
        // No modification possible - thread safe!
        System.out.println("Immutable objects can be shared safely");
    }

    // ==================== MAIN DEMONSTRATION ====================
    
    public static void main(String[] args) {
        System.out.println("=== Object Copying Examples ===");
        
        demonstrateShallowCopy();
        demonstrateDeepCopy();
        demonstrateCollectionCopy();
        
        try {
            demonstrateSerializationCopy();
            demonstrateCircularReferences();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        demonstrateMapCopy();
        demonstrateImmutableCopy();
        
        System.out.println("\n=== All demos completed ===");
    }
}
