import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Complete Solutions for ALL 40 Iteration Exercises
 */
public class solutions {

    // ============================================================================
    // ForLoop Exercises (exercises/ForLoopExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Sum of Even Numbers
     */
    public static int sumEvens(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] % 2 == 0) {
                sum += array[i];
            }
        }
        return sum;
    }

    /**
     * Exercise 2: Find Maximum
     */
    public static int findMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Exercise 3: Reverse Array
     */
    public static void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Exercise 4: Count Occurrences
     */
    public static int countOccurrences(int[] array, int target) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                count++;
            }
        }
        return count;
    }

    /**
     * Exercise 5: Fibonacci Series
     */
    public static int[] fibonacci(int n) {
        if (n <= 0) return new int[0];
        if (n == 1) return new int[]{0};

        int[] fib = new int[n];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i < n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib;
    }

    // ============================================================================
    // EnhancedFor Exercises (exercises/EnhancedForExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Find Maximum
     */
    public static int findMaxEnhanced(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * Exercise 2: Count Matching Condition
     */
    public static int countGreaterThan(int[] array, int threshold) {
        int count = 0;
        for (int num : array) {
            if (num > threshold) {
                count++;
            }
        }
        return count;
    }

    /**
     * Exercise 3: Build String from Collection
     */
    public static String buildString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : list) {
            if (!first) {
                sb.append(" -> ");
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    /**
     * Exercise 4: Find Intersection
     */
    public static List<Integer> intersection(int[] array1, int[] array2) {
        Set<Integer> set = new HashSet<>();
        for (int num : array2) {
            set.add(num);
        }

        List<Integer> result = new ArrayList<>();
        Set<Integer> added = new HashSet<>();
        for (int num : array1) {
            if (set.contains(num) && !added.contains(num)) {
                result.add(num);
                added.add(num);
            }
        }
        return result;
    }

    /**
     * Exercise 5: Group by First Letter
     */
    public static Map<Character, List<String>> groupByFirstLetter(List<String> list) {
        Map<Character, List<String>> map = new HashMap<>();
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                char firstChar = s.charAt(0);
                map.computeIfAbsent(firstChar, k -> new ArrayList<>()).add(s);
            }
        }
        return map;
    }

    // ============================================================================
    // WhileLoop Exercises (exercises/WhileLoopExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Read Until Sentinel
     */
    public static int sumUntilSentinel(int[] array, int sentinel) {
        int sum = 0;
        int i = 0;
        while (i < array.length && array[i] != sentinel) {
            sum += array[i];
            i++;
        }
        return sum;
    }

    /**
     * Exercise 2: Binary Search
     */
    public static int binarySearch(int[] sortedArray, int target) {
        int left = 0;
        int right = sortedArray.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sortedArray[mid] == target) {
                return mid;
            } else if (sortedArray[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Exercise 3: Flatten Nested List
     */
    public static <T> List<T> flatten(List<List<T>> nestedList) {
        List<T> result = new ArrayList<>();
        for (List<T> innerList : nestedList) {
            for (T element : innerList) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Exercise 4: Run-Length Decoding
     */
    public static String decode(String encoded) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < encoded.length()) {
            // Read the number
            int count = 0;
            while (i < encoded.length() && Character.isDigit(encoded.charAt(i))) {
                count = count * 10 + (encoded.charAt(i) - '0');
                i++;
            }
            // If no number found, default to 1
            if (count == 0) count = 1;
            // Read the character
            if (i < encoded.length()) {
                char ch = encoded.charAt(i);
                i++;
                for (int j = 0; j < count; j++) {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * Exercise 5: Pascal's Triangle Row
     */
    public static List<Long> pascalRow(int n) {
        List<Long> row = new ArrayList<>();
        row.add(1L);

        for (int i = 1; i <= n; i++) {
            long prev = row.get(i - 1);
            long val = prev * (n - i + 1) / i;
            row.add(val);
        }
        return row;
    }

    // ============================================================================
    // Iterator Exercises (exercises/IteratorExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Remove Elements Matching Predicate
     */
    public static <T> void removeIf(List<T> list, Predicate<T> predicate) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (predicate.test(element)) {
                iterator.remove();
            }
        }
    }

    /**
     * Exercise 2: Find Elements Between Two Values
     */
    public static List<Integer> findBetween(Collection<Integer> collection, int lower, int upper) {
        List<Integer> result = new ArrayList<>();
        Iterator<Integer> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value > lower && value < upper) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * Exercise 3: Collect Every Nth Element
     */
    public static <T> List<T> everyNth(Collection<T> collection, int n) {
        List<T> result = new ArrayList<>();
        Iterator<T> iterator = collection.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            T element = iterator.next();
            if (count % n == 0) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Exercise 4: Detect Cycle in List
     */
    public static <T> boolean hasCycle(List<T> list) {
        Set<T> seen = new HashSet<>();
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (!seen.add(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exercise 5: Merge Two Sorted Iterators
     */
    public static Iterator<Integer> mergeSorted(Iterator<Integer> it1, Iterator<Integer> it2) {
        List<Integer> result = new ArrayList<>();
        Integer val1 = it1.hasNext() ? it1.next() : null;
        Integer val2 = it2.hasNext() ? it2.next() : null;

        while (val1 != null || val2 != null) {
            if (val1 == null) {
                result.add(val2);
                val2 = it2.hasNext() ? it2.next() : null;
            } else if (val2 == null) {
                result.add(val1);
                val1 = it1.hasNext() ? it1.next() : null;
            } else if (val1 <= val2) {
                result.add(val1);
                val1 = it1.hasNext() ? it1.next() : null;
            } else {
                result.add(val2);
                val2 = it2.hasNext() ? it2.next() : null;
            }
        }
        return result.iterator();
    }

    // ============================================================================
    // ListIterator Exercises (exercises/ListIteratorExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Reverse List
     */
    public static <T> void reverse(List<T> list) {
        ListIterator<T> left = list.listIterator();
        ListIterator<T> right = list.listIterator(list.size());

        while (left.nextIndex() < right.previousIndex()) {
            T leftVal = left.next();
            T rightVal = right.previous();

            left.set(rightVal);
            right.set(leftVal);
        }
    }

    /**
     * Exercise 2: Insert at Sorted Position
     */
    public static void insertSorted(List<Integer> list, int element) {
        ListIterator<Integer> iterator = list.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() >= element) {
                iterator.previous();
                iterator.add(element);
                return;
            }
        }
        iterator.add(element);
    }

    /**
     * Exercise 3: Swap Adjacent Pairs
     */
    public static <T> void swapPairs(List<T> list) {
        ListIterator<T> iterator = list.listIterator();
        while (iterator.hasNext() && iterator.nextIndex() < list.size() - 1) {
            T first = iterator.next();
            T second = iterator.next();

            iterator.set(first);
            iterator.previous();
            iterator.set(second);
            iterator.next();
        }
    }

    /**
     * Exercise 4: Interleave Two Lists
     */
    public static <T> List<T> interleave(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        ListIterator<T> it1 = list1.listIterator();
        ListIterator<T> it2 = list2.listIterator();

        while (it1.hasNext() || it2.hasNext()) {
            if (it1.hasNext()) {
                result.add(it1.next());
            }
            if (it2.hasNext()) {
                result.add(it2.next());
            }
        }
        return result;
    }

    /**
     * Exercise 5: Find Middle Element
     */
    public static <T> T findMiddle(List<T> list) {
        if (list.isEmpty()) return null;

        int size = list.size();
        int middleIndex = (size % 2 == 0) ? (size / 2 - 1) : (size / 2);

        ListIterator<T> iterator = list.listIterator(middleIndex);
        return iterator.next();
    }

    // ============================================================================
    // Enumeration Exercises (exercises/EnumerationExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Convert Enumeration to List
     */
    public static <T> List<T> toList(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * Exercise 2: Count Elements
     */
    public static <T> int count(Enumeration<T> enumeration) {
        int count = 0;
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
            count++;
        }
        return count;
    }

    /**
     * Exercise 3: Filter Enumeration by Predicate
     */
    public static <T> Enumeration<T> filter(Enumeration<T> enumeration, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            T element = enumeration.nextElement();
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        return Collections.enumeration(filtered);
    }

    /**
     * Exercise 4: Reverse Enumeration
     */
    public static <T> Enumeration<T> reverse(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        Collections.reverse(list);
        return Collections.enumeration(list);
    }

    /**
     * Exercise 5: Merge Two Enumerations
     */
    public static <T> Enumeration<T> merge(Enumeration<T> enum1, Enumeration<T> enum2) {
        List<T> merged = new ArrayList<>();
        while (enum1.hasMoreElements() || enum2.hasMoreElements()) {
            if (enum1.hasMoreElements()) {
                merged.add(enum1.nextElement());
            }
            if (enum2.hasMoreElements()) {
                merged.add(enum2.nextElement());
            }
        }
        return Collections.enumeration(merged);
    }

    // ============================================================================
    // Spliterator Exercises (exercises/SpliteratorExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Estimate Size
     */
    public static <T> long estimateSize(Spliterator<T> spliterator) {
        long size = spliterator.estimateSize();
        return (size != Spliterator.UNKNOWN) ? size : 0;
    }

    /**
     * Exercise 2: TryAdvance Process Elements
     */
    public static <T> int tryAdvanceProcess(Spliterator<T> spliterator, Consumer<T> consumer) {
        int count = 0;
        while (spliterator.tryAdvance(consumer)) {
            count++;
        }
        return count;
    }

    /**
     * Exercise 3: Split for Parallel Processing
     */
    public static <T> List<T> splitCollection(Collection<T> collection) {
        Spliterator<T> spliterator = collection.spliterator();
        Spliterator<T> split = spliterator.trySplit();

        List<T> result = new ArrayList<>();
        if (split != null) {
            split.forEachRemaining(result::add);
        }
        spliterator.forEachRemaining(result::add);
        return result;
    }

    /**
     * Exercise 4: Count Elements with Characteristics
     */
    public static <T> int countWithPredicate(Spliterator<T> spliterator, Predicate<T> predicate) {
        int count = 0;
        AtomicReference<T> ref = new AtomicReference<>();
        while (spliterator.tryAdvance(ref::set)) {
            if (predicate.test(ref.get())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Exercise 5: Custom Spliterator for Range
     */
    public static Spliterator<Integer> rangeSpliterator(int start, int end) {
        return new Spliterator<Integer>() {
            private int current = start;

            @Override
            public boolean tryAdvance(Consumer<? super Integer> action) {
                if (current < end) {
                    action.accept(current++);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<Integer> trySplit() {
                int mid = (start + end) / 2;
                if (mid <= current) return null;

                int oldEnd = end;
                end = mid;
                return new RangeSpliterator(current, mid);
            }

            @Override
            public long estimateSize() {
                return (long) end - current;
            }

            @Override
            public int characteristics() {
                return ORDERED | SIZED | IMMUTABLE | NONNULL;
            }
        };
    }

    // Helper class for range spliterator
    private static class RangeSpliterator implements Spliterator<Integer> {
        private int current;
        private int end;

        RangeSpliterator(int start, int end) {
            this.current = start;
            this.end = end;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Integer> action) {
            if (current < end) {
                action.accept(current++);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Integer> trySplit() {
            int mid = (current + end) / 2;
            if (mid <= current) return null;
            int oldEnd = end;
            end = mid;
            return new RangeSpliterator(current, mid);
        }

        @Override
        public long estimateSize() {
            return (long) end - current;
        }

        @Override
        public int characteristics() {
            return ORDERED | SIZED | IMMUTABLE | NONNULL;
        }
    }

    // ============================================================================
    // Stream Exercises (exercises/StreamExercises.java)
    // ============================================================================

    /**
     * Exercise 1: Convert For Loop to Stream
     */
    public static List<Integer> transform(int[] array) {
        return Arrays.stream(array)
                .filter(x -> x % 2 == 0)
                .map(x -> x * 2)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Exercise 2: Stream.iterate Sequence
     */
    public static List<Long> powersOfTwo(int n) {
        return Stream.iterate(1L, x -> x * 2)
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Exercise 3: Parallel Word Count
     */
    public static int parallelWordCount(List<String> strings) {
        return strings.parallelStream()
                .mapToInt(s -> s.split("\\s+").length)
                .sum();
    }

    /**
     * Exercise 4: Stream.generate Infinite
     */
    public static List<Integer> randomNumbers(int n, long seed) {
        return Stream.generate(() -> new Random(seed).nextInt(100) + 1)
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Exercise 5: Stream.concat
     */
    public static List<Integer> concatSorted(List<Integer> list1, List<Integer> list2) {
        return Stream.concat(list1.stream(), list2.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // ============================================================================
    // Main method for testing
    // ============================================================================

    public static void main(String[] args) {
        System.out.println("=== Testing Solutions ===\n");

        // ForLoop Tests
        System.out.println("--- ForLoop Exercises ---");
        System.out.println("Sum Evens [1,2,3,4,5,6]: " + sumEvens(new int[]{1, 2, 3, 4, 5, 6}));
        System.out.println("Find Max [3,7,2,8,1]: " + findMax(new int[]{3, 7, 2, 8, 1}));

        int[] arr = {1, 2, 3, 4, 5};
        reverseArray(arr);
        System.out.println("Reverse [1,2,3,4,5]: " + Arrays.toString(arr));

        System.out.println("Count 2 in [1,2,3,2,4,2]: " + countOccurrences(new int[]{1, 2, 3, 2, 4, 2}, 2));
        System.out.println("Fibonacci(5): " + Arrays.toString(fibonacci(5)));

        // EnhancedFor Tests
        System.out.println("\n--- EnhancedFor Exercises ---");
        System.out.println("Find Max Enhanced [3,7,2,8,1]: " + findMaxEnhanced(new int[]{3, 7, 2, 8, 1}));
        System.out.println("Count > 4 in [1,5,3,8,2,7]: " + countGreaterThan(new int[]{1, 5, 3, 8, 2, 7}, 4));
        System.out.println("Build String: " + buildString(Arrays.asList("Java", "Python", "C++")));
        System.out.println("Intersection: " + intersection(new int[]{1, 2, 3, 4}, new int[]{3, 4, 5, 6}));
        System.out.println("Group by First Letter: " + groupByFirstLetter(Arrays.asList("apple", "banana", "avocado", "blueberry")));

        // WhileLoop Tests
        System.out.println("\n--- WhileLoop Exercises ---");
        System.out.println("Sum Until Sentinel: " + sumUntilSentinel(new int[]{1, 2, 3, -1, 4, 5}, -1));
        System.out.println("Binary Search (7): " + binarySearch(new int[]{1, 3, 5, 7, 9, 11}, 7));
        System.out.println("Binary Search (6): " + binarySearch(new int[]{1, 3, 5, 7, 9, 11}, 6));
        System.out.println("Decode '3a2b1c': " + decode("3a2b1c"));
        System.out.println("Pascal Row(4): " + pascalRow(4));

        // Iterator Tests
        System.out.println("\n--- Iterator Exercises ---");
        List<Integer> numList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        removeIf(numList, x -> x % 2 == 0);
        System.out.println("Remove Evens: " + numList);
        System.out.println("Find Between (2,7): " + findBetween(Arrays.asList(1, 5, 3, 8, 2, 7), 2, 7));
        System.out.println("Every 3rd: " + everyNth(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 3));
        System.out.println("Has Cycle [1,2,3,4]: " + hasCycle(Arrays.asList(1, 2, 3, 4)));
        System.out.println("Has Cycle [1,2,3,1]: " + hasCycle(Arrays.asList(1, 2, 3, 1)));

        // ListIterator Tests
        System.out.println("\n--- ListIterator Exercises ---");
        List<Integer> reverseList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        reverse(reverseList);
        System.out.println("Reverse: " + reverseList);

        List<Integer> sortedList = new ArrayList<>(Arrays.asList(1, 3, 5, 7));
        insertSorted(sortedList, 4);
        System.out.println("Insert Sorted: " + sortedList);

        List<Integer> swapList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        swapPairs(swapList);
        System.out.println("Swap Pairs: " + swapList);

        System.out.println("Interleave: " + interleave(Arrays.asList(1, 3, 5), Arrays.asList(2, 4)));
        System.out.println("Find Middle [1,2,3,4,5]: " + findMiddle(Arrays.asList(1, 2, 3, 4, 5)));
        System.out.println("Find Middle [1,2,3,4]: " + findMiddle(Arrays.asList(1, 2, 3, 4)));

        // Enumeration Tests
        System.out.println("\n--- Enumeration Exercises ---");
        Enumeration<Integer> enum1 = Collections.enumeration(Arrays.asList(1, 2, 3));
        System.out.println("To List: " + toList(enum1));
        System.out.println("Count: " + count(Collections.enumeration(Arrays.asList(1, 2, 3, 4, 5))));
        System.out.println("Filter > 2: " + toList(filter(Collections.enumeration(Arrays.asList(1, 2, 3, 4, 5)), x -> x > 2)));
        System.out.println("Reverse: " + toList(reverse(Collections.enumeration(Arrays.asList(1, 2, 3)))));
        System.out.println("Merge: " + toList(merge(Collections.enumeration(Arrays.asList(1, 3, 5)), Collections.enumeration(Arrays.asList(2, 4)))));

        // Spliterator Tests
        System.out.println("\n--- Spliterator Exercises ---");
        List<Integer> splist = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Estimate Size: " + estimateSize(splist.spliterator()));
        System.out.println("TryAdvance Process: " + tryAdvanceProcess(splist.spliterator(), x -> {}));
        System.out.println("Split Collection: " + splitCollection(Arrays.asList(1, 2, 3, 4, 5, 6)));
        System.out.println("Count > 3: " + countWithPredicate(splist.spliterator(), x -> x > 3));

        Spliterator<Integer> rangeSpl = rangeSpliterator(1, 5);
        List<Integer> rangeResult = new ArrayList<>();
        rangeSpl.forEachRemaining(rangeResult::add);
        System.out.println("Range Spliterator (1,5): " + rangeResult);

        // Stream Tests
        System.out.println("\n--- Stream Exercises ---");
        System.out.println("Transform: " + transform(new int[]{1, 2, 3, 4, 5}));
        System.out.println("Powers of Two(5): " + powersOfTwo(5));
        System.out.println("Parallel Word Count: " + parallelWordCount(Arrays.asList("hello world", "foo bar baz")));
        System.out.println("Random Numbers: " + randomNumbers(5, 42));
        System.out.println("Concat Sorted: " + concatSorted(Arrays.asList(1, 3, 5), Arrays.asList(2, 3, 6)));
    }
}
