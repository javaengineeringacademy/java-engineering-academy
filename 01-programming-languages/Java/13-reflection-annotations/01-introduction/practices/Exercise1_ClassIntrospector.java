package academy.javaengineering.reflection.intro.practices;

import java.lang.reflect.*;
import java.util.*;

public class Exercise1_ClassIntrospector {

    public static void printClassInfo(Object obj) {
        Class<?> clazz = obj.getClass();

        // Build hierarchy
        List<String> hierarchy = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            hierarchy.add(current.getSimpleName());
            current = current.getSuperclass();
        }

        // Collect all interfaces
        Set<String> interfaces = new LinkedHashSet<>();
        current = clazz;
        while (current != null) {
            for (Class<?> iface : current.getInterfaces()) {
                collectInterfaces(iface, interfaces);
            }
            current = current.getSuperclass();
        }

        System.out.println("Hierarchy: " + String.join(" -> ", hierarchy));
        System.out.println("Interfaces: " + String.join(", ", interfaces));
        System.out.println();
    }

    private static void collectInterfaces(Class<?> iface, Set<String> result) {
        if (result.add(iface.getSimpleName())) {
            for (Class<?> parent : iface.getInterfaces()) {
                collectInterfaces(parent, result);
            }
        }
    }

    public static void main(String[] args) {
        printClassInfo("hello");
        printClassInfo(new java.util.ArrayList<>());
        printClassInfo(42);
    }
}
