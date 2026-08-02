package academy.javaengineering.jvm;

/**
 * JVM Architecture - ClassLoader, Runtime Data Areas, Execution Engine.
 */
public class JvmArchitectureExample {

    public static class ClassLoaderDemo {
        public void printClassLoaderInfo() {
            ClassLoader appLoader = ClassLoaderDemo.class.getClassLoader();
            ClassLoader platformLoader = appLoader.getParent();
            ClassLoader bootstrapLoader = platformLoader != null ? platformLoader.getParent() : null;

            System.out.println("Application ClassLoader: " + appLoader);
            System.out.println("Platform ClassLoader: " + platformLoader);
            System.out.println("Bootstrap ClassLoader: " + bootstrapLoader);
        }
    }

    public static class RuntimeDataAreas {
        private static int staticVariable = 10;
        private int instanceVariable = 20;

        public void demonstrateStackFrame(int param) {
            int localVar = 30;
            System.out.println("Static: " + staticVariable);
            System.out.println("Instance: " + instanceVariable);
            System.out.println("Local: " + localVar);
            System.out.println("Parameter: " + param);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Architecture Demo ===");

        ClassLoaderDemo classLoaderDemo = new ClassLoaderDemo();
        classLoaderDemo.printClassLoaderInfo();

        RuntimeDataAreas runtimeData = new RuntimeDataAreas();
        runtimeData.demonstrateStackFrame(40);

        System.out.println("\n=== Memory Info ===");
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");
    }
}
