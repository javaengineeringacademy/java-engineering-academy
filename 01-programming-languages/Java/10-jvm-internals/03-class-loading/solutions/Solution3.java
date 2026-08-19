package academy.javaengineering.jvm.classloading;

import java.io.IOException;

/**
 * Solution 3: Lazy Loading Implementation
 *
 * Demonstrates lazy class loading with proper lifecycle management
 * and error handling.
 */
public class Solution3 {

    public static void main(String[] args) {
        System.out.println("=== Lazy Loading Implementation ===\n");

        // Task 1: Lazy loading
        System.out.println("--- Task 1: Lazy Loading ---");
        try {
            LazyLoaderSolution loader = new LazyLoaderSolution(
                "academy.javaengineering.jvm.classloading.DelayedInit"
            );
            System.out.println("Before get(): class not loaded yet");

            Class<?> clazz = loader.get();
            System.out.println("After get(): class loaded - " + clazz.getName());

            // Second call returns cached
            Class<?> cached = loader.get();
            System.out.println("Same instance? " + (clazz == cached));

            loader.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Task 2: Lifecycle management
        System.out.println("\n--- Task 2: Lifecycle Management ---");
        try {
            LazyLoaderSolution loader = new LazyLoaderSolution(
                "academy.javaengineering.jvm.classloading.DelayedInit"
            );
            Class<?> clazz = loader.get();
            System.out.println("Class loaded: " + clazz.getName());
            System.out.println("Closing classloader...");
            loader.close();
            System.out.println("Classloader closed (eligible for GC)");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Task 3: Error handling
        System.out.println("\n--- Task 3: Error Handling ---");
        try {
            LazyLoaderSolution badLoader = new LazyLoaderSolution(
                "com.nonexistent.FakeClass"
            );
            badLoader.get();
        } catch (ClassNotFoundException e) {
            System.out.println("Handled: " + e.getMessage());
        }
    }
}

class LazyLoaderSolution extends ClassLoader {
    private final String className;
    private Class<?> loadedClass;

    public LazyLoaderSolution(String className) {
        super(LazyLoaderSolution.class.getClassLoader());
        this.className = className;
        System.out.println("  LazyLoader created (class NOT loaded yet)");
    }

    public Class<?> get() throws ClassNotFoundException {
        if (loadedClass == null) {
            System.out.println("  Loading class for the first time...");
            loadedClass = loadClass(className);
            System.out.println("  Class loaded and cached");
        } else {
            System.out.println("  Returning cached class");
        }
        return loadedClass;
    }

    @Override
    public void close() throws IOException {
        loadedClass = null;
        super.close();
    }
}
