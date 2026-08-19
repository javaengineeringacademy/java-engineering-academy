package academy.javaengineering.jvm.classloader;

/**
 * Exercise 1: Map the ClassLoader Hierarchy
 *
 * Task: Complete the mapClassLoader() method to recursively traverse and print
 * the classloader hierarchy for a given class. The output should show the
 * complete chain from the given class's loader up to (and including) null (Bootstrap).
 *
 * Expected output for String.class:
 *   String loaded by: null (Bootstrap ClassLoader)
 *   Parent: null (end of chain)
 *
 * Expected output for your custom class:
 *   MyClass loaded by: sun.misc.Launcher$AppClassLoader (Application ClassLoader)
 *   Parent: sun.misc.Launcher$ExtClassLoader (Platform ClassLoader)
 *   Parent: null (Bootstrap ClassLoader)
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Hierarchy Mapper ===\n");

        // Task 1: Map hierarchy for core JDK classes
        System.out.println("--- Core JDK Classes ---");
        mapClassLoader(String.class);
        mapClassLoader(java.util.ArrayList.class);
        mapClassLoader(javax.xml.parsers.DocumentBuilderFactory.class);

        // Task 2: Map hierarchy for application class
        System.out.println("\n--- Application Classes ---");
        mapClassLoader(Exercise1.class);

        // Task 3: Map hierarchy for a class loaded by a custom classloader
        System.out.println("\n--- Dynamic Loading ---");
        try {
            Class<?> dynamicClass = Class.forName("academy.javaengineering.jvm.classloader.Exercise1");
            mapClassLoader(dynamicClass);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }

        // Task 4: Print the complete hierarchy tree
        System.out.println("\n--- Full Hierarchy Tree ---");
        printHierarchyTree(Exercise1.class.getClassLoader(), 0);
    }

    /**
     * TODO: Implement this method to print the classloader hierarchy for a given class.
     *
     * Requirements:
     * 1. Print the class name and its classloader
     * 2. If classloader is null, print "(Bootstrap ClassLoader)"
     * 3. Recursively print parent classloaders until null
     * 4. Each level should be indented with "  " (two spaces)
     */
    static void mapClassLoader(Class<?> clazz) {
        // TODO: Implement this method
        System.out.println("  [TODO: Implement mapClassLoader]");
    }

    /**
     * TODO: Implement this method to print a tree view of the classloader hierarchy.
     *
     * Requirements:
     * 1. Print the classloader name with proper indentation
     * 2. If classloader is null, print "(Bootstrap ClassLoader)"
     * 3. Recursively print children with increased indentation
     * 4. Use tree characters (├──, └──) for visual clarity
     */
    static void printHierarchyTree(ClassLoader loader, int depth) {
        // TODO: Implement this method
    }
}
