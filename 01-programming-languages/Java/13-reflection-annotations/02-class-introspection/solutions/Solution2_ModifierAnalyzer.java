package academy.javaengineering.reflection.classintrospection.solutions;

import java.lang.reflect.Modifier;
import java.util.*;

public class Solution2_ModifierAnalyzer {

    public static List<String> analyzeModifiers(Class<?> clazz) {
        int mods = clazz.getModifiers();
        List<String> result = new ArrayList<>();

        if (Modifier.isPublic(mods)) result.add("public");
        if (Modifier.isPrivate(mods)) result.add("private");
        if (Modifier.isProtected(mods)) result.add("protected");
        if (Modifier.isStatic(mods)) result.add("static");
        if (Modifier.isFinal(mods)) result.add("final");
        if (Modifier.isAbstract(mods)) result.add("abstract");
        if (Modifier.isInterface(mods)) result.add("interface");
        if (Modifier.isEnum(mods)) result.add("enum");
        if (Modifier.isAnnotation(mods)) result.add("annotation");
        if (Modifier.isSynthetic(mods)) result.add("synthetic");

        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(analyzeModifiers(String.class));
        System.out.println(analyzeModifiers(Runnable.class));
        System.out.println(analyzeModifiers(int.class));
    }
}
