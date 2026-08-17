package academy.javaengineering.oop.practices;

/**
 * Practice: Inheritance in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating parent and child classes with extends
 * - Overriding methods in subclasses
 * - Using super to call parent constructors and methods
 * - Understanding method hiding vs overriding
 * - instanceof checks
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 09-inheritance ===\n");

        // Test Exercise 1: Create a Dog that extends Animal
        Dog dog = createDog("Rex", 5, "German Shepherd");
        System.out.println("Exercise 1 - createDog: "
            + (dog != null && "Rex".equals(dog.getName()) && dog.getAge() == 5 ? "PASS" : "FAIL"));

        // Test Exercise 2: Override speak()
        String sound = dog.speak();
        System.out.println("Exercise 2 - dog.speak(): "
            + (sound != null && sound.contains("Woof") ? "PASS" : "FAIL"));

        // Test Exercise 3: Override toString()
        String str = dog.toString();
        System.out.println("Exercise 3 - dog.toString(): "
            + (str != null && str.contains("Rex") && str.contains("German Shepherd") ? "PASS" : "FAIL"));

        // Test Exercise 4: Use super constructor
        Animal animal = new Animal("Generic", 1);
        System.out.println("Exercise 4 - Animal constructor: "
            + ("Generic".equals(animal.getName()) && animal.getAge() == 1 ? "PASS" : "FAIL"));

        // Test Exercise 5: instanceof checks
        System.out.println("Exercise 5 - instanceof: "
            + (dog instanceof Animal && dog instanceof Dog ? "PASS" : "FAIL"));
    }

    // TODO 1: Create a static method that creates and returns a Dog object
    // The Dog should use the super constructor to initialize name and age,
    // and store its own breed field.
    // Hint: Dog extends Animal, and Animal has a constructor(String name, int age)
    static Dog createDog(String name, int age, String breed) {
        // YOUR CODE HERE
        return null;
    }
}

/**
 * Parent class representing a generic Animal.
 * Students should understand this and create Dog as a subclass.
 */
class Animal {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String speak() {
        return "...";
    }

    @Override
    public String toString() {
        return "Animal{name='" + name + "', age=" + age + "}";
    }
}

/**
 * TODO 2: Complete the Dog class so it:
 * - Extends Animal
 * - Has a private breed field
 * - Has a constructor that calls super(name, age) and sets breed
 * - Overrides speak() to return "Woof! Woof!"
 * - Overrides toString() to include breed info like: "Dog{name='Rex', age=5, breed='German Shepherd'}"
 */
class Dog extends Animal {
    private String breed;

    // YOUR CODE HERE: constructor, overridden methods
}
