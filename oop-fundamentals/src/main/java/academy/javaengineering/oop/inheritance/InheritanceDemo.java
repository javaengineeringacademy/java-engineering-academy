package academy.javaengineering.oop.inheritance;

/**
 * InheritanceDemo - Demonstrates single and multilevel inheritance in Java.
 * 
 * <p><b>Inheritance</b> allows a class to inherit fields and methods from another class,
 * promoting code reuse and establishing IS-A relationships.
 * 
 * <p><b>Key Concepts:</b>
 * <ul>
 *   <li><b>extends</b> keyword establishes inheritance</li>
 *   <li><b>super</b> calls parent constructor/methods</li>
 *   <li><b>protected</b> members accessible to subclasses</li>
 *   <li>Java supports single inheritance (one parent only)</li>
 *   <li>Multilevel inheritance: A -> B -> C chain</li>
 *   <li>All classes implicitly extend Object</li>
 * </ul>
 * 
 * <p><b>Analogy:</b> Like a family tree - children inherit traits from parents,
 * but each child has unique characteristics.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class InheritanceDemo {

    private InheritanceDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Inheritance Demo ===\n");

        // Creating objects at different levels
        System.out.println("--- Single Inheritance ---");
        Animal animal = new Animal("Generic Animal");
        animal.eat();
        animal.sleep();

        System.out.println("\n--- Multilevel Inheritance ---");
        Dog dog = new Dog("Buddy", "Golden Retriever");
        dog.eat();      // Inherited from Animal
        dog.sleep();    // Inherited from Animal
        dog.fetch();    // Dog-specific
        dog.bark();     // Dog-specific

        System.out.println("\n--- Three-Level Inheritance ---");
        Puppy puppy = new Puppy("Max", "Labrador", 3);
        puppy.eat();    // From Animal
        puppy.sleep();  // From Animal
        puppy.fetch();  // From Dog
        puppy.play();   // Puppy-specific

        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("Creating Puppy triggers:");
        System.out.println("  1. Puppy constructor");
        System.out.println("  2. Dog constructor (super)");
        System.out.println("  3. Animal constructor (super)");

        System.out.println("\n--- Protected Access ---");
        System.out.println("Puppy name: " + puppy.getName()); // protected in Animal
        System.out.println("Puppy breed: " + puppy.breed);    // protected in Dog

        System.out.println("\n--- instanceof Checks ---");
        System.out.println("puppy instanceof Puppy: " + (puppy instanceof Puppy));
        System.out.println("puppy instanceof Dog: " + (puppy instanceof Dog));
        System.out.println("puppy instanceof Animal: " + (puppy instanceof Animal));
        System.out.println("puppy instanceof Object: " + (puppy instanceof Object));

        System.out.println("\n--- Method Override ---");
        puppy.sound(); // Puppy's override
        dog.sound();   // Dog's override
        animal.sound(); // Animal's original

        System.out.println("\n--- Object Class Inheritance ---");
        System.out.println("toString(): " + puppy.toString());
        System.out.println("hashCode(): " + puppy.hashCode());
        System.out.println("getClass(): " + puppy.getClass().getSimpleName());
    }
}