package academy.javaengineering.jvm;

/**
 * ClassLoader - Bootstrap, Platform, Application ClassLoaders, Delegation Model.
 */
public class ClassloaderExample {

    public static class CustomClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            throw new ClassNotFoundException("Class not found: " + name);
        }
    }

    public static class ClassLoaderHierarchy {
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

    public static class HotSwapDemo {
        private int version = 1;

        public int getVersion() { return version; }
        public void setVersion(int version) { this.version = version; }
    }

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Demo ===");

        ClassLoaderHierarchy hierarchy = new ClassLoaderHierarchy();
        hierarchy.printHierarchy(HotSwapDemo.class);

        CustomClassLoader customLoader = new CustomClassLoader();
        System.out.println("\nCustom ClassLoader: " + customLoader);
        System.out.println("Parent: " + customLoader.getParent());
    }
}
