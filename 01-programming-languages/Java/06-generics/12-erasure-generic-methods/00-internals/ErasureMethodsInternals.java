package academy.javaengineering.generics.internals;

import java.lang.reflect.*;
import java.util.*;

public class ErasureMethodsInternals {

    static <T> T identity(T value) {
        return value;
    }

    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Erasure of Generic Methods ===\n");

        // 1. Method Erasure Rules
        System.out.println("--- Method Erasure ---");
        System.out.println("<T> T method(T arg) -> Object method(Object arg)");
        System.out.println("<T extends X> T method(T arg) -> X method(X arg)");
        System.out.println("Return type and parameters erased separately");

        // 2. Bytecode Inspection
        System.out.println("\n--- Bytecode ---");
        Method m = ErasureMethodsInternals.class.getDeclaredMethod("identity", Object.class);
        System.out.println("identity method: " + m);
        System.out.println("Parameter type: " + m.getParameterTypes()[0]);
        System.out.println("Return type: " + m.getReturnType());

        // 3. Bridge Methods
        System.out.println("\n--- Bridge Methods ---");
        Method[] methods = ErasureMethodsInternals.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName() + " -> " + method.getReturnType());
        }

        // 4. Cast Insertion
        System.out.println("\n--- Cast Insertion ---");
        System.out.println("String result = identity(\"hello\")");
        System.out.println("Compiler inserts: (String) identity(\"hello\")");
        System.out.println("Runtime: Object -> String cast");

        // 5. Multiple Type Parameters
        System.out.println("\n--- Multiple Params ---");
        System.out.println("<T,R> R convert(T, Function<T,R>)");
        System.out.println("Both T and R erased independently");
    }
}
