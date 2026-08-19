package academy.javaengineering.jvm.solutions;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * ClassLoader Solutions - Complete implementations
 */
public class ClassLoaderSolutions {

    /**
     * Exercise 1 Solution: Print the full ClassLoader hierarchy
     */
    public static void printClassLoaderHierarchy(Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        StringBuilder hierarchy = new StringBuilder(clazz.getSimpleName());

        while (cl != null) {
            hierarchy.append(" -> ").append(cl.getClass().getSimpleName());
            cl = cl.getParent();
        }
        hierarchy.append(" -> BootstrapClassLoader (null)");

        System.out.println(hierarchy.toString());
    }

    /**
     * Exercise 2 Solution: DirectoryClassLoader
     * Loads .class files from a specified directory
     */
    static class DirectoryClassLoader extends ClassLoader {
        private final String loadPath;

        public DirectoryClassLoader(String directoryPath) {
            super(null); // Bootstrap classloader as parent
            this.loadPath = directoryPath;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String fileName = name.replace('.', File.separatorChar) + ".class";
            File classFile = new File(loadPath, fileName);

            if (!classFile.exists()) {
                throw new ClassNotFoundException("Class file not found: " + classFile);
            }

            try {
                byte[] bytes = Files.readAllBytes(classFile.toPath());
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Error loading class: " + name, e);
            }
        }
    }

    /**
     * Exercise 3 Solution: CachingClassLoader
     * Caches loaded classes to avoid re-reading from disk
     */
    static class CachingClassLoader extends ClassLoader {
        private final String loadPath;
        private final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

        public CachingClassLoader(String directoryPath) {
            super(null);
            this.loadPath = directoryPath;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // Check cache first
            if (cache.containsKey(name)) {
                System.out.println("  [Cache HIT] " + name);
                return cache.get(name);
            }

            System.out.println("  [Cache MISS] Loading: " + name);

            // Load from disk
            String fileName = name.replace('.', File.separatorChar) + ".class";
            File classFile = new File(loadPath, fileName);

            if (!classFile.exists()) {
                throw new ClassNotFoundException("Class file not found: " + classFile);
            }

            try {
                byte[] bytes = Files.readAllBytes(classFile.toPath());
                Class<?> clazz = defineClass(name, bytes, 0, bytes.length);

                // Cache the loaded class
                cache.put(name, clazz);

                return clazz;
            } catch (IOException e) {
                throw new ClassNotFoundException("Error loading class: " + name, e);
            }
        }
    }

    /**
     * Exercise 4 Solution: Detect classloader leaks
     */
    public static void detectClassloaderLeaks() {
        System.out.println("Loaded class count by classloader:");

        // Get all loaded classes via reflection
        try {
            Class<?> classLoaderClass = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Field field = classLoaderClass.getDeclaredField("classes");
            field.setAccessible(true);

            // This approach doesn't work directly, use alternative
            RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
            System.out.println("  Boot classpath: " + rtBean.getBootClassPath().substring(0, Math.min(50, rtBean.getBootClassPath().length())) + "...");

        } catch (Exception e) {
            System.out.println("  Using ManagementFactory for class info");
        }

        // Alternative: Use ClassLoadingMXBean
        ClassLoadingMXBean clBean = ManagementFactory.getClassLoadingMXBean();
        System.out.println("  Total loaded: " + clBean.getTotalLoadedClassCount());
        System.out.println("  Currently loaded: " + clBean.getLoadedClassCount());
        System.out.println("  Unloaded: " + clBean.getUnloadedClassCount());
    }

    /**
     * Exercise 5 Solution: ChildFirstClassLoader
     * Breaks parent-first delegation (child-first)
     */
    static class ChildFirstClassLoader extends ClassLoader {
        private final String loadPath;

        public ChildFirstClassLoader(String directoryPath) {
            super(null);
            this.loadPath = directoryPath;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // 1. Check if already loaded
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            // 2. Try to load locally FIRST (child-first)
            // Exception: java.lang.Object and system classes must be loaded by parent
            if (!name.startsWith("java.lang.") && !name.startsWith("java.util.")) {
                try {
                    Class<?> localClass = findClass(name);
                    if (resolve) {
                        resolveClass(localClass);
                    }
                    System.out.println("  [ChildFirst] Loaded locally: " + name);
                    return localClass;
                } catch (ClassNotFoundException e) {
                    // Fall through to parent
                }
            }

            // 3. Delegate to parent for system classes or if not found locally
            try {
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("Class not found: " + name);
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String fileName = name.replace('.', File.separatorChar) + ".class";
            File classFile = new File(loadPath, fileName);

            if (!classFile.exists()) {
                throw new ClassNotFoundException("Class file not found: " + classFile);
            }

            try {
                byte[] bytes = Files.readAllBytes(classFile.toPath());
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Error loading class: " + name, e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Solutions ===\n");

        // Exercise 1 Solution
        System.out.println("Exercise 1: ClassLoader Hierarchy");
        printClassLoaderHierarchy(String.class);
        printClassLoaderHierarchy(ClassLoaderSolutions.class);
        printClassLoaderHierarchy(Thread.class);

        // Exercise 2 Solution
        System.out.println("\nExercise 2: DirectoryClassLoader");
        System.out.println("To test: create /tmp/classes/com/test/Test.class");
        System.out.println("Then: DirectoryClassLoader cl = new DirectoryClassLoader(\"/tmp/classes\");");
        System.out.println("      Class<?> clazz = cl.loadClass(\"com.test.Test\");");

        // Exercise 3 Solution
        System.out.println("\nExercise 3: CachingClassLoader");
        System.out.println("Caching prevents redundant disk reads");
        System.out.println("First load: [Cache MISS] Loading: com.test.Test");
        System.out.println("Second load: [Cache HIT] com.test.Test");

        // Exercise 4 Solution
        System.out.println("\nExercise 4: Detect ClassLoader Leaks");
        detectClassloaderLeaks();

        // Exercise 5 Solution
        System.out.println("\nExercise 5: ChildFirstClassLoader");
        System.out.println("Delegation order:");
        System.out.println("  1. Check loaded classes");
        System.out.println("  2. Try local load (child-first)");
        System.out.println("  3. Fall back to parent");
        System.out.println("  4. System classes always loaded by parent");
    }
}
