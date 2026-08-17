package academy.javaengineering.exercises.solutions;

import java.util.Arrays;

/**
 * Solutions: Methods (Parameters, Return Values, Overloading)
 */
public class MethodSolutions {

    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public String add(String a, String b) { return a + " " + b; }

    public String reverseString(String str) {
        if (str == null || str.isEmpty()) return str;
        return reverseString(str.substring(1)) + str.charAt(0);
    }

    public long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public String join(String separator, String... values) {
        if (values == null || values.length == 0) return "";
        StringBuilder sb = new StringBuilder(values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append(separator).append(values[i]);
        }
        return sb.toString();
    }

    @FunctionalInterface
    interface IntTransformer { int transform(int value); }

    public int[] transformArray(int[] input, IntTransformer transformer) {
        int[] result = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = transformer.transform(input[i]);
        }
        return result;
    }

    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        MethodSolutions solutions = new MethodSolutions();
        System.out.println("=== Method Solutions ===\n");

        System.out.println("1. add(5, 3) = " + solutions.add(5, 3));
        System.out.println("   add(2.5, 3.5) = " + solutions.add(2.5, 3.5));
        System.out.println("   add(1, 2, 3) = " + solutions.add(1, 2, 3));
        System.out.println("   add('Hello', 'World') = " + solutions.add("Hello", "World"));

        System.out.println("\n2. reverseString('hello') = " + solutions.reverseString("hello"));
        System.out.println("3. factorial(5) = " + solutions.factorial(5));
        System.out.println("4. join(', ', 'a', 'b', 'c') = " + solutions.join(", ", "a", "b", "c"));

        int[] doubled = solutions.transformArray(new int[]{1, 2, 3}, x -> x * 2);
        System.out.println("5. transformArray [1,2,3] doubled = " + Arrays.toString(doubled));

        int[] sorted = MethodSolutions.bubbleSort(new int[]{5, 3, 8, 1, 2});
        System.out.println("6. bubbleSort [5,3,8,1,2] = " + Arrays.toString(sorted));
    }
}
