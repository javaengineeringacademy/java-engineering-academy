package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Object Creation and Usage ===\n");

        // WHY: Objects encapsulate state + behavior. Without them, code is procedural and untestable.
        // INTERNAL: new triggers class loading → memory allocation → constructor chain → reference returned
        // ENGINEERING: Prefer factory methods over public constructors for flexibility

        Person alice = new Person("Alice", 30);
        Person bob = Person.create("Bob", 25);

        System.out.println("Alice: " + alice);
        System.out.println("Bob: " + bob);
        System.out.println("Same name? " + alice.hasSameNameAs(bob));

        // TRADE-OFF: Creating objects is cheap in Java (escape analysis can even allocate on stack)
        // But object graphs with deep hierarchies increase GC pressure
        Person[] people = new Person[1000];
        for (int i = 0; i < 1000; i++) {
            people[i] = Person.create("Person" + i, i);
        }
        System.out.println("Created 1000 objects. Memory: " + 
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
    }
}

class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static Person create(String name, int age) {
        return new Person(name, age);
    }

    public boolean hasSameNameAs(Person other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}
