package academy.javaengineering.debugging;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates debugging utilities.
 */
public class DebugUtils {

    public static void printObjectState(String label, Object obj) {
        System.out.printf("[%s] Object: %s, HashCode: %d, ToString: %s%n",
            label, obj.getClass().getSimpleName(), obj.hashCode(), obj);
    }

    public static void printCollection(String label, java.util.Collection<?> collection) {
        System.out.printf("[%s] Collection size: %d, Contents: %s%n",
            label, collection.size(), collection);
    }

    public static void printMap(String label, java.util.Map<?, ?> map) {
        System.out.printf("[%s] Map size: %d, Contents: %s%n",
            label, map.size(), map);
    }

    public static void printStackTrace(String message) {
        System.out.printf("[DEBUG] %s%n", message);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < Math.min(stackTrace.length, 8); i++) {
            System.out.printf("  at %s.%s(%s:%d)%n",
                stackTrace[i].getClassName(),
                stackTrace[i].getMethodName(),
                stackTrace[i].getFileName(),
                stackTrace[i].getLineNumber());
        }
    }
}
