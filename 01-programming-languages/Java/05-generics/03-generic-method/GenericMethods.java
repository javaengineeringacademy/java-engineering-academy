package academy.javaengineering.generics;

import java.util.Arrays;
import java.util.List;

public class GenericMethods {

    public static <T> void printArray(T[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static <T> void swap(T[] array, int i, int j) {
        if (array == null || i < 0 || j < 0 || i >= array.length || j >= array.length) {
            throw new IllegalArgumentException("Invalid indices or null array");
        }
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static <T extends Comparable<T>> T findMax(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) max = array[i];
        }
        return max;
    }

    public static <T extends Comparable<T>> T findMin(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        T min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(min) < 0) min = array[i];
        }
        return min;
    }

    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }

    public static <T> int countOccurrences(T[] array, T target) {
        int count = 0;
        for (T element : array) {
            if (element != null && element.equals(target)) count++;
        }
        return count;
    }

    public static <T> T[] concatenate(T[] first, T[] second) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
