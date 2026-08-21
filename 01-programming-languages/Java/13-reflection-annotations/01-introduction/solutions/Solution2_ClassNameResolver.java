package academy.javaengineering.reflection.intro.solutions;

public class Solution2_ClassNameResolver {

    private static final String[] COMMON_PACKAGES = {
        "java.lang", "java.util", "java.io", "java.net",
        "java.math", "java.time", "java.util.concurrent", "java.util.function"
    };

    public static Class<?> resolve(String simpleName) {
        if (simpleName == null || simpleName.isEmpty()) return null;

        try {
            return Class.forName(simpleName);
        } catch (ClassNotFoundException ignored) {}

        for (String pkg : COMMON_PACKAGES) {
            try {
                return Class.forName(pkg + "." + simpleName);
            } catch (ClassNotFoundException ignored) {}
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(resolve("String"));
        System.out.println(resolve("ArrayList"));
        System.out.println(resolve("NoSuchClass"));
    }
}
