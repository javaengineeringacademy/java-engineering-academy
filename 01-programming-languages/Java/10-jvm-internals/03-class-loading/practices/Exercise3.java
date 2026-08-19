package academy.javaengineering.jvm.classloading;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exercise 3: Lazy Loading Implementation
 *
 * Task: Implement a lazy loading mechanism that loads classes only when
 * they are first accessed, using the classloader lifecycle properly.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== Lazy Loading Implementation ===\n");

        // Task 1: Lazy loading with Class.forName
        System.out.println("--- Task 1: Lazy Loading ---");
        // TODO: Implement a lazy loader that loads class only on first get()
        // LazyLoader loader = new LazyLoader("com.example.HeavyClass");
        // System.out.println("Before get: class not loaded");
        // Class<?> clazz = loader.get();
        // System.out.println("After get: class loaded");

        // Task 2: Lifecycle management
        System.out.println("\n--- Task 2: Lifecycle Management ---");
        // TODO: Implement close() to allow classloader to be GC'd
        // TODO: Demonstrate class unloading after close()

        // Task 3: Error handling
        System.out.println("\n--- Task 3: Error Handling ---");
        // TODO: Handle ClassNotFoundException gracefully
        // TODO: Handle ExceptionInInitializerError from static blocks

        System.out.println("\n[Complete the TODO sections above]");
    }
}

/**
 * TODO: Implement a lazy class loader.
 *
 * Requirements:
 * 1. Accept a class name in the constructor
 * 2. Do NOT load the class in the constructor
 * 3. Load the class only when get() is first called
 * 4. Cache the loaded class for subsequent calls
 * 5. Implement close() to release the classloader reference
 */
class LazyLoader extends ClassLoader {
    private final String className;
    private Class<?> loadedClass;

    public LazyLoader(String className) {
        super(LazyLoader.class.getClassLoader());
        this.className = className;
    }

    // TODO: Implement get() method
    // public Class<?> get() throws ClassNotFoundException {
    //     if (loadedClass == null) {
    //         loadedClass = loadClass(className);
    //     }
    //     return loadedClass;
    // }

    // TODO: Implement close() method
    // @Override
    // public void close() throws IOException {
    //     loadedClass = null;
    //     super.close();
    // }
}
