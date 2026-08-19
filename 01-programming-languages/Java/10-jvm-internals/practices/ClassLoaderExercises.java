package academy.javaengineering.jvm.practices;

/**
 * ClassLoader Exercises
 * Complete each exercise by implementing the required method.
 * Test your solutions by running the main method.
 */
public class ClassLoaderExercises {

    /**
     * Exercise 1: Print the full ClassLoader hierarchy
     * Given a class, print its complete classloader chain from
     * the classloader that loaded it up to (and including) the bootstrap loader.
     *
     * Expected output format:
     *   ClassLoaderDemo -> ApplicationClassLoader -> PlatformClassLoader -> BootstrapClassLoader (null)
     */
    public static void printClassLoaderHierarchy(Class<?> clazz) {
        // TODO: Implement this method
        // HINT: Use clazz.getClassLoader() and loop through getParent()
    }

    /**
     * Exercise 2: Create a custom classloader that loads .class files
     * from a specified directory. The classloader should:
     * 1. Accept a directory path in constructor
     * 2. Read .class files from that directory
     * 3. Use defineClass to create Class objects
     * 4. Follow parent-first delegation
     */
    static class DirectoryClassLoader extends ClassLoader {
        public DirectoryClassLoader(String directoryPath) {
            // TODO: Implement constructor
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // TODO: Implement findClass
            // Convert class name to file path
            // Read .class bytes from disk
            // Use defineClass
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * Exercise 3: Implement a class cache
     * Create a classloader that caches loaded classes and returns
     * the cached version on subsequent requests (without re-reading from disk).
     * Use a HashMap to store cached classes.
     */
    static class CachingClassLoader extends ClassLoader {
        // TODO: Add cache storage

        public CachingClassLoader(String directoryPath) {
            // TODO: Implement
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // TODO: Check cache first, then load from disk and cache
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * Exercise 4: Detect classloader leaks
     * Write a method that detects potential classloader leaks by:
     * 1. Counting the number of loaded classes
     * 2. Identifying which classloader loaded each class
     * 3. Printing classes loaded by classloaders other than the application loader
     */
    public static void detectClassloaderLeaks() {
        // TODO: Implement
        // Use ManagementFactory.getClassLoadingMXBean() or Runtime MXBean
        // Iterate through loaded classes and identify classloaders
    }

    /**
     * Exercise 5: Break parent-first delegation
     * Create a classloader that checks local classes FIRST (child-first),
     * falling back to parent only if the class is not found locally.
     * This is used in some application servers and OSGi.
     */
    static class ChildFirstClassLoader extends ClassLoader {
        public ChildFirstClassLoader(String directoryPath) {
            // TODO: Implement
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // TODO: Implement child-first delegation
            // 1. Check if already loaded (findLoadedClass)
            // 2. Try to load locally FIRST (findClass)
            // 3. If not found locally, delegate to parent (super.loadClass)
            throw new ClassNotFoundException(name);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: ClassLoader Hierarchy");
        System.out.println("Your output:");
        // printClassLoaderHierarchy(String.class);
        // printClassLoaderHierarchy(ClassLoaderExercises.class);

        // Test Exercise 2
        System.out.println("\nExercise 2: DirectoryClassLoader");
        System.out.println("Create a DirectoryClassLoader and load a test class:");
        // DirectoryClassLoader cl = new DirectoryClassLoader("/path/to/classes");
        // Class<?> clazz = cl.loadClass("com.example.TestClass");

        // Test Exercise 3
        System.out.println("\nExercise 3: CachingClassLoader");
        System.out.println("Load the same class twice and verify it's the same object:");

        // Test Exercise 4
        System.out.println("\nExercise 4: Detect ClassLoader Leaks");
        detectClassloaderLeaks();

        // Test Exercise 5
        System.out.println("\nExercise 5: ChildFirstClassLoader");
        System.out.println("Verify child-first delegation order:");
    }
}
