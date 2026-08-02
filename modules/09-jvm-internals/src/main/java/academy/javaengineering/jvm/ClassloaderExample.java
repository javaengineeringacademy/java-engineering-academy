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

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Demo ===");
        new ClassLoaderHierarchy().printHierarchy(ClassloaderExample.class);
        CustomClassLoader customLoader = new CustomClassLoader();
        System.out.println("Custom ClassLoader: " + customLoader);
        System.out.println("Parent: " + customLoader.getParent());
    }
}
