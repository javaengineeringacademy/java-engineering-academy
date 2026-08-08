package collections.solutions;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * SOLUTIONS — All Collections Exercises
 *
 * This file contains complete solutions for all exercise sets.
 */
public class solutions {

    // =========================================================================
    // LIST EXERCISES SOLUTIONS
    // =========================================================================

    public static List<Integer> evenIndexWithSum(List<Integer> input) {
        if (input == null || input.isEmpty()) return new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < input.size(); i += 2) {
            result.add(input.get(i));
            sum += input.get(i);
        }
        result.add(sum);
        return result;
    }

    public static List<String> processDequeCommands(String[] commands) {
        Deque<String> deque = new ArrayDeque<>();
        List<String> results = new ArrayList<>();
        for (String cmd : commands) {
            String[] parts = cmd.split(" ");
            switch (parts[0]) {
                case "push_front": deque.addFirst(parts[1]); break;
                case "push_back": deque.addLast(parts[1]); break;
                case "pop_front":
                    results.add(deque.isEmpty() ? "ERROR" : deque.removeFirst());
                    break;
                case "pop_back":
                    results.add(deque.isEmpty() ? "ERROR" : deque.removeLast());
                    break;
            }
        }
        return results;
    }

    public static List<String> findDuplicateElements(Vector<String> vector) {
        Map<String, Integer> freq = new LinkedHashMap<>();
        for (String s : vector) {
            freq.merge(s, 1, Integer::sum);
        }
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            if (e.getValue() > 1) result.add(e.getKey());
        }
        return result;
    }

    public static boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        return stack.isEmpty();
    }

    public static List<Integer> safeAddWithSnapshot(CopyOnWriteArrayList<Integer> list,
                                                     List<Integer> toAdd) {
        int before = list.size();
        list.addAll(toAdd);
        int after = list.size();
        return Arrays.asList(before, after);
    }

    // =========================================================================
    // SET EXERCISES SOLUTIONS
    // =========================================================================

    public static List<Integer> setDifference(List<Integer> a, List<Integer> b) {
        Set<Integer> bSet = new HashSet<>(b);
        List<Integer> result = new ArrayList<>();
        for (int x : a) {
            if (!bSet.contains(x)) result.add(x);
        }
        return result;
    }

    public static char firstNonRepeatingChar(String s) {
        LinkedHashMap<Character, Integer> freq = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            if (e.getValue() == 1) return e.getKey();
        }
        return '\0';
    }

    public static List<Double> runningMedian(List<Integer> nums) {
        List<Double> medians = new ArrayList<>();
        TreeSet<Integer> lower = new TreeSet<>(Collections.reverseOrder());
        TreeSet<Integer> upper = new TreeSet<>();
        for (int num : nums) {
            if (lower.isEmpty() || num <= lower.first()) {
                lower.add(num);
            } else {
                upper.add(num);
            }
            while (lower.size() > upper.size() + 1) {
                upper.add(lower.pollFirst());
            }
            while (upper.size() > lower.size()) {
                lower.add(upper.pollFirst());
            }
            if (lower.size() > upper.size()) {
                medians.add((double) lower.first());
            } else {
                medians.add((lower.first() + upper.first()) / 2.0);
            }
        }
        return medians;
    }

    public static int longestConsecutiveSequence(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);
        int longest = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) {
                int current = n;
                int streak = 1;
                while (set.contains(current + 1)) {
                    current++;
                    streak++;
                }
                longest = Math.max(longest, streak);
            }
        }
        return longest;
    }

    public static List<Integer> subrangeQuery(TreeSet<Integer> set, int lower, int upper) {
        return new ArrayList<>(set.subSet(lower, true, upper, true));
    }

    // =========================================================================
    // MAP EXERCISES SOLUTIONS
    // =========================================================================

    public static Map<String, Integer> wordFrequency(String sentence) {
        Map<String, Integer> freq = new HashMap<>();
        if (sentence == null || sentence.trim().isEmpty()) return freq;
        for (String word : sentence.toLowerCase().trim().split("\\s+")) {
            if (!word.isEmpty()) {
                freq.merge(word, 1, Integer::sum);
            }
        }
        return freq;
    }

    public static int rangeCount(TreeMap<String, Integer> scores,
                                  String startKey, String endKey) {
        return scores.subMap(startKey, true, endKey, true).size();
    }

    public static Map<String, Integer> concurrentWordCount(List<String> words) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        for (String word : words) {
            map.merge(word.toLowerCase(), 1, Integer::sum);
        }
        return map;
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    // =========================================================================
    // QUEUE EXERCISES SOLUTIONS
    // =========================================================================

    public static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int n : nums) {
            minHeap.offer(n);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    public static int[] slidingWindowMaximum(int[] nums, int k) {
        if (nums == null || nums.length == 0) return new int[0];
        int[] result = new int[nums.length - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    public static List<String> scheduleTasks(String[] tasks, int[] priorities) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        for (int i = 0; i < tasks.length; i++) {
            pq.offer(new int[]{i, priorities[i]});
        }
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] task = pq.poll();
            result.add(tasks[task[0]]);
        }
        return result;
    }

    public static boolean isPalindromeDeque(String s) {
        Deque<Character> deque = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                deque.addLast(Character.toLowerCase(c));
            }
        }
        while (deque.size() > 1) {
            if (!deque.pollFirst().equals(deque.pollLast())) return false;
        }
        return true;
    }

    public static List<Integer> mergeKSortedLists(List<List<Integer>> lists) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (List<Integer> list : lists) {
            pq.addAll(list);
        }
        List<Integer> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        return result;
    }

    // =========================================================================
    // ITERATOR EXERCISES SOLUTIONS
    // =========================================================================

    public static void filterAndDouble(List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int val = it.next();
            if (val < 0) {
                it.remove();
            }
        }
        ListIterator<Integer> lit = list.listIterator();
        while (lit.hasNext()) {
            lit.set(lit.next() * 2);
        }
    }

    public static void reverseList(List<String> list) {
        ListIterator<String> left = list.listIterator();
        ListIterator<String> right = list.listIterator(list.size());
        while (left.nextIndex() < right.previousIndex()) {
            String l = left.next();
            String r = right.previous();
            left.set(r);
            right.set(l);
        }
    }

    public static boolean findAndRemove(List<String> list, String target) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals(target)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public static List<String> interleave(List<String> a, List<String> b) {
        List<String> result = new ArrayList<>();
        ListIterator<String> ia = a.listIterator();
        ListIterator<String> ib = b.listIterator();
        while (ia.hasNext() || ib.hasNext()) {
            if (ia.hasNext()) result.add(ia.next());
            if (ib.hasNext()) result.add(ib.next());
        }
        return result;
    }

    public static void deduplicateConsecutive(List<Integer> list) {
        if (list.isEmpty()) return;
        Iterator<Integer> it = list.iterator();
        Integer prev = it.next();
        while (it.hasNext()) {
            Integer curr = it.next();
            if (curr.equals(prev)) {
                it.remove();
            } else {
                prev = curr;
            }
        }
    }

    // =========================================================================
    // COMPARABLE & COMPARATOR EXERCISES SOLUTIONS
    // =========================================================================

    public static List<Employee> sortEmployees(List<Employee> employees) {
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparing(Employee::getDepartment)
                .thenComparing(Employee::getSalary, Comparator.reverseOrder()));
        return sorted;
    }

    public static List<String> sortByLengthThenAlpha(List<String> words) {
        List<String> sorted = new ArrayList<>(words);
        sorted.sort(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()));
        return sorted;
    }

    public static List<String> sortWithNullsLast(List<String> items) {
        List<String> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        return sorted;
    }

    // =========================================================================
    // FAIL-FAST VS FAIL-SAFE EXERCISES SOLUTIONS
    // =========================================================================

    public static List<Integer> removeMultiplesOfThree(List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() % 3 == 0) {
                it.remove();
            }
        }
        return list;
    }

    public static List<Integer> copyOnWriteIterationDemo(CopyOnWriteArrayList<Integer> list) {
        Iterator<Integer> it = list.iterator();
        list.add(6); // Does not affect the iterator's snapshot
        List<Integer> snapshot = new ArrayList<>();
        it.forEachRemaining(snapshot::add);
        return snapshot;
    }

    public static Map<String, Integer> removeBelowThreshold(Map<String, Integer> map,
                                                            int threshold) {
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() < threshold) {
                it.remove();
            }
        }
        return map;
    }

    public static List<String> safeMapIteration(ConcurrentHashMap<String, Integer> map,
                                                 String newKey, int newValue) {
        map.put(newKey, newValue);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.add(entry.getKey() + "=" + entry.getValue());
        }
        return result;
    }

    public static List<Integer> arrayListVsCopyOnWrite(ArrayList<Integer> original,
                                                        int elementToAdd) {
        CopyOnWriteArrayList<Integer> copy = new CopyOnWriteArrayList<>(original);
        original.add(elementToAdd);
        return Arrays.asList(original.size(), copy.size());
    }

    // =========================================================================
    // COLLECTION ALGORITHMS EXERCISES SOLUTIONS
    // =========================================================================

    public static List<String> customSort(List<String> words) {
        List<String> sorted = new ArrayList<>(words);
        sorted.sort(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.reverseOrder()));
        return sorted;
    }

    public static int findInsertionPoint(List<Integer> sortedList, int target) {
        int idx = Collections.binarySearch(sortedList, target);
        if (idx < 0) return -idx - 1;
        return idx;
    }

    public static List<Integer> deterministicShuffle(List<Integer> list, long seed) {
        List<Integer> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled, new Random(seed));
        Set<Integer> original = new HashSet<>(list);
        Set<Integer> shuffledSet = new HashSet<>(shuffled);
        if (!original.equals(shuffledSet)) {
            throw new IllegalStateException("Elements changed after shuffle");
        }
        return shuffled;
    }

    public static List<Integer> rotateAndReverse(List<Integer> list, int k) {
        List<Integer> result = new ArrayList<>(list);
        Collections.rotate(result, k);
        Collections.reverse(result.subList(0, result.size() / 2));
        return result;
    }

    public static Map<String, Integer> listAnalysis(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set1 = new HashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);
        int commonCount = 0;
        for (int x : set1) {
            if (set2.contains(x)) commonCount++;
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("frequency", commonCount);
        result.put("disjoint", commonCount == 0 ? 1 : 0);
        result.put("commonElements", commonCount);
        return result;
    }

    // =========================================================================
    // STREAM EXERCISES SOLUTIONS
    // =========================================================================

    public static List<String> findPalindromes(List<String> words) {
        return words.stream()
                .filter(w -> {
                    String rev = new StringBuilder(w).reverse().toString();
                    return w.equalsIgnoreCase(rev);
                })
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<Integer> uniqueWordLengths(List<String> sentences) {
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .map(String::length)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static long factorial(int n) {
        return java.util.stream.Long.rangeClosed(1, n).reduce(1, (a, b) -> a * b);
    }

    public static Map<String, Integer> evenOddSum(List<Integer> numbers) {
        return numbers.stream()
                .collect(Collectors.groupingBy(
                        n -> n % 2 == 0 ? "even" : "odd",
                        Collectors.summingInt(Integer::intValue)
                ));
    }

    public static List<String> topNScored(Map<String, Integer> scores, int n) {
        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.toList());
    }

    // =========================================================================
    // LRU CACHE SOLUTION (Inner class)
    // =========================================================================

    public static class LRUCache {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> map;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > LRUCache.this.capacity;
                }
            };
        }

        public int get(int key) {
            return map.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            map.put(key, value);
        }
    }

    // =========================================================================
    // COMPARABLE CLASSES SOLUTIONS
    // =========================================================================

    public static class Student implements Comparable<Student> {
        private String name;
        private double gpa;

        public Student(String name, double gpa) {
            this.name = name;
            this.gpa = gpa;
        }

        public String getName() { return name; }
        public double getGpa() { return gpa; }

        @Override
        public int compareTo(Student other) {
            int cmp = Double.compare(other.gpa, this.gpa);
            return cmp != 0 ? cmp : this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "(" + gpa + ")";
        }
    }

    public static class Employee {
        private String name;
        private String department;
        private double salary;

        public Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }

        @Override
        public String toString() {
            return name + "(" + department + ", $" + salary + ")";
        }
    }

    public static class Product implements Comparable<Product> {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public int compareTo(Product other) {
            int cmp = Double.compare(this.price, other.price);
            return cmp != 0 ? cmp : this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "($" + price + ")";
        }
    }

    public static List<Student> rankStudents(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted);
        return sorted;
    }

    public static List<Product> sortProducts(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        Collections.sort(sorted);
        return sorted;
    }
}
