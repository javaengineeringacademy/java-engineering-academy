package academy.javaengineering.oop.memory;

public class CloningMemory {

    static class Address implements Cloneable {
        String city;
        Address(String city) { this.city = city; }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class Person implements Cloneable {
        String name;
        Address address;
        Person(String name, Address address) {
            this.name = name;
            this.address = address;
        }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        protected Person deepClone() {
            return new Person(this.name, new Address(this.address.city));
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("=== Cloning Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Shallow Clone Memory
        System.out.println("--- Shallow Clone Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p1 = new Person("Alice", new Address("NYC"));
        Person p2 = (Person) p1.clone();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Shallow clone: " + (after - before) + " bytes");
        System.out.println("Shares reference to Address");

        // 2. Deep Clone Memory
        System.out.println("\n--- Deep Clone Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Person p3 = p1.deepClone();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Deep clone: " + (after - before) + " bytes");
        System.out.println("Creates new Address object");

        // 3. Memory Comparison
        System.out.println("\n--- Memory Comparison ---");
        System.out.println("Shallow: 1 new object (Person)");
        System.out.println("Deep: 2 new objects (Person + Address)");
        System.out.println("Deep uses more memory but safer");
    }
}
