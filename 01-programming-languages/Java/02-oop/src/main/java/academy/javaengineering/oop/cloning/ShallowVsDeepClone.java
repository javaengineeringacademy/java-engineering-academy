package academy.javaengineering.oop.cloning;

public class ShallowVsDeepClone {

    public static void shallowCloneDemo() {
        System.out.println("=== SHALLOW CLONE DEMO ===");

        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        Employee original = new Employee("Alice", 101, 75000.0, address, "secret123");

        Employee shallowCopy = original.shallowClone();

        System.out.println("Original: " + original);
        System.out.println("Shallow Copy: " + shallowCopy);
        System.out.println("Same Address reference? " + (original.getAddress() == shallowCopy.getAddress()));

        shallowCopy.getAddress().setCity("Chicago");
        System.out.println("\nAfter modifying shallow copy's address:");
        System.out.println("Original address city: " + original.getAddress().getCity());
        System.out.println("Copy address city: " + shallowCopy.getAddress().getCity());
        System.out.println("Both changed because they share the same Address object!");
        System.out.println();
    }

    public static void deepCloneDemo() {
        System.out.println("=== DEEP CLONE DEMO ===");

        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        Employee original = new Employee("Bob", 102, 85000.0, address, "pass456");

        Employee deepCopy = original.deepClone();

        System.out.println("Original: " + original);
        System.out.println("Deep Copy: " + deepCopy);
        System.out.println("Same Address reference? " + (original.getAddress() == deepCopy.getAddress()));

        deepCopy.getAddress().setCity("Chicago");
        System.out.println("\nAfter modifying deep copy's address:");
        System.out.println("Original address city: " + original.getAddress().getCity());
        System.out.println("Copy address city: " + deepCopy.getAddress().getCity());
        System.out.println("Only copy changed because they have independent Address objects!");
        System.out.println();
    }

    public static void cloneWithTransientDemo() {
        System.out.println("=== TRANSIENT FIELD DEMO ===");

        Address address = new Address("456 Oak Ave", "Metropolis", "NY", "10001");
        Employee original = new Employee("Charlie", 103, 95000.0, address, "mySecretPassword");

        Employee clone = original.deepClone();

        System.out.println("Original password: " + original.getPassword());
        System.out.println("Cloned password: " + clone.getPassword());
        System.out.println("Transient field 'password' is NOT cloned because it is excluded from serialization.");
        System.out.println();
    }

    static class Person {
        private String name;
        private Address address;

        public Person(String name, Address address) {
            this.name = name;
            this.address = address;
        }

        public Person(Person other) {
            this.name = other.name;
            this.address = new Address(
                other.address.getStreet(),
                other.address.getCity(),
                other.address.getState(),
                other.address.getZip()
            );
        }

        public String getName() {
            return name;
        }

        public Address getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', address=" + address + "}";
        }
    }

    public static void copyConstructorDemo() {
        System.out.println("=== COPY CONSTRUCTOR DEMO ===");

        Address address = new Address("789 Elm St", "Gotham", "NJ", "07001");
        Person original = new Person("Diana", address);

        Person copy = new Person(original);

        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        System.out.println("Same Address? " + (original.getAddress() == copy.getAddress()));

        copy.getAddress().setCity("Star City");
        System.out.println("\nAfter modifying copy's city:");
        System.out.println("Original: " + original.getAddress().getCity());
        System.out.println("Copy: " + copy.getAddress().getCity());
        System.out.println();
    }

    static class Vehicle implements Cloneable {
        private String model;
        private Address location;

        public Vehicle(String model, Address location) {
            this.model = model;
            this.location = location;
        }

        public Vehicle copy() {
            Vehicle copy = new Vehicle(this.model, this.location.clone());
            return copy;
        }

        public String getModel() {
            return model;
        }

        public Address getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "Vehicle{model='" + model + "', location=" + location + "}";
        }
    }

    public static void copyMethodDemo() {
        System.out.println("=== COPY METHOD DEMO ===");

        Address location = new Address("100 Industrial Blvd", "Central City", "CA", "90210");
        Vehicle original = new Vehicle("Tesla Model 3", location);

        Vehicle copy = original.copy();

        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        System.out.println("Same location? " + (original.getLocation() == copy.getLocation()));

        copy.getLocation().setCity("Coast City");
        System.out.println("\nAfter modifying copy:");
        System.out.println("Original location: " + original.getLocation().getCity());
        System.out.println("Copy location: " + copy.getLocation().getCity());
        System.out.println();
    }

    public static void whyCloneIsBroken() {
        System.out.println("=== WHY CLONE() IS BROKEN (Effective Java) ===");
        System.out.println("1. Cloneable is a broken interface - no clone() method defined");
        System.out.println("2. Object.clone() is public but Cloneable has no public clone method");
        System.out.println("3. Constructor is bypassed - violates object initialization contracts");
        System.out.println("4. Throws checked CloneNotSupportedException unnecessarily");
        System.out.println("5. Mutable final fields cannot be reassigned in clone()");
        System.out.println("6. No clean way to handle deep cloning of cyclic references");
        System.out.println("7. Subclasses must handle CloneNotSupportedException even if they implement Cloneable");
        System.out.println();
    }

    public static void bestPractices() {
        System.out.println("=== BEST PRACTICES FOR COPYING OBJECTS ===");
        System.out.println("1. Use copy constructors: new MyClass(other)");
        System.out.println("2. Use static factory methods: MyClass.copyOf(other)");
        System.out.println("3. Avoid clone() - it is error-prone and fragile");
        System.out.println("4. For deep copies, explicitly copy each mutable field");
        System.out.println("5. Use serialization-based copying for complex object graphs");
        System.out.println("6. Consider making classes immutable to avoid copying altogether");
        System.out.println("7. Use libraries like Apache Commons BeanUtils for bean copying");
        System.out.println();
    }

    public static void main(String[] args) {
        shallowCloneDemo();
        deepCloneDemo();
        cloneWithTransientDemo();
        copyConstructorDemo();
        copyMethodDemo();
        whyCloneIsBroken();
        bestPractices();
    }
}
