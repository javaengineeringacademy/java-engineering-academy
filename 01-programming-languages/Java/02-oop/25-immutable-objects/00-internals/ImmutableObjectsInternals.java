package academy.javaengineering.oop.internals;

public class ImmutableObjectsInternals {

    public final class Person {
        private final String name;
        private final int age;
        private final Address address;

        public Person(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = new Address(address); // Defensive copy
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public Address getAddress() { return new Address(address); } // Defensive copy
    }

    public static class Address {
        private final String city;

        public Address(String city) { this.city = city; }
        public Address(Address other) { this.city = other.city; }
        public String getCity() { return city; }
    }

    public static void main(String[] args) {
        System.out.println("=== Immutable Objects Internals ===\n");

        // 1. Immutable Object
        System.out.println("--- Immutable Object ---");
        Address addr = new Address("NYC");
        Person person = new Person("Alice", 25, addr);
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());
        System.out.println("City: " + person.getAddress().getCity());

        // 2. Characteristics
        System.out.println("\n--- Characteristics ---");
        System.out.println("1. All fields final");
        System.out.println("2. Class is final");
        System.out.println("3. No setter methods");
        System.out.println("4. Defensive copies");

        // 3. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("Thread-safe without synchronization");
        System.out.println("Safe for HashMap keys");
        System.out.println("No defensive copying needed");
    }
}
