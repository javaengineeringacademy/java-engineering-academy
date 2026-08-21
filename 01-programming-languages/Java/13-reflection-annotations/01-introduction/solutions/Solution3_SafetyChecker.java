package academy.javaengineering.reflection.intro.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution3_SafetyChecker {

    public static List<String> checkInstantiationSafety(Class<?> clazz) {
        List<String> issues = new ArrayList<>();

        if (clazz.isInterface()) issues.add("Cannot instantiate an interface");
        if (Modifier.isAbstract(clazz.getModifiers())) issues.add("Cannot instantiate an abstract class");
        if (clazz.isPrimitive()) issues.add("Cannot instantiate a primitive type");
        if (clazz.isArray()) issues.add("Cannot instantiate an array type directly");
        if (clazz.isAnnotation()) issues.add("Cannot instantiate an annotation type");
        if (clazz.isEnum()) issues.add("Cannot instantiate an enum type via constructor");

        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            if (!Modifier.isPublic(ctor.getModifiers())) {
                issues.add("No-arg constructor is not public");
            }
        } catch (NoSuchMethodException e) {
            issues.add("No default (no-arg) constructor found");
        }

        return issues;
    }

    public static void main(String[] args) {
        System.out.println("String: " + checkInstantiationSafety(String.class));
        System.out.println("ArrayList: " + checkInstantiationSafety(java.util.ArrayList.class));
        System.out.println("Serializable: " + checkInstantiationSafety(java.io.Serializable.class));
    }
}
