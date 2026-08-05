package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Strategy interface specifically for sorting operations.
 * Extends the base Strategy interface with sorting capabilities.
 */
public interface SortStrategy extends Strategy {

    /**
     * Sort an array of integers.
     *
     * @param array the array to sort
     * @return the sorted array
     */
    int[] sort(int[] array);
}
