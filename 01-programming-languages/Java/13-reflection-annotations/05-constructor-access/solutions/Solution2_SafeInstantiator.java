package academy.javaengineering.reflection.constructor.solutions;

import java.lang.reflect.Constructor;

public class Solution2_SafeInstantiator {

    public static <T> T safeNewInstance(Class<T> clazz) {
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            try {
                ctor.setAccessible(true);
                if (ctor.getParameterCount() == 0) {
                    return clazz.cast(ctor.newInstance());
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static void main(String[] args) {
        StringBuilder sb = safeNewInstance(StringBuilder.class);
        System.out.println("Created: " + sb.getClass().getSimpleName());
    }
}
