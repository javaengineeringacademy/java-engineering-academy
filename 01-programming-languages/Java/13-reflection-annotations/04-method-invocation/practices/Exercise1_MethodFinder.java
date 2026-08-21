package academy.javaengineering.reflection.methodinvocation.practices;

import java.lang.reflect.Method;
import java.util.*;

public class Exercise1_MethodFinder {

    public static List<String> findMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        List<String> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (returnType.isAssignableFrom(m.getReturnType())) {
                    result.add(m.getName());
                }
            }
            current = current.getSuperclass();
        }
        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findMethodsByReturnType(ArrayList.class, boolean.class));
    }
}
