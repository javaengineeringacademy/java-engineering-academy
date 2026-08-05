package academy.javaengineering.patterns.behavioral.strategy;

import java.util.Arrays;

/**
 * Concrete Strategy implementation - Quick Sort algorithm.
 * Efficient O(n log n) sorting algorithm using divide and conquer.
 */
public class QuickSort implements SortStrategy {

    @Override
    public int[] sort(int[] array) {
        int[] result = Arrays.copyOf(array, array.length);
        quickSort(result, 0, result.length - 1);
        return result;
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    @Override
    public String execute(String data) {
        return "QuickSort applied to: " + data;
    }
}
