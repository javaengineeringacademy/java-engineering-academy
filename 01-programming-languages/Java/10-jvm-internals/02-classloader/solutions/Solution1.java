package academy.javaengineering.jvm.classloader;

/**
 * Solution 1: ClassLoader Hierarchy Mapping
 *
 * Demonstrates how to traverse and print the classloader hierarchy
 * for any given class, showing the complete delegation chain.
 */
public class Solution1 {

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Hierarchy Mapper ===\n");

        System.out.println("--- Core JDK Classes ---");
        mapClassLoader(String.class);
        mapClassLoader(java.util.ArrayList.class);
        mapClassLoader(javax.xml.parsers.DocumentBuilderFactory.class);

        System.out.println("\n--- Application Classes ---");
        mapClassLoader(Solution1.class);

        System.out.println("\n--- Dynamic Loading ---");
        try {
            Class<?> dynamicClass = Class.forName("academy.javaengineering.jvm.classloader.Solution1");
            mapClassLoader(dynamicClass);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }

        System.out.println("\n--- Full Hierarchy Tree ---");
        printHierarchyTree(Solution1.class.getClassLoader(), 0);
    }

    static void mapClassLoader(Class<?> clazz) {
        String className = clazz.getSimpleName();
        ClassLoader loader = clazz.getClassLoader();
        String loaderName = getLoaderName(loader);

        System.out.println("  " + className + " loaded by: " + loaderName);

        ClassLoader current = loader;
        while (current != null) {
            current = current.getParent();
            System.out.println("    Parent: " + getLoaderName(current));
        }
    }

    static void printHierarchyTree(ClassLoader loader, int depth) {
        String indent = "  ".repeat(depth);
        String prefix = depth == 0 ? "" : (depth > 0 ? indent + "└── " : "");
        System.out.println(prefix + getLoaderName(loader));

        if (loader != null) {
            printHierarchyTree(loader.getParent(), depth + 1);
        }
    }

    private static String getLoaderName(ClassLoader loader) {
        if (loader == null) {
            return "null (Bootstrap ClassLoader)";
        }
        String name = loader.getClass().getName();
        if (name.contains("AppClassLoader")) {
            return name + " (Application ClassLoader)";
        } else if (name.contains("ExtClassLoader") || name.contains("PlatformClassLoader")) {
            return name + " (Platform ClassLoader)";
        }
        return name;
    }
}
