package set.enumset.exercises;

import java.util.*;

public class EnumSetExercises {

    enum Season { SPRING, SUMMER, FALL, WINTER }
    enum Planet { MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE }

    // TODO 1: Write a method that returns all combinations of two EnumSets.
    public static <E extends Enum<E>> EnumSet<E> union(EnumSet<E> set1, EnumSet<E> set2) {
        // Your code here
        return null;
    }

    // TODO 2: Write a method that checks if one EnumSet is a subset of another.
    public static <E extends Enum<E>> boolean isSubset(EnumSet<E> subset, EnumSet<E> superset) {
        // Your code here
        return false;
    }

    // TODO 3: Write a method that returns the complement of an EnumSet relative to
    //         all values of the enum.
    public static <E extends Enum<E>> EnumSet<E> complement(EnumSet<E> set) {
        // Your code here
        return null;
    }

    // TODO 4: Write a method that creates an EnumSet from a boolean array
    //         (true = include, false = exclude).
    public static EnumSet<Season> fromBooleanArray(boolean[] include) {
        // Your code here
        return null;
    }

    // TODO 5: Write a method that finds which enum values are NOT in an EnumSet.
    public static <E extends Enum<E>> List<E> missingValues(EnumSet<E> set, Class<E> enumClass) {
        // Your code here
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Run the solutions to verify your answers.");
    }
}
