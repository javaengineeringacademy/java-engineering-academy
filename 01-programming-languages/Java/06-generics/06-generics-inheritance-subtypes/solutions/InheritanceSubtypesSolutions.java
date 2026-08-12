package academy.javaengineering.generics.generics-inheritance-subtypes.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Generics, Inheritance, and Subtypes Solutions - Complete implementations.
 */
public class InheritanceSubtypesSolutions {

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

    // Exercise 1: Generic Type Relationships
    public static void exercise1() {
        System.out.println("Exercise 1: Generic Type Relationships");

        // Dog IS-A Animal (inheritance works)
        Animal animal = new Dog("Rex");
        System.out.println("Animal animal = new Dog(): " + animal);

        // Box<Dog> IS-NOT-A Box<Animal> (generics are invariant)
        Box<Dog> dogBox = new Box<>(new Dog("Buddy"));
        // Box<Animal> animalBox = dogBox;  // COMPILE ERROR!
        System.out.println("Box<Dog> cannot be assigned to Box<Animal>");
        System.out.println("Generics are invariant - no inheritance relationship");
    }

    // Exercise 2: List Relationships with Wildcards
    public static void exercise2() {
        System.out.println("\nExercise 2: List Relationships with Wildcards");
        List<Dog> dogs = List.of(new Dog("Rex"), new Dog("Buddy"));

        // Using wildcards to establish relationship
        List<? extends Animal> animals = dogs;
        System.out.println("List<? extends Animal> = List<Dog>: " + animals);

        // Can read as Animal
        for (Animal a : animals) {
            System.out.println("  Animal: " + a);
        }

        // Cannot add to wildcard list (except null)
        // animals.add(new Dog("Max"));  // COMPILE ERROR!
        System.out.println("Cannot add to List<? extends Animal>");
    }

    // Exercise 3: Assignment Compatibility
    public static void exercise3() {
        System.out.println("\nExercise 3: Assignment Compatibility");
        Box<Dog> dogBox = new Box<>(new Dog("Rex"));
        Box<Puppy> puppyBox = new Box<>(new Puppy("Tiny"));

        // These assignments demonstrate invariance:
        System.out.println("Box<Dog> dogBox = new Box<>(new Dog())");
        System.out.println("Box<Puppy> puppyBox = new Box<>(new Puppy())");
        System.out.println();

        // Box<Animal> a1 = dogBox;      // COMPILE ERROR: Incompatible types
        System.out.println("Box<Animal> a1 = dogBox;  // COMPILE ERROR");

        // Box<Dog> a2 = puppyBox;       // COMPILE ERROR: Incompatible types
        System.out.println("Box<Dog> a2 = puppyBox;   // COMPILE ERROR");

        // Box<Object> a3 = dogBox;      // COMPILE ERROR: Incompatible types
        System.out.println("Box<Object> a3 = dogBox;  // COMPILE ERROR");
        System.out.println("Generics do not support inheritance!");
    }

    // Exercise 4: Method accepting list of any Animal subtype
    public static void printAnimals(List<? extends Animal> animals) {
        System.out.print("Animals: ");
        for (Animal animal : animals) {
            System.out.print(animal + " ");
        }
        System.out.println();
    }

    // Exercise 5: Method adding animals to a list
    public static void addDogs(List<? super Dog> list, int count) {
        for (int i = 1; i <= count; i++) {
            list.add(new Dog("Dog" + i));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Generics, Inheritance, and Subtypes Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();

        // Test Exercise 4
        System.out.println("\nExercise 4: printAnimals");
        List<Dog> dogs = List.of(new Dog("Rex"), new Dog("Buddy"));
        printAnimals(dogs);
        List<Animal> moreAnimals = List.of(new Animal("Cat"), new Dog("Max"));
        printAnimals(moreAnimals);

        // Test Exercise 5
        System.out.println("\nExercise 5: addDogs");
        List<Animal> animals = new ArrayList<>();
        addDogs(animals, 3);
        System.out.println("Added animals: " + animals);
        List<Object> objects = new ArrayList<>();
        addDogs(objects, 2);
        System.out.println("Added to Object list: " + objects);
    }
}
