package academy.javaengineering.strings;

import java.util.*;

public class StringPatterns {

    public static void main(String[] args) {
        demonstratePalindrome();
        demonstrateAnagram();
        demonstrateReverse();
        demonstrateDuplicateChars();
        demonstrateCharFrequency();
    }

    private static void demonstratePalindrome() {
        System.out.println("=== Palindrome Check ===");

        String[] testCases = {"racecar", "hello", "A man a plan a canal Panama", "race a car"};

        for (String test : testCases) {
            boolean result = isPalindrome(test);
            System.out.printf("'%s' is palindrome: %b%n", test, result);
        }

        System.out.println("\nUsing StringBuilder:");
        for (String test : testCases) {
            boolean result = isPalindromeStringBuilder(test);
            System.out.printf("'%s' is palindrome: %b%n", test, result);
        }
    }

    static boolean isPalindrome(String s) {
        if (s == null) return false;
        String cleaned = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        int left = 0;
        int right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    static boolean isPalindromeStringBuilder(String s) {
        if (s == null) return false;
        String cleaned = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed);
    }

    private static void demonstrateAnagram() {
        System.out.println("\n=== Anagram Check ===");

        String[][] testCases = {
            {"listen", "silent"},
            {"hello", "world"},
            {"triangle", "integral"},
            {"apple", "papel"}
        };

        for (String[] pair : testCases) {
            boolean result = isAnagram(pair[0], pair[1]);
            System.out.printf("'%s' and '%s' are anagrams: %b%n", pair[0], pair[1], result);
        }

        System.out.println("\nUsing character count:");
        for (String[] pair : testCases) {
            boolean result = isAnagramCounting(pair[0], pair[1]);
            System.out.printf("'%s' and '%s' are anagrams: %b%n", pair[0], pair[1], result);
        }
    }

    static boolean isAnagram(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        char[] chars1 = s1.toLowerCase().toCharArray();
        char[] chars2 = s2.toLowerCase().toCharArray();
        Arrays.sort(chars1);
        Arrays.sort(chars2);
        return Arrays.equals(chars1, chars2);
    }

    static boolean isAnagramCounting(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.length() != s2.length()) return false;

        int[] count = new int[26];
        for (char c : s1.toLowerCase().toCharArray()) {
            count[c - 'a']++;
        }
        for (char c : s2.toLowerCase().toCharArray()) {
            count[c - 'a']--;
        }
        for (int i : count) {
            if (i != 0) return false;
        }
        return true;
    }

    private static void demonstrateReverse() {
        System.out.println("\n=== String Reverse ===");

        String[] testCases = {"Hello", "Java", "Programming", ""};

        for (String test : testCases) {
            System.out.printf("Original: '%s', Reversed: '%s'%n", test, reverseString(test));
        }

        System.out.println("\nUsing char array:");
        for (String test : testCases) {
            System.out.printf("Original: '%s', Reversed: '%s'%n", test, reverseCharArray(test));
        }
    }

    static String reverseString(String s) {
        if (s == null) return null;
        return new StringBuilder(s).reverse().toString();
    }

    static String reverseCharArray(String s) {
        if (s == null) return null;
        char[] chars = s.toCharArray();
        int left = 0;
        int right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }

    private static void demonstrateDuplicateChars() {
        System.out.println("\n=== Duplicate Characters ===");

        String[] testCases = {"programming", "hello world", "abcdef", "aabcc"};

        for (String test : testCases) {
            Set<Character> duplicates = findDuplicateChars(test);
            System.out.printf("'%s' duplicates: %s%n", test, duplicates);
        }
    }

    static Set<Character> findDuplicateChars(String s) {
        Set<Character> seen = new HashSet<>();
        Set<Character> duplicates = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (!seen.add(c)) {
                duplicates.add(c);
            }
        }
        return duplicates;
    }

    private static void demonstrateCharFrequency() {
        System.out.println("\n=== Character Frequency ===");

        String[] testCases = {"programming", "hello", "aabbbccc"};

        for (String test : testCases) {
            Map<Character, Integer> freq = characterFrequency(test);
            System.out.printf("'%s' frequency: %s%n", test, freq);
        }
    }

    static Map<Character, Integer> characterFrequency(String s) {
        Map<Character, Integer> freq = new TreeMap<>();
        for (char c : s.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }
        return freq;
    }
}
