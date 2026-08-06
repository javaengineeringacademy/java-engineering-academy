package academy.javaengineering.jvm.classloader;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassLoader Deep Dive
 * Covers Bootstrap, Platform, Application classloaders, parent delegation,
 * custom classloader creation, and classloader isolation patterns.
 */
public class ClassLoadingDeepDive {

    // Track loaded classloaders for leak detection
    private static final Map<String, WeakReference<ClassLoader>> classloaderRegistry =
            new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("=== ClassLoader Deep Dive ===\n");

        // 1. Bootstrap ClassLoader
        demonstrateBootstrapClassLoader();

        // 2. Platform ClassLoader
        demonstratePlatformClassLoader();

        // 3. Application ClassLoader
        demonstrateApplicationClassLoader();

        // 4. Parent Delegation Model
        demonstrateParentDelegation();

        // 5. Custom ClassLoader Creation
        demonstrateCustomClassLoader();

        // 6. ClassLoader Isolation
        demonstrateClassLoaderIsolation();

        // 7. Thread Context ClassLoader
        demonstrateThreadContextClassLoader();

        // 8. ClassLoader Leak Detection
        demonstrateClassLoaderLeaks();
    }

    /**
     * Bootstrap ClassLoader
     * - Implemented in native code (C/C++)
     * - Loads core Java classes from rt.jar / java.base module
     * - Reference is null in Java code
     * - Cannot be extended or overridden
     */
    private static void demonstrateBootstrapClassLoader() {
        System.out.println("--- 1. Bootstrap ClassLoader ---");

        // Core Java classes are loaded by Bootstrap ClassLoader
        Class<?> stringClass = String.class;
        Class<?> systemClass = System.class;
        Class<?> objectClass = Object.class;

        System.out.println("String.class.getClassLoader() = " + stringClass.getClassLoader());
        System.out.println("System.class.getClassLoader() = " + systemClass.getClassLoader());
        System.out.println("Object.class.getClassLoader() = " + objectClass.getClassLoader());
        System.out.println("  (null means Bootstrap ClassLoader - native code)\n");

        // Bootstrap loads from: $JAVA_HOME/lib (rt.jar, etc.)
        System.out.println("Bootstrap ClassLoader loads from:");
        System.out.println("  - java.base module (java.lang, java.util, etc.)");
        System.out.println("  - rt.jar (pre-Java 9)");
        System.out.println("  - Modules in $JAVA_HOME/lib/\n");
    }

    /**
     * Platform ClassLoader
     * - Replaces Extension ClassLoader (pre-Java 9)
     * - Loads platform-specific modules (java.xml, java.sql, etc.)
     * - Parent is Bootstrap ClassLoader
     */
    private static void demonstratePlatformClassLoader() {
        System.out.println("--- 2. Platform ClassLoader ---");

        try {
            // Platform classes like javax.xml are loaded by Platform ClassLoader
            Class<?> xmlClass = Class.forName("javax.xml.parsers.DocumentBuilderFactory");
            ClassLoader platformLoader = xmlClass.getClassLoader();
            System.out.println("DocumentBuilderFactory classloader: " + platformLoader);
            System.out.println("  (Platform ClassLoader - loads java.xml module)\n");

            // In Java 9+, Platform ClassLoader replaces Extension ClassLoader
            System.out.println("Platform ClassLoader (Java 9+):");
            System.out.println("  - Replaces Extension ClassLoader");
            System.out.println("  - Loads: java.xml, java.sql, java.logging, etc.");
            System.out.println("  - Does NOT load: javax.*, java.*, sun.* (use Application CL)\n");
        } catch (ClassNotFoundException e) {
            System.out.println("XML classes not found (module system may restrict access)\n");
        }
    }

    /**
     * Application ClassLoader
     * - Loads classes from classpath
     * - Parent is Platform ClassLoader
     * - Default classloader for application classes
     */
    private static void demonstrateApplicationClassLoader() {
        System.out.println("--- 3. Application ClassLoader ---");

        ClassLoader appLoader = ClassLoadingDeepDive.class.getClassLoader();
        System.out.println("Application classloader: " + appLoader);
        System.out.println("  Class: " + appLoader.getClass().getName());
        System.out.println("  Parent: " + appLoader.getParent());
        System.out.println("  Grandparent: " + (appLoader.getParent() != null ?
            appLoader.getParent().getParent() : "null (Bootstrap)\n"));

        // Show classpath
        System.out.println("Classpath entries:");
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(File.pathSeparator);
        for (String path : paths) {
            System.out.println("  - " + path);
        }
        System.out.println();
    }

    /**
     * Parent Delegation Model
     * Request flow: Application → Platform → Bootstrap → (back down if not found)
     */
    private static void demonstrateParentDelegation() {
        System.out.println("--- 4. Parent Delegation Model ---");

        System.out.println("Delegation chain for loading 'com.example.MyClass':");
        System.out.println("  1. Application ClassLoader receives request");
        System.out.println("  2. Delegates to Platform ClassLoader");
        System.out.println("  3. Platform delegates to Bootstrap ClassLoader");
        System.out.println("  4. Bootstrap: not found → returns null");
        System.out.println("  5. Platform: not found → delegates back to Application");
        System.out.println("  6. Application: tries to load from classpath");
        System.out.println("  7. If found: defineClass() → return Class object");
        System.out.println("  8. If not found: throw ClassNotFoundException\n");

        // Demonstrate with actual classes
        System.out.println("Actual delegation results:");
        System.out.println("  String (core): loaded by Bootstrap");
        System.out.println("  " + ClassLoadingDeepDive.class.getName() + ": loaded by Application");
        System.out.println("  java.util.HashMap: loaded by Bootstrap\n");

        // Show classloader hierarchy
        System.out.println("Classloader hierarchy:");
        printClassloaderHierarchy(ClassLoadingDeepDive.class.getClassLoader(), 0);
        System.out.println();
    }

    /**
     * Custom ClassLoader Creation
     * Demonstrates creating a classloader that reads from a specific directory
     */
    private static void demonstrateCustomClassLoader() throws Exception {
        System.out.println("--- 5. Custom ClassLoader Creation ---");

        // Create a custom classloader that loads from a directory
        Path classesDir = Path.of(System.getProperty("user.dir"), "target", "classes");
        if (Files.exists(classesDir)) {
            DirectoryClassLoader customLoader = new DirectoryClassLoader(
                    classesDir, ClassLoadingDeepDive.class.getClassLoader());

            System.out.println("Custom DirectoryClassLoader created:");
            System.out.println("  Directory: " + classesDir.toAbsolutePath());
            System.out.println("  Parent: " + customLoader.getParent().getClass().getName());

            // Load a class through the custom classloader
            try {
                Class<?> clazz = customLoader.loadClass(
                        "academy.javaengineering.jvm.classloader.ClassLoadingDeepDive");
                System.out.println("  Loaded class: " + clazz.getName());
                System.out.println("  Classloader: " + clazz.getClassLoader());
                System.out.println("  Same as Application CL? " +
                        (clazz.getClassLoader() == ClassLoadingDeepDive.class.getClassLoader()));
            } catch (ClassNotFoundException e) {
                System.out.println("  Class not found in custom classloader path");
            }
        } else {
            System.out.println("Target/classes directory not found, demonstrating custom CL pattern:");
            System.out.println("  Custom ClassLoader should:");
            System.out.println("  1. Extend ClassLoader");
            System.out.println("  2. Override findClass(String name)");
            System.out.println("  3. Read .class bytes from custom source");
            System.out.println("  4. Call defineClass(name, bytes, 0, len)");
        }
        System.out.println();
    }

    /**
     * ClassLoader Isolation
     * Different classloaders loading same class name produce different Class objects
     */
    private static void demonstrateClassLoaderIsolation() {
        System.out.println("--- 6. ClassLoader Isolation ---");

        ClassLoader parent = ClassLoadingDeepDive.class.getClassLoader();

        IsolationClassLoader loader1 = new IsolationClassLoader("module-1", parent);
        IsolationClassLoader loader2 = new IsolationClassLoader("module-2", parent);

        System.out.println("Two isolation classloaders created:");
        System.out.println("  loader1: " + loader1);
        System.out.println("  loader2: " + loader2);
        System.out.println("  loader1 != loader2: " + (loader1 != loader2));
        System.out.println("  Each classloader maintains its own namespace\n");

        System.out.println("Isolation benefits:");
        System.out.println("  - Plugin systems (OSGi, Spring Boot DevTools)");
        System.out.println("  - Web application containers (Tomcat, Jetty)");
        System.out.println("  - Hot deployment/reloading");
        System.out.println("  - Class version management\n");
    }

    /**
     * Thread Context ClassLoader
     * Used by JDBC, JNDI, etc. to break parent delegation
     */
    private static void demonstrateThreadContextClassLoader() {
        System.out.println("--- 7. Thread Context ClassLoader ---");

        Thread currentThread = Thread.currentThread();
        ClassLoader contextLoader = currentThread.getContextClassLoader();
        ClassLoader appLoader = ClassLoadingDeepDive.class.getClassLoader();

        System.out.println("Current thread: " + currentThread.getName());
        System.out.println("Context ClassLoader: " + contextLoader);
        System.out.println("Application ClassLoader: " + appLoader);
        System.out.println("Same? " + (contextLoader == appLoader) + "\n");

        System.out.println("Why Thread Context ClassLoader?");
        System.out.println("  - JDBC: DriverManager uses context CL to load drivers");
        System.out.println("  - JNDI: Naming services use context CL");
        System.out.println("  - Spring: ApplicationContext uses context CL");
        System.out.println("  - Breaks parent delegation (loads from child classloader)\n");

        System.out.println("Setting context classloader:");
        System.out.println("  Thread.currentThread().setContextClassLoader(myLoader);");
        System.out.println("  Class.forName(\"com.example.MyClass\", true, contextLoader);\n");
    }

    /**
     * ClassLoader Leak Detection
     * Common causes of classloader leaks in long-running applications
     */
    private static void demonstrateClassLoaderLeaks() {
        System.out.println("--- 8. ClassLoader Leak Detection ---");

        System.out.println("Common classloader leak sources:");
        System.out.println("  1. ThreadLocal values not removed");
        System.out.println("     - ThreadLocal.set() holds reference to classloader");
        System.out.println("     - Fix: Use InheritableThreadLocal or remove in finally");
        System.out.println("  2. JDBC drivers not deregistered");
        System.out.println("     - DriverManager.registerDriver() holds static reference");
        System.out.println("     - Fix: Call DriverManager.deregisterDriver() in close()");
        System.out.println("  3. JNDI bindings not unbound");
        System.out.println("     - InitialContext.bind() holds reference");
        System.out.println("     - Fix: Call unbind() in cleanup");
        System.out.println("  4. Static fields holding classloader references");
        System.out.println("     - Static collections prevent GC");
        System.out.println("     - Use WeakReference for caches");
        System.out.println("  5. RMI/Remote objects not unexported");
        System.out.println("     - UnicastRemoteObject.exportObject() leaks");
        System.out.println("     - Fix: UnicastRemoteObject.unexportObject()");
        System.out.println();

        // Monitor loaded classes
        System.out.println("Current class loading stats:");
        System.out.println("  Loaded classes: " +
                java.lang.management.ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
        System.out.println("  Total loaded: " +
                java.lang.management.ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount());
        System.out.println("  Unloaded: " +
                java.lang.management.ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount());
        System.out.println();
    }

    private static void printClassloaderHierarchy(ClassLoader loader, int indent) {
        String indentStr = "  ".repeat(indent);
        if (loader == null) {
            System.out.println(indentStr + "Bootstrap ClassLoader (null - native)");
            return;
        }
        System.out.println(indentStr + loader.getClass().getSimpleName() +
                " (" + loader.getClass().getName() + ")");
        printClassloaderHierarchy(loader.getParent(), indent + 1);
    }

    /**
     * Custom DirectoryClassLoader - loads .class files from a directory
     */
    static class DirectoryClassLoader extends ClassLoader {
        private final Path classesDir;
        private final Map<String, byte[]> classCache = new HashMap<>();

        DirectoryClassLoader(Path classesDir, ClassLoader parent) {
            super(parent);
            this.classesDir = classesDir;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String path = name.replace('.', '/') + ".class";
            Path classFile = classesDir.resolve(path);

            if (Files.exists(classFile)) {
                try {
                    byte[] bytes = Files.readAllBytes(classFile);
                    classCache.put(name, bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException("Failed to load: " + name, e);
                }
            }
            throw new ClassNotFoundException(name);
        }

        public Map<String, byte[]> getClassCache() {
            return Collections.unmodifiableMap(classCache);
        }
    }

    /**
     * Isolation ClassLoader - for module/plugin isolation
     */
    static class IsolationClassLoader extends ClassLoader {
        private final String moduleName;

        IsolationClassLoader(String moduleName, ClassLoader parent) {
            super(parent);
            this.moduleName = moduleName;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // In real implementation, load from module-specific source
            throw new ClassNotFoundException(name);
        }

        @Override
        public String toString() {
            return "IsolationClassLoader[" + moduleName + "]";
        }
    }
}
