package academy.javaengineering.oop.internals;

public class CloningInternals {

    static class Address implements Cloneable {
        String city;

        Address(String city) {
            this.city = city;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone(); // Shallow copy
        }
    }

    static class Person implements Cloneable {
        String name;
        Address address;

        Person(String name, Address address) {
            this.name = name;
            this.address = address;
        }

        // Shallow clone
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        // Deep clone
        protected Person deepClone() {
            return new Person(this.name, new Address(this.address.city));
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("=== Cloning Internals ===\n");

        // 1. Cloneable Interface
        System.out.println("--- Cloneable Interface ---");
        System.out.println("Marker interface (no methods)");
        System.out.println("Indicates clone() is safe");
        System.out.println("Without it: CloneNotSupportedException");

        // 2. Shallow Clone
        System.out.println("\n--- Shallow Clone ---");
        Person p1 = new Person("Alice", new Address("NYC"));
        Person p2 = (Person) p1.clone();
        System.out.println("p1.address == p2.address: " + (p1.address == p2.address));
        System.out.println("Same reference - shallow copy");

        // 3. Deep Clone
        System.out.println("\n--- Deep Clone ---");
        Person p3 = p1.deepClone();
        System.out.println("p1.address == p3.address: " + (p1.address == p3.address));
        System.out.println("Different references - deep copy");

        // 4. clone() vs copy constructor
        System.out.println("\n--- clone() vs copy constructor ---");
        System.out.println("clone(): returns Object, needs casting");
        System.out.println("copy constructor: type-safe");
        System.out.println("Copy constructor preferred in Java");
    }
}
