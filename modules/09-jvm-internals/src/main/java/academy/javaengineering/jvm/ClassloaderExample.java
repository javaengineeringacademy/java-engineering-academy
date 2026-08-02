package academy.javaengineering.jvm;

/**
 * Demonstrates ClassLoader hierarchy and custom class loading.
 *
 * <p>This class shows how Java's ClassLoader delegation model works, including
 * Bootstrap, Platform, and Application ClassLoaders.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Class delegation model (parent-first)</li>
 *   <li>Custom ClassLoader implementation</li>
 *   <li>ClassLoader hierarchy traversal</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ClassloaderExample {

    /**
     * Custom ClassLoader implementation demonstrating class loading extension.
     */
    public static class CustomClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            throw new ClassNotFoundException("Class not found: " + name);
        }
    }

    /**
     * Utility class for printing ClassLoader hierarchy.
     */
    public static class ClassLoaderHierarchy {
        /**
         * Prints the ClassLoader hierarchy for the given class.
         *
         * @param clazz the class to inspect
         */
        public void printHierarchy(Class<?> clazz) {
            ClassLoader loader = clazz.getClassLoader();
            int level = 0;
            while (loader != null) {
                System.out.println("Level " + level + ": " + loader.getName());
                loader = loader.getParent();
                level++;
            }
            System.out.println("Level " + level + ": Bootstrap (null)");
        }
    }

    /**
     * Demonstrates ClassLoader concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== ClassLoader Demo ===");
        new ClassLoaderHierarchy().printHierarchy(ClassloaderExample.class);
        CustomClassLoader customLoader = new CustomClassLoader();
        System.out.println("Custom ClassLoader: " + customLoader);
        System.out.println("Parent: " + customLoader.getParent());
    }
}
