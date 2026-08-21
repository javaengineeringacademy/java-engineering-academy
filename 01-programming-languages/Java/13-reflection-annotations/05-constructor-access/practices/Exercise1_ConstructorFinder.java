package academy.javaengineering.reflection.constructor.practices;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class Exercise1_ConstructorFinder {

    public static Constructor<?> findSimplestConstructor(Class<?> clazz) {
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        if (ctors.length == 0) return null;

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
