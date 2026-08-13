package academy.javaengineering.generics.internals;

import java.util.*;

public class RestrictionsInternals {

    static class Container<T> {
        T value;
        public Container(T value) { this.value = value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Restrictions on Generics ===\n");

        // 1. No Primitive Types
        System.out.println("--- No Primitives ---");
        System.out.println("Cannot: Container<int>");
        System.out.println("Must use: Container<Integer>");
        System.out.println("Reason: generics work with Object references");

        // 2. No Static Members
        System.out.println("\n--- No Static ---");
        System.out.println("Cannot: static T value");
        System.out.println("Cannot: static T method()");
        System.out.println("Reason: T belongs to instance, not class");

        // 3. No instanceof
        System.out.println("\n--- No instanceof ---");
        System.out.println("Cannot: obj instanceof Container<String>");
        System.out.println("Can: obj instanceof Container<?>");
        System.out.println("Reason: type erased at runtime");

        // 4. No New T()
        System.out.println("\n--- No new T() ---");
        System.out.println("Cannot: new T()");
        System.out.println("Cannot: T.class");
        System.out.println("Reason: T not known at runtime");

        // 5. No Arrays of Parameterized Types
        System.out.println("\n--- No Generic Arrays ---");
        System.out.println("Cannot: new List<String>[10]");
        System.out.println("Can: List<?>[] arr = new List[10]");
        System.out.println("Reason: array needs reifiable type");

        // 6. No Overloading with Varargs
        System.out.println("\n--- Varargs Restrictions ---");
        System.out.println("@SafeVarargs required for generic varargs");
        System.out.println("Prevents heap pollution");
    }
}
