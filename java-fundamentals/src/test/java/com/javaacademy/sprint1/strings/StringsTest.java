package com.javaacademy.sprint1.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringBasicsTest {

    @Test
    void testStringPool() {
        String a = "hello";
        String b = "hello";
        String c = new String("hello");

        assertTrue(a == b);
        assertFalse(a == c);
        assertTrue(a.equals(c));
    }

    @Test
    void testIntern() {
        String s1 = "Java";
        String s2 = new String("Java");
        String s3 = s2.intern();
        assertTrue(s1 == s3);
    }

    @Test
    void testLengthAndIndex() {
        String s = "Hello";
        assertEquals(5, s.length());
        assertEquals('H', s.charAt(0));
        assertEquals('o', s.charAt(4));
    }

    @Test
    void testSubstring() {
        String s = "JavaEngineering";
        assertEquals("Engineering", s.substring(4));
        assertEquals("Java", s.substring(0, 4));
    }

    @Test
    void testSearching() {
        String s = "Hello World Hello";
        assertEquals(0, s.indexOf("Hello"));
        assertEquals(12, s.lastIndexOf("Hello"));
        assertEquals(-1, s.indexOf("Java"));
        assertTrue(s.contains("World"));
        assertTrue(s.startsWith("Hello"));
        assertTrue(s.endsWith("Hello"));
    }

    @Test
    void testComparison() {
        assertTrue("apple".equals("apple"));
        assertFalse("apple".equals("banana"));
        assertTrue("apple".equalsIgnoreCase("APPLE"));
        assertTrue("apple".compareTo("banana") < 0);
        assertEquals(0, "apple".compareTo("apple"));
    }

    @Test
    void testTransformation() {
        String s = "  Hello World  ";
        assertEquals("Hello World", s.trim());
        assertEquals("HELLO WORLD", s.trim().toUpperCase());
        assertEquals("hello world", s.trim().toLowerCase());
        assertEquals("HeLLo WorLd", s.trim().replace('l', 'L'));
        assertEquals("Hello-World", s.trim().replaceAll("\\s+", "-"));
    }

    @Test
    void testSplit() {
        assertArrayEquals(new String[]{"apple","banana","orange"}, "apple,banana,orange".split(","));
        assertArrayEquals(new String[]{"apple","banana","orange"}, "apple  banana   orange".split("\\s+"));
    }

    @Test
    void testEmptyBlank() {
        assertTrue("".isEmpty());
        assertFalse(" ".isEmpty());
        assertTrue(" ".isBlank());
        assertTrue("".isBlank());
        assertFalse("hi".isBlank());
    }
}

class StringBuilderTest {

    @Test
    void testBasic() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello").append(" ").append("World");
        assertEquals("Hello World", sb.toString());
    }

    @Test
    void testOperations() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.reverse();
        assertEquals("dlroW olleH", sb.toString());
        
        sb = new StringBuilder("Hello World");
        sb.delete(5, 6);
        assertEquals("HelloWorld", sb.toString());
        
        sb = new StringBuilder("Hello World");
        sb.insert(5, " ");
        assertEquals("Hello World", sb.toString());
        
        sb = new StringBuilder("Java");
        sb.replace(0, 4, "Python");
        assertEquals("Python", sb.toString());
    }

    @Test
    void testCapacity() {
        StringBuilder sb = new StringBuilder();
        assertEquals(16, sb.capacity());
        
        sb.append("a".repeat(20));
        assertEquals(34, sb.capacity());
        
        sb = new StringBuilder(100);
        assertEquals(100, sb.capacity());
    }

    @Test
    void testPerformance() {
        int iterations = 10000;
        
        long start = System.nanoTime();
        String str = "";
        for (int i = 0; i < iterations; i++) str += i;
        long stringTime = System.nanoTime() - start;
        
        start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) sb.append(i);
        long builderTime = System.nanoTime() - start;
        
        assertTrue(builderTime < stringTime / 10);
    }
}

class StringFormattingTest {

    @Test
    void testBasicFormat() {
        String formatted = String.format("Name: %s, Age: %d", "Alice", 30);
        assertEquals("Name: Alice, Age: 30", formatted);
    }

    @Test
    void testPrecision() {
        assertEquals("3.14", String.format("%.2f", Math.PI));
        assertEquals("3.1416", String.format("%.4f", Math.PI));
    }

    @Test
    void testWidthAlignment() {
        assertEquals("|     hello|", String.format("|%10s|", "hello"));
        assertEquals("|hello     |", String.format("|%-10s|", "hello"));
    }

    @Test
    void testZeroPadding() {
        assertEquals("00042", String.format("%05d", 42));
    }

    @Test
    void testGrouping() {
        assertEquals("1,000,000", String.format("%,d", 1_000_000));
    }

    @Test
    void testDateTime() {
        java.time.LocalDate date = java.time.LocalDate.of(2024, 1, 15);
        assertEquals("2024-01-15", String.format("%tF", date));
    }

    @Test
    void testArgumentIndex() {
        assertEquals("Hello World Hello", String.format("%1$s %2$s %1$s", "Hello", "World"));
    }

    @Test
    void testTextBlocks() {
        String html = """
            <html>
                <body>Hello</body>
            </html>
            """;
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    void testFormatted() {
        String template = "User: %s, Score: %d".formatted("Bob", 95);
        assertEquals("User: Bob, Score: 95", template);
    }
}

class StringAlgorithmsTest {

    @Test
    void testPalindrome() {
        assertTrue(isPalindrome("racecar"));
        assertTrue(isPalindrome("A man a plan a canal Panama"));
        assertFalse(isPalindrome("hello"));
    }

    @Test
    void testAnagram() {
        assertTrue(areAnagrams("listen", "silent"));
        assertFalse(areAnagrams("hello", "world"));
    }

    @Test
    void testLongestCommonPrefix() {
        assertEquals("fl", longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        assertEquals("", longestCommonPrefix(new String[]{"dog", "racecar", "car"}));
    }

    @Test
    void testLongestCommonSubstring() {
        assertEquals("abc", longestCommonSubstring("abcdefg", "xyzabc"));
    }

    @Test
    void testCompression() {
        assertEquals("a3b2c1", compress("aaabbc"));
        assertEquals("abcd", compress("abcd"));
        assertEquals("a10", compress("aaaaaaaaaa"));
    }

    @Test
    void testReverseWords() {
        assertEquals("Java World Hello", reverseWords("Hello World Java"));
    }

    @Test
    void testKmpSearch() {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        assertEquals(10, kmpSearch(text, pattern));
    }

    static boolean isPalindrome(String s) {
        if (s == null) return false;
        int left = 0, right = s.length() - 1;
        while (left < right) {
            char l = Character.toLowerCase(s.charAt(left));
            char r = Character.toLowerCase(s.charAt(right));
            if (!Character.isLetterOrDigit(l)) { left++; continue; }
            if (!Character.isLetterOrDigit(r)) { right--; continue; }
            if (l != r) return false;
            left++; right--;
        }
        return true;
    }

    static boolean areAnagrams(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) return false;
        int[] count = new int[26];
        for (int i = 0; i < a.length(); i++) {
            count[Character.toLowerCase(a.charAt(i)) - 'a']++;
            count[Character.toLowerCase(b.charAt(i)) - 'a']--;
        }
        for (int c : count) if (c != 0) return false;
        return true;
    }

    static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
    }

    static String longestCommonSubstring(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        int maxLen = 0, endIndex = 0;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLen) { maxLen = dp[i][j]; endIndex = i; }
                }
            }
        }
        return a.substring(endIndex - maxLen, endIndex);
    }

    static String compress(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        char current = s.charAt(0); int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == current) count++;
            else { sb.append(current).append(count); current = s.charAt(i); count = 1; }
        }
        sb.append(current).append(count);
        return sb.length() < s.length() ? sb.toString() : s;
    }

    static String reverseWords(String s) {
        if (s == null) return null;
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(' ');
        }
        return sb.toString();
    }

    static int kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = computeLPS(pattern);
        int i = 0, j = 0;
while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) { i++; j++; if (j == pattern.length()) return i - j; }
            else { if (j != 0) j = lps[j - 1]; else i++; }
        }
        return -1;
    }

    static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0, i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) lps[i++] = ++len;
            else { if (len != 0) len = lps[len - 1]; else lps[i++] = 0; }
        }
        return lps;
    }
}