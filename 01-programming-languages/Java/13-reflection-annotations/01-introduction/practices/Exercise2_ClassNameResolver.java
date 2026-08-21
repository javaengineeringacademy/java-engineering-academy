package academy.javaengineering.reflection.intro.practices;

public class Exercise2_ClassNameResolver {

    private static final String[] COMMON_PACKAGES = {
        "java.lang",
        "java.util",
        "java.io",
        "java.net",
        "java.math",
        "java.time",
        "java.util.concurrent",
        "java.util.function"
    };

    public static Class<?> resolve(String simpleName) {
        if (simpleName == null || simpleName.isEmpty()) {
            return null;
        }

        // Try direct fully-qualified name first
        try {
            return Class.forName(simpleName);
        } catch (ClassNotFoundException ignored) {
        }

        // Try common packages
        for (String pkg : COMMON_PACKAGES) {
            try {
                return Class.forName(pkg + "." + simpleName);
            } catch (ClassNotFoundException ignored) {
            }
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(resolve("String"));           // java.lang.String
        System.out.println(resolve("ArrayList"));        // java.util.ArrayList
        System.out.println(resolve("NoSuchClass"));      // null
        System.out.println(resolve("HashMap"));          // java.util.HashMap
        System.out.println(resolve("File"));             // java.io.File
    }
}
