package academy.javaengineering.patterns.behavioral.strategy;

import java.util.Arrays;

/**
 * Concrete Strategy implementation - Bubble Sort algorithm.
 * Simple but inefficient O(n^2) sorting algorithm.
 */
public class BubbleSort implements SortStrategy {

    @Override
    public int[] sort(int[] array) {
        int[] result = Arrays.copyOf(array, array.length);
        int n = result.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (result[j] > result[j + 1]) {
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }
        return result;
    }

    @Override
    public String execute(String data) {
        return "BubbleSort applied to: " + data;
    }
}
