package academy.javaengineering.generics.internals;

import java.util.*;

public class InheritanceSubtypesInternals {

    static class Animal {}
    static class Dog extends Animal {}
    static class Puppy extends Dog {}

    static class Box<T> {
        T value;
        public Box(T value) { this.value = value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Generics Inheritance & Subtypes ===\n");

        // 1. No Inheritance for Generics
        System.out.println("--- No Generic Inheritance ---");
        System.out.println("List<Dog> is NOT a subtype of List<Animal>");
        System.out.println("Box<Dog> is NOT a subtype of Box<Animal>");
        System.out.println("Generics are invariant");

        // 2. Wildcard for Subtyping
        System.out.println("\n--- Wildcard Subtyping ---");
        List<Dog> dogs = new ArrayList<>();
        List<? extends Animal> animals = dogs; // OK
        System.out.println("? extends Animal allows Dog, Puppy, etc.");
        System.out.println("? super Animal allows Animal, Object");

        // 3. Type Hierarchy
        System.out.println("\n--- Type Hierarchy ---");
        Box<Dog> dogBox = new Box<>(new Dog());
        Box<? extends Animal> animalBox = dogBox;
        System.out.println("Box<Dog> -> Box<? extends Animal> -> Box<?>");
        System.out.println("Each level widens accepted types");

        // 4. Recursive Inheritance
        System.out.println("\n--- Recursive Types ---");
        System.out.println("<T extends Comparable<T>>");
        System.out.println("String implements Comparable<String>");
        System.out.println("Integer implements Comparable<Integer>");

        // 5. Generic Interface Implementation
        System.out.println("\n--- Interface Implementation ---");
        System.out.println("class Dog implements Comparable<Dog>");
        System.out.println("Specifies concrete type for T");
    }
}
