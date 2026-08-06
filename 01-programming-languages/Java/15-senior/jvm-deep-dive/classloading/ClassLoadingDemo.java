package academy.javaengineering.senior.jvm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * ClassLoading Demo - Classloader hierarchy, delegation, custom loaders.
 *
 * Class loading phases:
 *   1. Loading    - Find bytecode (.class file or network)
 *   2. Linking    - Verify, prepare, resolve references
 *   3. Initialization - Execute static initializers (<clinit>)
 *
 * Classloader hierarchy:
 *   Bootstrap → Extension/Platform → Application → Custom
 *   Each loader delegates to parent first (delegation model).
 */
public class ClassLoadingDemo {

    // =====================================================
    // DEMO 1: Classloader Hierarchy
    // =====================================================
    public static void classloaderHierarchy() {
        System.out.println("=== Classloader Hierarchy ===");

        ClassLoader appLoader = ClassLoadingDemo.class.getClassLoader();
        ClassLoader platformLoader = appLoader.getParent();
        ClassLoader bootstrapLoader = platformLoader.getParent();

        System.out.println("Application classloader: " + appLoader);
        System.out.println("  → loads classes from classpath (app jars)");
        System.out.println("Platform/Extension loader: " + platformLoader);
        System.out.println("  → loads from JAVA_HOME/lib/ext or module path");
        System.out.println("Bootstrap loader: " + bootstrapLoader);
        System.out.println("  → null (native, loads java.lang.*, java.util.*)");
        System.out.println();
        System.out.println("Delegation chain: App → Platform → Bootstrap");
    }

    // =====================================================
    // DEMO 2: Who Loads What
    // =====================================================
    public static void whoLoadsWhat() {
        System.out.println("\n=== Who Loads What ===");

        System.out.println("java.lang.String → " + String.class.getClassLoader());
        System.out.println("  (Bootstrap loader → null)");

        System.out.println("sun.misc.Unsafe → " + sun.misc.Unsafe.class.getClassLoader());
        System.out.println("  (Bootstrap loader → null)");

        System.out.println("This class → " + ClassLoadingDemo.class.getClassLoader());
        System.out.println("  (Application loader)");
    }

    // =====================================================
    // DEMO 3: ClassNotFoundException vs NoClassDefFoundError
    // =====================================================
    public static void classNotFoundVsNoClassDef() {
        System.out.println("\n=== ClassNotFoundException vs NoClassDefFoundError ===");
        System.out.println();
        System.out.println("ClassNotFoundException:");
        System.out.println("  - Class NOT FOUND during loading phase");
        System.out.println("  - Checked exception from Class.forName(), loadClass()");
        System.out.println("  - Cause: missing jar, wrong classpath, typo");
        System.out.println();
        System.out.println("NoClassDefFoundError:");
        System.out.println("  - Class FOUND but could not be LINKED");
        System.out.println("  - Error (unchecked) thrown at runtime");
        System.out.println("  - Cause: missing dependency, failed static init");
        System.out.println();
        System.out.println("Example ClassNotFoundException:");
        try {
            Class.forName("com.nonexistent.MyClass");
        } catch (ClassNotFoundException e) {
            System.out.println("  Caught: " + e.getMessage());
        }
        System.out.println();
        System.out.println("Example NoClassDefFoundError:");
        try {
            Class.forName("academy.javaengineering.senior.jvm.MissingDependency");
        } catch (ClassNotFoundException e) {
            System.out.println("  Actually ClassNotFoundException: " + e.getMessage());
        }
    }

    // =====================================================
    // DEMO 4: Custom ClassLoader
    // =====================================================
    public static class CustomClassLoader extends ClassLoader {
        private final String prefix;

        public CustomClassLoader(String prefix) {
            this.prefix = prefix;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name.startsWith(prefix)) {
                // Simulate loading from custom source
                System.out.println("  CustomClassLoader: loading " + name);
                byte[] bytes = loadClassBytes(name);
                if (bytes != null) {
                    return defineClass(name, bytes, 0, bytes.length);
                }
            }
            return super.findClass(name);
        }

        private byte[] loadClassBytes(String name) {
            // In real implementation: read from DB, network, encrypted file
            return null;
        }
    }

    // =====================================================
    // DEMO 5: Custom ClassLoader with Resource Loading
    // =====================================================
    public static class ResourceClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String path = name.replace('.', '/') + ".class";
            try {
                InputStream is = getResourceAsStream(path);
                if (is != null) {
                    byte[] bytes = is.readAllBytes();
                    return defineClass(name, bytes, 0, bytes.length);
                }
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
            return super.findClass(name);
        }
    }

    // =====================================================
    // DEMO 6: ClassLoader Isolation
    // =====================================================
    public static void classloaderIsolation() {
        System.out.println("\n=== ClassLoader Isolation ===");
        System.out.println("Different classloaders → different Class objects");
        System.out.println("Even if same bytecode, classes are NOT equal:");
        System.out.println("  loader1.load(\"Foo\") != loader2.load(\"Foo\")");
        System.out.println();
        System.out.println("Use cases:");
        System.out.println("  - Hot deployment: reload classes without restart");
        System.out.println("  - Plugin isolation: plugins can't see each other");
        System.out.println("  - OSGi: module-level classloader isolation");
    }

    // =====================================================
    // DEMO 7: Class Loading Phases
    // =====================================================
    public static void classLoadingPhases() {
        System.out.println("\n=== Class Loading Phases ===");
        System.out.println("1. LOADING");
        System.out.println("   - Find .class file via classloader");
        System.out.println("   - Create java.lang.Class object");
        System.out.println();
        System.out.println("2. LINKING");
        System.out.println("   a. VERIFY: Check bytecode format, constant pool");
        System.out.println("   b. PREPARE: Allocate memory for static fields");
        System.out.println("   c. RESOLVE: Replace symbolic references with direct refs");
        System.out.println();
        System.out.println("3. INITIALIZATION");
        System.out.println("   - Execute <clinit> (static initializers, static blocks)");
        System.out.println("   - Thread-safe: JVM guarantees single initialization");
    }

    public static void main(String[] args) throws Exception {
        classloaderHierarchy();
        whoLoadsWhat();
        classNotFoundVsNoClassDef();
        classloaderIsolation();
        classLoadingPhases();

        System.out.println("\n=== Custom ClassLoader Demo ===");
        CustomClassLoader loader = new CustomClassLoader("academy.");
        System.out.println("Custom loader parent: " + loader.getParent());
        System.out.println("Custom loader can load classes: " + loader);
    }
}