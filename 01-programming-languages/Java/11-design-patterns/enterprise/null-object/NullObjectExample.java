package academy.javaengineering.patterns.enterprise.null_object;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the Null Object pattern eliminating null checks.
 */
public class NullObjectExample {

    public static void main(String[] args) {
        System.out.println("=== Null Object Pattern Demo ===\n");

        // Mixed collection with real and null objects
        List<Animal> animals = Arrays.asList(
                new Dog("Rex"),
                NullAnimal.getInstance(),
                new Dog("Buddy"),
                NullAnimal.getInstance()
        );

        System.out.println("--- Iterating over animals ---");
        for (Animal animal : animals) {
            // No null checks needed!
            System.out.println("Animal: " + animal.getName()
                    + " | Real: " + animal.isReal()
                    + " | Legs: " + animal.getLegs());
            animal.speak();
        }

        System.out.println("\n--- Finding an animal ---");
        Animal found = findAnimal(animals, "Rex");
        System.out.println("Found: " + found);

        Animal notFound = findAnimal(animals, "Nonexistent");
        System.out.println("Not found: " + notFound);
        System.out.println("isReal: " + notFound.isReal());

        System.out.println("\n--- Safe operations on null object ---");
        NullAnimal nullAnimal = NullAnimal.getInstance();
        nullAnimal.speak(); // No output, no exception
        System.out.println("Name: " + nullAnimal.getName());
        System.out.println("Legs: " + nullAnimal.getLegs());
    }

    private static Animal findAnimal(List<Animal> animals, String name) {
        return animals.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(NullAnimal.getInstance());
    }
}
