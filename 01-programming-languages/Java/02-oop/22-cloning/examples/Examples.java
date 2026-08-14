package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Clone Patterns ===\n");

        // WHY: Clone creates independent copy of object graph
        // INTERNAL: Object.clone() does shallow copy, must implement Cloneable
        // ENGINEERING: Copy constructor preferred over clone (Effective Java)

        Address addr = new Address("123 Main St", "Springfield", "IL");
        PersonOriginal p1 = new PersonOriginal("Alice", 30, addr);

        PersonOriginal cloned = p1.clone();
        System.out.println("Original: " + p1);
        System.out.println("Cloned: " + cloned);
        System.out.println("Same address? " + (p1.getAddress() == cloned.getAddress())); // TRUE - shallow!

        // Modify clone's address - affects original!
        addr.city = "Chicago";
        System.out.println("\nAfter modifying address:");
        System.out.println("Original city: " + p1.getAddress().city); // Chicago!
        System.out.println("Cloned city: " + cloned.getAddress().city); // Chicago!

        // TRADE-OFF: Shallow clone vs Deep clone
        // Shallow: fast, shares references, risky
        // Deep: safe, independent, slower
    }
}

class Address implements Cloneable {
    String street, city, state;
    Address(String street, String city, String state) {
        this.street = street; this.city = city; this.state = state;
    }

    @Override
    protected Address clone() {
        try { return (Address) super.clone(); }
        catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }
}

class PersonOriginal implements Cloneable {
    private final String name;
    private final int age;
    private final Address address;

    PersonOriginal(String name, int age, Address address) {
        this.name = name; this.age = age; this.address = address;
    }

    public Address getAddress() { return address; }

    @Override
    public PersonOriginal clone() {
        try {
            PersonOriginal copy = (PersonOriginal) super.clone();
            // For deep copy: copy.address = this.address.clone();
            return copy;
        } catch (CloneNotSupportedException e) { throw new AssertionError(); }
    }

    @Override
    public String toString() { return name + "(" + age + ") @ " + address.city; }
}
