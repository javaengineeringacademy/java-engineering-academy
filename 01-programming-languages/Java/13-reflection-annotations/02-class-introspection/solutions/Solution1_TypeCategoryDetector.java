package academy.javaengineering.reflection.classintrospection.solutions;

import java.lang.reflect.Modifier;

public class Solution1_TypeCategoryDetector {

    public enum Category {
        PRIMITIVE, ARRAY, ENUM, INTERFACE, ANNOTATION, ABSTRACT_CLASS, CONCRETE_CLASS
    }

    public static Category classify(Class<?> clazz) {
        if (clazz.isPrimitive()) return Category.PRIMITIVE;
        if (clazz.isArray()) return Category.ARRAY;
        if (clazz.isEnum()) return Category.ENUM;
        if (clazz.isInterface()) return Category.INTERFACE;
        if (clazz.isAnnotation()) return Category.ANNOTATION;
        if (Modifier.isAbstract(clazz.getModifiers())) return Category.ABSTRACT_CLASS;
        return Category.CONCRETE_CLASS;
    }

    public static void main(String[] args) {
        System.out.println(classify(int.class));
        System.out.println(classify(String[].class));
        System.out.println(classify(Thread.State.class));
        System.out.println(classify(Runnable.class));
        System.out.println(classify(Deprecated.class));
        System.out.println(classify(String.class));
    }
}
