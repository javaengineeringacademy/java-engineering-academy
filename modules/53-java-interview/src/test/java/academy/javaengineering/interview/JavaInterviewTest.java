package academy.javaengineering.interview;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Test cases for Java Interview Examples
 */
class JavaInterviewTest {

    // ============================================
    // CORE JAVA TESTS
    // ============================================
    
    @Test
    @DisplayName("Test anagram detection")
    void testAnagrams() {
        assertTrue(JavaInterviewExample.areAnagrams("listen", "silent"));
        assertTrue(JavaInterviewExample.areAnagrams("triangle", "integral"));
        assertFalse(JavaInterviewExample.areAnagrams("hello", "world"));
        assertFalse(JavaInterviewExample.areAnagrams("aab", "bba"));
    }
    
    @Test
    @DisplayName("Test first non-repeating character")
    void testFirstNonRepeating() {
        assertEquals('l', JavaInterviewExample.firstNonRepeating("leetcode"));
        assertEquals('_', JavaInterviewExample.firstNonRepeating("aabbcc"));
        assertEquals('d', JavaInterviewExample.firstNonRepeating("aabcc"));
    }
    
    @Test
    @DisplayName("Test string reversal")
    void testReverseString() {
        assertEquals("olleh", JavaInterviewExample.reverseString("hello"));
        assertEquals("a", JavaInterviewExample.reverseString("a"));
        assertEquals("", JavaInterviewExample.reverseString(""));
    }
    
    @Test
    @DisplayName("Test palindrome check")
    void testPalindrome() {
        assertTrue(JavaInterviewExample.isPalindrome("racecar"));
        assertTrue(JavaInterviewExample.isPalindrome("madam"));
        assertFalse(JavaInterviewExample.isPalindrome("hello"));
        assertTrue(JavaInterviewExample.isPalindrome(""));
    }

    // ============================================
    // OOP TESTS
    // ============================================
    
    @Test
    @DisplayName("Test Singleton pattern")
    void testSingleton() {
        JavaInterviewExample.Singleton s1 = JavaInterviewExample.Singleton.getInstance();
        JavaInterviewExample.Singleton s2 = JavaInterviewExample.Singleton.getInstance();
        assertSame(s1, s2);
    }
    
    @Test
    @DisplayName("Test Immutable Money class")
    void testImmutableMoney() {
        JavaInterviewExample.Money m1 = new JavaInterviewExample.Money(
            new java.math.BigDecimal("10.50"), java.util.Currency.getInstance("USD"));
        JavaInterviewExample.Money m2 = new JavaInterviewExample.Money(
            new java.math.BigDecimal("5.25"), java.util.Currency.getInstance("USD"));
        
        JavaInterviewExample.Money sum = m1.add(m2);
        assertEquals(new java.math.BigDecimal("15.75"), sum.getAmount());
        
        // Verify immutability - original unchanged
        assertEquals(new java.math.BigDecimal("10.50"), m1.getAmount());
    }
    
    @Test
    @DisplayName("Test Builder pattern")
    void testBuilder() {
        JavaInterviewExample.User user = new JavaInterviewExample.User.Builder()
            .name("John")
            .email("john@example.com")
            .age(30)
            .build();
        
        assertNotNull(user);
        assertEquals("User{name='John', email='john@example.com', age=30}", user.toString());
    }

    // ============================================
    // COLLECTIONS TESTS
    // ============================================
    
    @Test
    @DisplayName("Test LRU Cache")
    void testLRUCache() {
        JavaInterviewExample.LRUCache<String, Integer> cache = 
            new JavaInterviewExample.LRUCache<>(2);
        
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(1, cache.get("a"));
        cache.put("c", 3); // Should evict "b"
        
        assertNull(cache.get("b"));
        assertEquals(1, cache.get("a"));
        assertEquals(3, cache.get("c"));
    }
    
    @Test
    @DisplayName("Test find duplicates")
    void testFindDuplicates() {
        List<Integer> duplicates = JavaInterviewExample.findDuplicates(
            new int[]{1, 2, 3, 2, 1, 4});
        assertEquals(2, duplicates.size());
        assertTrue(duplicates.contains(1));
        assertTrue(duplicates.contains(2));
    }
    
    @Test
    @DisplayName("Test merge sorted lists")
    void testMergeSortedLists() {
        List<Integer> l1 = Arrays.asList(1, 3, 5);
        List<Integer> l2 = Arrays.asList(2, 4, 6);
        
        List<Integer> merged = JavaInterviewExample.mergeSortedLists(l1, l2);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), merged);
    }
    
    @Test
    @DisplayName("Test group anagrams")
    void testGroupAnagrams() {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> groups = JavaInterviewExample.groupAnagrams(strs);
        
        assertEquals(3, groups.size());
    }

    // ============================================
    // MULTITHREADING TESTS
    // ============================================
    
    @Test
    @DisplayName("Test thread-safe counter")
    void testThreadSafeCounter() throws InterruptedException {
        JavaInterviewExample.CounterExamples counter = new JavaInterviewExample.CounterExamples();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.incrementAtomic();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.incrementAtomic();
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        assertEquals(2000, counter.getCountAtomic());
    }
    
    @Test
    @DisplayName("Test CompletableFuture")
    void testCompletableFuture() throws Exception {
        String result = JavaInterviewExample.fetchDataAsync().get(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals("Data from service", result);
    }
    
    @Test
    @DisplayName("Test combined CompletableFutures")
    void testCombinedFutures() throws Exception {
        String result = JavaInterviewExample.combineResults().get(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals("User123 - Order456", result);
    }

    // ============================================
    // JVM TESTS
    // ============================================
    
    @Test
    @DisplayName("Test memory demo")
    void testMemoryDemo() {
        // Should not throw exception
        assertDoesNotThrow(() -> JavaInterviewExample.memoryDemo());
    }
    
    @Test
    @DisplayName("Test soft reference cache")
    void testSoftReferenceCache() {
        JavaInterviewExample.Cache<String, String> cache = new JavaInterviewExample.Cache<>();
        cache.put("key1", "value1");
        
        assertEquals("value1", cache.get("key1"));
        assertNull(cache.get("nonexistent"));
    }
}
