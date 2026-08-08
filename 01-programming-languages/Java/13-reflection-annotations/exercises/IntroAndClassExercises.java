package reflection.exercises;

import java.lang.reflect.*;
import java.util.*;

/**
 * TOPIC 1 & 2 EXERCISES — Introduction and Class Introspection
 * 5 practice problems.
 */
public class IntroAndClassExercises {

    // =========================================================================
    // EXERCISE 1: Three Ways to Get a Class
    // =========================================================================
    /**
     * Return three different Class objects for String:
     * 1. Using .class literal
     * 2. Using getClass() on an instance
     * 3. Using Class.forName()
     * Return them as a List. All three should be equal.
     *
     * TODO: Implement this method
     */
    public static List<Class<?>> threeWaysToGetClass() {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Class Metadata Inspector
    // =========================================================================
    /**
     * Given a Class object, return a Map with the following keys:
     *   "name" -> getName()
     *   "simpleName" -> getSimpleName()
     *   "packageName" -> getPackage().getName()
     *   "isInterface" -> String.valueOf(isInterface())
     *   "isAbstract" -> String.valueOf(Modifier.isAbstract(getModifiers()))
     *
     * TODO: Implement this method
     */
    public static Map<String, String> inspectClassMetadata(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Find All Implemented Interfaces
    // =========================================================================
    /**
     * Given a Class object, return a sorted list of all interfaces it implements
     * (directly and indirectly). Use getInterfaces() and recurse into superclasses.
     *
     * TODO: Implement this method
     */
    public static List<String> findAllInterfaces(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Class Hierarchy Chain
    // =========================================================================
    /**
     * Given a Class object, return a list of class names in the hierarchy
     * from the given class up to (and including) Object.
     *
     * Example: ArrayList.class -> ["ArrayList", "AbstractList", "AbstractCollection", "Object"]
     *
     * TODO: Implement this method
     */
    public static List<String> classHierarchy(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: Dynamic Class Loading
    // =========================================================================
    /**
     * Given a fully-qualified class name as a string, load the class using
     * Class.forName(), create an instance using the default constructor,
     * and return the instance. If anything fails, return null.
     *
     * TODO: Implement this method
     */
    public static Object createInstanceByName(String className) {
        // TODO: Your code here
        return null;
    }
}
