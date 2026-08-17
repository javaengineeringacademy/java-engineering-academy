package academy.javaengineering.generics.generics-inheritance-subtypes.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Generics, Inheritance, and Subtypes Exercises
 * Understand how generics interact with inheritance.
 */
public class InheritanceSubtypesExercises {

    static class Animal {
        String name;
        Animal(String name) { this.name = name; }
        @Override
        public String toString() { return name; }
    }

    static class Dog extends Animal {
        Dog(String name) { super(name); }
    }

    static class Puppy extends Dog {
        Puppy(String name) { super(name); }
    }

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    // Exercise 1: Demonstrate that Dog IS-A Animal but Box<Dog> IS-NOT-A Box<Animal>
    // TODO: Show the type relationship
    public static void exercise1() {
        System.out.println("Exercise 1: Generic Type Relationships");
        Animal animal = new Dog("Rex");
        System.out.println("Animal animal = new Dog(): " + animal);

        Box<Dog> dogBox = new Box<>(new Dog("Buddy"));
        // TODO: Try to assign dogBox to Box<Animal>
        // Box<Animal> animalBox = dogBox;  // This causes a compile error
        // Explain why

        System.out.println("Box<Dog> cannot be assigned to Box<Animal>");
    }

    // Exercise 2: Show that List<Dog> IS-A List<Animal> using wildcards
    // TODO: Demonstrate proper type relationships
    public static void exercise2() {
        System.out.println("\nExercise 2: List Relationships with Wildcards");
        List<Dog> dogs = List.of(new Dog("Rex"), new Dog("Buddy"));

        // TODO: Use wildcards to make this work
        List<? extends Animal> animals = dogs;
        System.out.println("List<? extends Animal> = List<Dog>: " + animals);

        // TODO: Show what you can and cannot do with wildcard lists
    }

    // Exercise 3: Demonstrate assignment compatibility
    // TODO: Show how generics affect assignment rules
    public static void exercise3() {
        System.out.println("\nExercise 3: Assignment Compatibility");
        // TODO: Create instances and show assignment rules
        Box<Dog> dogBox = new Box<>(new Dog("Rex"));
        Box<Puppy> puppyBox = new Box<>(new Puppy("Tiny"));

        // TODO: Show these assignments:
        // Box<Animal> a1 = dogBox;      // Does this compile?
        // Box<Dog> a2 = puppyBox;       // Does this compile?
        // Box<Object> a3 = dogBox;      // Does this compile?

    }

    // Exercise 4: Create a method that accepts a list of any Animal subtype
    // TODO: Implement using wildcard
    public static void printAnimals(List<? extends Animal> animals) {
        // TODO: Implement
    }

    // Exercise 5: Create a method that adds animals to a list
    // TODO: Implement using lower bound
    public static void addDogs(List<? super Dog> list, int count) {
        // TODO: Implement
    }

    public static void main(String[] args) {
        System.out.println("=== Generics, Inheritance, and Subtypes Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();

        // Test Exercise 4
        System.out.println("\nExercise 4: printAnimals");
        List<Dog> dogs = List.of(new Dog("Rex"), new Dog("Buddy"));
        printAnimals(dogs);

        // Test Exercise 5
        System.out.println("\nExercise 5: addDogs");
        List<Animal> animals = new ArrayList<>();
        addDogs(animals, 3);
        System.out.println("Added animals: " + animals);
    }
}
