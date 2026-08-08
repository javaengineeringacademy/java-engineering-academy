package list.vector.solutions;

import java.util.*;

public class VectorSolutions {

    public static <T> T mostFrequent(Vector<T> vector) {
        Map<T, Integer> countMap = new HashMap<>();
        for (T item : vector) {
            countMap.merge(item, 1, Integer::sum);
        }
        T mostFrequent = null;
        int maxCount = 0;
        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        return mostFrequent;
    }

    public static Vector<Integer> filterPrimes(Vector<Integer> vector) {
        Vector<Integer> primes = new Vector<>();
        for (Integer num : vector) {
            if (isPrime(num)) {
                primes.add(num);
            }
        }
        return primes;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static <T> void rotateLeft(Vector<T> vector, int k) {
        if (vector.isEmpty()) return;
        k = k % vector.size();
        if (k < 0) k += vector.size();
        List<T> rotated = new ArrayList<>(vector.subList(k, vector.size()));
        rotated.addAll(vector.subList(0, k));
        vector.clear();
        vector.addAll(rotated);
    }

    public static <T> void removeNulls(Vector<T> vector) {
        vector.removeAll(Collections.singleton(null));
    }

    public static <T> Map<T, Integer> frequencyMap(Vector<T> vector) {
        Map<T, Integer> map = new LinkedHashMap<>();
        for (T item : vector) {
            map.merge(item, 1, Integer::sum);
        }
        return map;
    }

    public static void main(String[] args) {
        Vector<Integer> nums = new Vector<>(Arrays.asList(1, 2, 3, 2, 4, 2, 5));
        System.out.println("Most frequent: " + mostFrequent(nums));

        Vector<Integer> values = new Vector<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Primes: " + filterPrimes(values));

        Vector<String> rotate = new Vector<>(Arrays.asList("A", "B", "C", "D", "E"));
        rotateLeft(rotate, 2);
        System.out.println("Rotated left 2: " + rotate);

        Vector<String> nulls = new Vector<>(Arrays.asList("A", null, "B", null, "C"));
        removeNulls(nulls);
        System.out.println("After remove nulls: " + nulls);

        System.out.println("Frequency: " + frequencyMap(new Vector<>(Arrays.asList("a", "b", "a", "c", "a"))));
    }
}
