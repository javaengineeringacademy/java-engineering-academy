package academy.javaengineering.interview;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

/**
 * Java Interview Examples - Common patterns and solutions
 */
public class JavaInterviewExample {

    // ============================================
    // CORE JAVA INTERVIEW QUESTIONS
    // ============================================
    
    /**
     * Check if two strings are anagrams
     * Time: O(n), Space: O(1) with fixed alphabet
     */
    public static boolean areAnagrams(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        
        int[] charCount = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            charCount[s1.charAt(i) - 'a']++;
            charCount[s2.charAt(i) - 'a']--;
        }
        
        for (int count : charCount) {
            if (count != 0) return false;
        }
        return true;
    }
    
    /**
     * Find first non-repeating character
     * Time: O(n), Space: O(1)
     */
    public static char firstNonRepeating(String str) {
        Map<Character, Integer> charCount = new LinkedHashMap<>();
        for (char c : str.toCharArray()) {
            charCount.merge(c, 1, Integer::sum);
        }
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() == 1) return entry.getKey();
        }
        return '_';
    }
    
    /**
     * Reverse a string without using StringBuilder
     */
    public static String reverseString(String str) {
        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }
    
    /**
     * Check if string is palindrome
     */
    public static boolean isPalindrome(String str) {
        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    // ============================================
    // OOP INTERVIEW QUESTIONS
    // ============================================
    
    /**
     * Singleton pattern - Double checked locking
     */
    public static class Singleton implements Serializable {
        private static volatile Singleton instance;
        private int value;
        
        private Singleton() {
            // Prevent reflection attack
            if (instance != null) {
                throw new RuntimeException("Use getInstance() method");
            }
        }
        
        public static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }
        
        // Prevent deserialization from creating new instance
        protected Object readResolve() {
            return getInstance();
        }
    }
    
    /**
     * Immutable class example
     */
    public static final class Money {
        private final BigDecimal amount;
        private final Currency currency;
        
        public Money(BigDecimal amount, Currency currency) {
            this.amount = Objects.requireNonNull(amount);
            this.currency = Objects.requireNonNull(currency);
        }
        
        public BigDecimal getAmount() { return amount; }
        public Currency getCurrency() { return currency; }
        
        public Money add(Money other) {
            if (!this.currency.equals(other.currency)) {
                throw new IllegalArgumentException("Different currencies");
            }
            return new Money(this.amount.add(other.amount), this.currency);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Money)) return false;
            Money money = (Money) o;
            return amount.equals(money.amount) && currency.equals(money.currency);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(amount, currency);
        }
        
        @Override
        public String toString() {
            return currency.getSymbol() + amount.toString();
        }
    }
    
    /**
     * Builder pattern example
     */
    public static class User {
        private final String name;
        private final String email;
        private final int age;
        private final String phone;
        
        private User(Builder builder) {
            this.name = builder.name;
            this.email = builder.email;
            this.age = builder.age;
            this.phone = builder.phone;
        }
        
        public static class Builder {
            private String name;
            private String email;
            private int age;
            private String phone;
            
            public Builder name(String name) {
                this.name = name;
                return this;
            }
            
            public Builder email(String email) {
                this.email = email;
                return this;
            }
            
            public Builder age(int age) {
                this.age = age;
                return this;
            }
            
            public Builder phone(String phone) {
                this.phone = phone;
                return this;
            }
            
            public User build() {
                return new User(this);
            }
        }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', email='" + email + "', age=" + age + "}";
        }
    }

    // ============================================
    // COLLECTIONS INTERVIEW QUESTIONS
    // ============================================
    
    /**
     * LRU Cache implementation using LinkedHashMap
     */
    public static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        
        public LRUCache(int capacity) {
            super(capacity, 0.75f, true); // accessOrder = true
            this.capacity = capacity;
        }
        
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }
    
    /**
     * Find all duplicates in array
     */
    public static List<Integer> findDuplicates(int[] nums) {
        List<Integer> duplicates = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            if (!seen.add(num)) {
                duplicates.add(num);
            }
        }
        return duplicates;
    }
    
    /**
     * Merge two sorted lists
     */
    public static List<Integer> mergeSortedLists(List<Integer> l1, List<Integer> l2) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < l1.size() && j < l2.size()) {
            if (l1.get(i) <= l2.get(j)) {
                result.add(l1.get(i++));
            } else {
                result.add(l2.get(j++));
            }
        }
        
        while (i < l1.size()) result.add(l1.get(i++));
        while (j < l2.size()) result.add(l2.get(j++));
        
        return result;
    }
    
    /**
     * Group anagrams together
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            map.computeIfAbsent(sorted, k -> new ArrayList<>()).add(str);
        }
        
        return new ArrayList<>(map.values());
    }

    // ============================================
    // MULTITHREADING INTERVIEW QUESTIONS
    // ============================================
    
    /**
     * Producer-Consumer using BlockingQueue
     */
    public static class ProducerConsumer {
        private final BlockingQueue<String> queue;
        private volatile boolean running = true;
        
        public ProducerConsumer(int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
        }
        
        public void produce() throws InterruptedException {
            int item = 0;
            while (running) {
                queue.put("Item " + item++);
                System.out.println("Produced: Item " + (item - 1));
                Thread.sleep(100);
            }
        }
        
        public void consume() throws InterruptedException {
            while (running) {
                String item = queue.take();
                System.out.println("Consumed: " + item);
                Thread.sleep(150);
            }
        }
        
        public void shutdown() {
            running = false;
        }
    }
    
    /**
     * Thread-safe counter using different approaches
     */
    public static class CounterExamples {
        // Approach 1: synchronized
        private int count1 = 0;
        public synchronized void incrementSync() { count1++; }
        public synchronized int getCountSync() { return count1; }
        
        // Approach 2: ReentrantLock
        private int count2 = 0;
        private final ReentrantLock lock = new ReentrantLock();
        public void incrementLock() {
            lock.lock();
            try { count2++; } 
            finally { lock.unlock(); }
        }
        
        // Approach 3: AtomicInteger
        private AtomicInteger count3 = new AtomicInteger(0);
        public void incrementAtomic() { count3.incrementAndGet(); }
        public int getCountAtomic() { return count3.get(); }
    }
    
    /**
     * ReadWriteLock example
     */
    public static class SharedResource {
        private final Map<String, String> cache = new HashMap<>();
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        
        public String read(String key) {
            rwLock.readLock().lock();
            try {
                return cache.get(key);
            } finally {
                rwLock.readLock().unlock();
            }
        }
        
        public void write(String key, String value) {
            rwLock.writeLock().lock();
            try {
                cache.put(key, value);
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }
    
    /**
     * CompletableFuture example
     */
    public static CompletableFuture<String> fetchDataAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(1000); } 
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Data from service";
        });
    }
    
    public static CompletableFuture<String> combineResults() {
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> "User123");
        CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> "Order456");
        
        return userFuture.thenCombine(orderFuture, 
            (user, order) -> user + " - " + order);
    }

    // ============================================
    // JVM INTERVIEW QUESTIONS
    // ============================================
    
    /**
     * Demonstrate memory usage
     */
    public static void memoryDemo() {
        Runtime runtime = Runtime.getRuntime();
        
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("Total Memory: " + totalMemory / 1024 + " KB");
        System.out.println("Free Memory: " + freeMemory / 1024 + " KB");
        System.out.println("Used Memory: " + usedMemory / 1024 + " KB");
    }
    
    /**
     * Soft reference example for cache
     */
    public static class Cache<K, V> {
        private final Map<K, SoftReference<V>> cache = new HashMap<>();
        
        public void put(K key, V value) {
            cache.put(key, new SoftReference<>(value));
        }
        
        public V get(K key) {
            SoftReference<V> ref = cache.get(key);
            return ref != null ? ref.get() : null;
        }
    }

    // ============================================
    // MAIN METHOD
    // ============================================
    
    public static void main(String[] args) {
        // Core Java examples
        System.out.println("=== Core Java ===");
        System.out.println("Are 'listen' and 'silent' anagrams? " + areAnagrams("listen", "silent"));
        System.out.println("First non-repeating in 'aabcc': " + firstNonRepeating("aabcc"));
        System.out.println("Reversed 'hello': " + reverseString("hello"));
        System.out.println("Is 'racecar' palindrome? " + isPalindrome("racecar"));
        
        // OOP examples
        System.out.println("\n=== OOP ===");
        User user = new User.Builder()
            .name("John")
            .email("john@example.com")
            .age(30)
            .build();
        System.out.println("User: " + user);
        
        // Collections examples
        System.out.println("\n=== Collections ===");
        LRUCache<String, Integer> lruCache = new LRUCache<>(3);
        lruCache.put("a", 1);
        lruCache.put("b", 2);
        lruCache.put("c", 3);
        lruCache.get("a"); // Access 'a', moves to end
        lruCache.put("d", 4); // Evicts 'b'
        System.out.println("LRU Cache after operations: " + lruCache);
        
        System.out.println("Duplicates in [1,2,3,2,1]: " + findDuplicates(new int[]{1, 2, 3, 2, 1}));
        System.out.println("Grouped anagrams: " + groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        
        // Memory demo
        System.out.println("\n=== JVM Memory ===");
        memoryDemo();
    }
}
