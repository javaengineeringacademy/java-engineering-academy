package reflection.exercises;

import java.lang.reflect.*;
import java.util.*;

/**
 * TOPIC 4 & 5 EXERCISES — Method Invocation and Constructor Access
 * 5 practice problems.
 */
public class MethodAndConstructorExercises {

    // =========================================================================
    // EXERCISE 1: Invoke Method by Name
    // =========================================================================
    /**
     * Given an object, a method name, and arguments, invoke the method
     * using reflection. Find the correct method by matching parameter count
     * and types. Return the result (null for void methods).
     *
     * TODO: Implement this method
     */
    public static Object invokeByName(Object obj, String methodName, Object... args) 
            throws Exception {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Get All Method Signatures
    // =========================================================================
    /**
     * Given a Class object, return a sorted list of all method signatures
     * in format "returnType methodName(paramType1, paramType2)".
     * Include only declared methods (not inherited).
     *
     * TODO: Implement this method
     */
    public static List<String> getMethodSignatures(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Create Instance with Constructor Matching
    // =========================================================================
    /**
     * Given a Class and constructor arguments, find the best matching
     * constructor and create an instance. Handle primitive type matching.
     *
     * TODO: Implement this method
     */
    public static <T> T createInstance(Class<T> clazz, Object... args) throws Exception {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Find Methods Returning Type
    // =========================================================================
    /**
     * Given a Class and a return type, find all declared methods that
     * return that type (or a subtype). Return their names sorted.
     *
     * TODO: Implement this method
     */
    public static List<String> findMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: Safe Method Invocation with Exception Unwrapping
    // =========================================================================
    /**
     * Invoke a method and properly unwrap InvocationTargetException.
     * If the method throws an exception, re-throw the actual exception
     * (not the InvocationTargetException wrapper).
     * If the method succeeds, return its result.
     *
     * TODO: Implement this method
     */
    public static Object safeInvoke(Object obj, Method method, Object... args) 
            throws Exception {
        // TODO: Your code here
        return null;
    }
}
