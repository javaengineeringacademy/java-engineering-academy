package academy.javaengineering.jvm.examples;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;

/**
 * Custom ClassLoader Demo
 * Demonstrates the class loading delegation model, custom classloaders,
 * and how class isolation works in practice.
 */
public class ClassLoaderDemo {

    /**
     * DEMO 1: ClassLoader Hierarchy
     * Bootstrap -> Platform (Extension) -> Application -> Custom
     */
    public static void demonstrateHierarchy() {
        System.out.println("=== ClassLoader Hierarchy ===");

        ClassLoader appLoader = ClassLoaderDemo.class.getClassLoader();
        ClassLoader platformLoader = appLoader.getParent();
        ClassLoader bootstrapLoader = platformLoader.getParent();

        System.out.println("Application ClassLoader: " + appLoader);
        System.out.println("  Class: " + appLoader.getClass().getName());
        System.out.println("Platform ClassLoader: " + platformLoader);
        System.out.println("  Class: " + platformLoader.getClass().getName());
        System.out.println("Bootstrap ClassLoader: " + bootstrapLoader);
        System.out.println("  (returns null - implemented in native code)");

        // Classes loaded by bootstrap
        System.out.println("\nBootstrap-loaded classes:");
        System.out.println("  String -> " + String.class.getClassLoader());
        System.out.println("  Integer -> " + Integer.class.getClassLoader());
        System.out.println("  Thread -> " + Thread.class.getClassLoader());

        // Classes loaded by application loader
        System.out.println("\nApplication-loaded classes:");
        System.out.println("  ClassLoaderDemo -> " + appLoader.getClass().getName());
    }

    /**
     * DEMO 2: Delegation Model (Parent-First)
     * When a class is requested:
     * 1. Check if already loaded (cache)
     * 2. Delegate to parent classloader
     * 3. If parent fails, attempt to load locally
     * 4. If local fails, throw ClassNotFoundException
     */
    public static void demonstrateDelegation() {
        System.out.println("\n=== Delegation Model (Parent-First) ===");

        System.out.println("Load request flow for 'com.example.MyClass':");
        System.out.println("  Application CL -> check cache");
        System.out.println("  Application CL -> delegate to Platform CL");
        System.out.println("  Platform CL -> delegate to Bootstrap CL");
        System.out.println("  Bootstrap CL -> try to load");
        System.out.println("  Bootstrap CL -> FAILS (not in java.base)");
        System.out.println("  Platform CL -> try to load");
        System.out.println("  Platform CL -> FAILS (not in platform modules)");
        System.out.println("  Application CL -> try to load from classpath");
        System.out.println("  Application CL -> LOADS or throws ClassNotFoundException");

        // Demonstrate finding class locations
        System.out.println("\nClasspath entries:");
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(File.pathSeparator);
        for (int i = 0; i < Math.min(paths.length, 5); i++) {
            System.out.println("  [" + i + "] " + paths[i]);
        }
        if (paths.length > 5) {
            System.out.println("  ... and " + (paths.length - 5) + " more");
        }
    }

    /**
     * DEMO 3: Custom ClassLoader Implementation
     */
    static class CustomClassLoader extends ClassLoader {
        private final String loadPath;

        public CustomClassLoader(String loadPath) {
            this.loadPath = loadPath;
        }

        public CustomClassLoader(String loadPath, ClassLoader parent) {
            super(parent);
            this.loadPath = loadPath;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("  [CustomCL] findClass called for: " + name);

            // Convert class name to file path
            String fileName = name.replace('.', File.separatorChar) + ".class";
            Path classFile = Paths.get(loadPath, fileName);

            try {
                byte[] bytes = Files.readAllBytes(classFile);
                System.out.println("  [CustomCL] Read " + bytes.length + " bytes from " + classFile);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Could not load " + name, e);
            }
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            System.out.println("  [CustomCL] getResourceAsStream: " + name);
            return super.getResourceAsStream(name);
        }
    }

    /**
     * DEMO 4: Class Isolation
     * Different classloaders can load the same class name independently.
     * They are considered different classes by the JVM.
     */
    public static void demonstrateIsolation() {
        System.out.println("\n=== Class Isolation ===");

        ClassLoader appLoader = ClassLoaderDemo.class.getClassLoader();

        // Create two custom classloaders with same parent
        // (In practice, they'd load from different directories)
        CustomClassLoader cl1 = new CustomClassLoader("/tmp/classes1", appLoader);
        CustomClassLoader cl2 = new CustomClassLoader("/tmp/classes2", appLoader);

        System.out.println("ClassLoader1 instance: " + System.identityHashCode(cl1));
        System.out.println("ClassLoader2 instance: " + System.identityHashCode(cl2));
        System.out.println("Different classloaders -> different class instances");
        System.out.println("  (even if loading same bytecode from different locations)");
    }

    /**
     * DEMO 5: Type Casting Across ClassLoaders
     */
    public static void demonstrateTypeCasting() {
        System.out.println("\n=== Type Casting Across ClassLoaders ===");
        System.out.println("ClassCastException occurs when:");
        System.out.println("  1. Same class loaded by different classloaders");
        System.out.println("  2. Attempting to cast between them");
        System.out.println("  3. JVM considers them different types");
        System.out.println("\nSolution: Use same classloader or shared interface");
    }

    /**
     * DEMO 6: Thread Context ClassLoader
     */
    public static void demonstrateThreadContextCL() {
        System.out.println("\n=== Thread Context ClassLoader ===");
        Thread current = Thread.currentThread();
        ClassLoader contextCL = current.getContextClassLoader();
        System.out.println("Thread context classloader: " + contextCL);
        System.out.println("Used by: JDBC drivers, JNDI, XML parsers");
        System.out.println("Purpose: Break parent-first delegation for SPI");

        // Setting custom context classloader
        // current.setContextClassLoader(new CustomClassLoader("/tmp", null));
    }

    /**
     * DEMO 7: Class Loading Tracing
     */
    public static void demonstrateTracing() {
        System.out.println("\n=== Class Loading Tracing ===");
        System.out.println("Use -XX:+TraceClassLoading and -XX:+TraceClassUnloading flags:");
        System.out.println("  java -XX:+TraceClassLoading -XX:+TraceClassUnloading MyApp");
        System.out.println("\nProgrammatic approach with -verbose:class:");
        System.out.println("  java -verbose:class MyApp");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║    CUSTOM CLASSLOADER DEMO          ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateHierarchy();
        demonstrateDelegation();
        demonstrateIsolation();
        demonstrateTypeCasting();
        demonstrateThreadContextCL();
        demonstrateTracing();
    }
}
