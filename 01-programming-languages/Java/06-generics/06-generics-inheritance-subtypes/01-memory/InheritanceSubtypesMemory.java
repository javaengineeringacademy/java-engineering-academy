package academy.javaengineering.generics.memory;

import java.util.*;

public class InheritanceSubtypesMemory {

    static class Animal {}
    static class Dog extends Animal {}

    static class Box<T> {
        T value;
        public Box(T value) { this.value = value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Generics Inheritance Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. No Covariance Cost
        System.out.println("--- Covariance Cost ---");
        System.out.println("No inheritance: no virtual dispatch overhead");
        System.out.println("Type check at compile-time only");
        System.out.println("Runtime: all Box<T> are same class");

        // 2. Wildcard Capture
        System.out.println("\n--- Wildcard Capture ---");
        Box<Dog> dogBox = new Box<>(new Dog());
        Box<? extends Animal> animalBox = dogBox;
        System.out.println("No object creation for wildcard");
        System.out.println("Compiler tracks type internally");

        // 3. Bridge Methods in Subclasses
        System.out.println("\n--- Bridge Methods ---");
        System.out.println("class DogBox extends Box<Dog>");
        System.out.println("Compiler generates: void set(Object) bridge");
        System.out.println("Cost: extra method per level");

        // 4. Memory Layout
        System.out.println("\n--- Memory Layout ---");
        Box<Dog> direct = new Box<>(new Dog());
        Box<? extends Animal> wildcard = direct;
        System.out.println("Both reference same object");
        System.out.println("No copy, no wrapper");
    }
}
