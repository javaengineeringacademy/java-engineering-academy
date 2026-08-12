import java.util.ArrayList;
import java.util.List;

public class BoundedTypeDemo {

    public static void main(String[] args) {
        upperBoundedDemo();
        lowerBoundedDemo();
        multipleBoundsDemo();
    }

    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T element : list) {
            total += element.doubleValue();
        }
        return total;
    }

    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    public static <T extends Number & Comparable<T>> T findMaxValue(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> List<T> filterGreaterThan(List<T> list, T threshold) {
        List<T> result = new ArrayList<>();
        for (T element : list) {
            if (element.compareTo(threshold) > 0) {
                result.add(element);
            }
        }
        return result;
    }

    public static void upperBoundedDemo() {
        System.out.println("=== Upper Bounded Demo ===");
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        List<Double> doubles = List.of(1.5, 2.5, 3.5);

        System.out.println("Integer sum: " + sum(integers));
        System.out.println("Double sum: " + sum(doubles));
    }

    public static void lowerBoundedDemo() {
        System.out.println("\n=== Lower Bounded Demo ===");
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        System.out.println("Max: " + findMax(numbers));
    }

    public static void multipleBoundsDemo() {
        System.out.println("\n=== Multiple Bounds Demo ===");
        List<Double> values = List.of(2.5, 8.3, 4.1, 9.7);
        System.out.println("Max value: " + findMaxValue(values));
    }
}
