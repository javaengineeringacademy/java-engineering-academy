package com.javaacademy.sprint1.strings;

/**
 * StringAlgorithms - Demonstrates common string algorithms and operations.
 *
 * <p><b>Common String Algorithms:</b>
 * <ul>
 *   <li>Palindrome check</li>
 *   <li>Anagram detection</li>
 *   <li>String matching (KMP, Rabin-Karp)</li>
 *   <li>Longest common substring/prefix</li>
 *   <li>String compression</li>
 *   <li>Character frequency</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b> Like text processing tools -
 * search (grep), diff, spell check, compression.
 *
 * <p><b>Best Practice:</b> Use standard library methods when possible.
 * For complex algorithms, consider using libraries like Apache Commons Lang.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class StringAlgorithms {

    private StringAlgorithms() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== String Algorithms ===\n");

        // Palindrome check
        System.out.println("--- Palindrome Check ---");
        String[] palindromes = {"racecar", "level", "A man a plan a canal Panama", "hello"};
        for (String s : palindromes) {
            System.out.printf("'%s' -> %b%n", s, isPalindrome(s));
        }

        // Anagram check
        System.out.println("\n--- Anagram Check ---");
        System.out.println("listen & silent: " + areAnagrams("listen", "silent")); // true
        System.out.println("hello & world: " + areAnagrams("hello", "world"));   // false

        // Character frequency
        System.out.println("\n--- Character Frequency ---");
        String text = "hello world";
        printFrequency(text);

        // Longest Common Prefix
        System.out.println("\n--- Longest Common Prefix ---");
        String[] words1 = {"flower", "flow", "flight"};
        String[] words2 = {"dog", "racecar", "car"};
        System.out.println("flower, flow, flight: " + longestCommonPrefix(words1)); // fl
        System.out.println("dog, racecar, car: " + longestCommonPrefix(words2));   // (empty)

        // Longest Common Substring
        System.out.println("\n--- Longest Common Substring ---");
        String s1 = "abcdefg";
        String s2 = "xyzabc";
        System.out.println("abcdefg & xyzabc: " + longestCommonSubstring(s1, s2)); // abc

        // String compression (Run-length encoding)
        System.out.println("\n--- String Compression ---");
        String[] toCompress = {"aaabbc", "abcd", "aaaaaaaaaa"};
        for (String s : toCompress) {
            System.out.printf("'%s' -> '%s'%n", s, compress(s));
        }

        // Reverse words in sentence
        System.out.println("\n--- Reverse Words ---");
        String sentence = "Hello World Java";
        System.out.println("Original: " + sentence);
        System.out.println("Reversed: " + reverseWords(sentence)); // Java World Hello

        // KMP Pattern Search
        System.out.println("\n--- KMP Pattern Search ---");
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        int index = kmpSearch(text, pattern);
        System.out.printf("Pattern found at index: %d%n", index); // 10

        // Expected output demonstrates all algorithms
    }

    // Palindrome: O(n) time, O(1) space
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

    // Anagram: O(n) time, O(1) space (fixed alphabet)
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

    // Frequency: O(n) time, O(1) space
    static void printFrequency(String s) {
        int[] freq = new int[256]; // Extended ASCII
        for (char c : s.toCharArray()) freq[c]++;
        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0) System.out.printf("'%c': %d ", (char) i, freq[i]);
        }
        System.out.println();
    }

    // Longest Common Prefix: O(n*m) where n=strings, m=min length
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

    // Longest Common Substring: O(n*m) DP
    static String longestCommonSubstring(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        int maxLen = 0, endIndex = 0;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLen) {
                        maxLen = dp[i][j];
                        endIndex = i;
                    }
                }
            }
        }
        return a.substring(endIndex - maxLen, endIndex);
    }

    // Run-length encoding compression
    static String compress(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        char current = s.charAt(0);
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == current) {
                count++;
            } else {
                sb.append(current).append(count);
                current = s.charAt(i);
                count = 1;
            }
        }
        sb.append(current).append(count);
        return sb.length() < s.length() ? sb.toString() : s; // Return original if not smaller
    }

    // Reverse words: O(n) time, O(n) space
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

    // KMP Algorithm: O(n+m) time, O(m) space
    static int kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = computeLPS(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == pattern.length()) return i - j;
            } else {
                if (j != 0) j = lps[j - 1];
                else i++;
            }
        }
        return -1;
    }

    static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0, i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) len = lps[len - 1];
                else lps[i++] = 0;
            }
        }
        return lps;
    }
}