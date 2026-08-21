package academy.javaengineering.reflection.constructor.solutions;

import java.lang.reflect.Constructor;

public class Solution1_ConstructorFinder {

    public static Constructor<?> findSimplestConstructor(Class<?> clazz) {
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        Constructor<?> simplest = ctors[0];
        for (Constructor<?> ctor : ctors) {
            if (ctor.getParameterCount() < simplest.getParameterCount()) {
                simplest = ctor;
            }
        }
        return simplest;
    }

    public static void main(String[] args) {
        System.out.println(findSimplestConstructor(StringBuilder.class));
    }
}
