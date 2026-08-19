package academy.javaengineering.jvm.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exercise 2: Custom ClassLoader Implementation
 *
 * Task: Implement a custom classloader that loads .class files from a specified directory.
 * This classloader should:
 * 1. Load classes from a given base directory
 * 2. Follow parent delegation (delegate to parent first)
 * 3. Cache loaded classes
 * 4. Support class unloading when the classloader is GC'd
 *
 * The classloader should be able to load classes like:
 *   CustomClassLoader loader = new CustomClassLoader("/path/to/classes");
 *   Class<?> clazz = loader.loadClass("com.example.MyClass");
 *   Object instance = clazz.getDeclaredConstructor().newInstance();
 */
public class Exercise2 {

    public static void main(String[] args) {
        System.out.println("=== Custom ClassLoader Exercise ===\n");

        // Task 1: Create a custom classloader
        System.out.println("--- Task 1: Create CustomClassLoader ---");
        // TODO: Uncomment and implement
        // DirectoryClassLoader loader = new DirectoryClassLoader(
        //     "/path/to/classes", Exercise2.class.getClassLoader());
        // System.out.println("Custom classloader created");

        // Task 2: Load a class using the custom classloader
        System.out.println("\n--- Task 2: Load Class ---");
        // TODO: Implement
        // Class<?> clazz = loader.loadClass("com.example.TestClass");
        // System.out.println("Loaded: " + clazz.getName());
        // System.out.println("ClassLoader: " + clazz.getClassLoader());

        // Task 3: Verify class identity
        System.out.println("\n--- Task 3: Class Identity ---");
        // TODO: Implement
        // Class<?> standardLoader = Class.forName("com.example.TestClass");
        // System.out.println("Same class? " + (clazz == standardLoader));

        // Task 4: Demonstrate class isolation
        System.out.println("\n--- Task 4: Class Isolation ---");
        // TODO: Implement
        // Two different classloaders loading the same class should produce different Class objects

        System.out.println("\n[Complete the TODO sections above]");
    }
}

/**
 * TODO: Implement this custom classloader.
 *
 * Requirements:
 * 1. Extend java.lang.ClassLoader
 * 2. Accept a base directory path and parent classloader in constructor
 * 3. Override findClass(String name) to load .class files from the directory
 * 4. Convert the class name to a file path (replace . with /)
 * 5. Read the .class bytes from the file
 * 6. Use defineClass() to create the Class object
 * 7. Throw ClassNotFoundException if the file is not found
 */
class DirectoryClassLoader extends ClassLoader {

    private final Path basePath;

    public DirectoryClassLoader(String basePath, ClassLoader parent) {
        super(parent);
        this.basePath = Paths.get(basePath);
    }

    // TODO: Override findClass() method
    // @Override
    // protected Class<?> findClass(String name) throws ClassNotFoundException {
    //     // 1. Convert class name to file path
    //     // 2. Check if file exists
    //     // 3. Read bytes from file
    //     // 4. Call defineClass()
    //     // 5. Handle IOException
    // }
}
