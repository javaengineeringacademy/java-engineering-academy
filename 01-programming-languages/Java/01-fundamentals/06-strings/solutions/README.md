# Strings - Solutions

```java
import java.util.HashMap;
import java.util.Map;

public class StringSolutions {

    // Reverse string
    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    // Palindrome checker
    static boolean isPalindrome(String s) {
        String clean = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        int left = 0, right = clean.length() - 1;
        while (left < right) {
            if (clean.charAt(left++) != clean.charAt(right--)) return false;
        }
        return true;
    }

    // Word counter
    static int countWords(String s) {
        return s.trim().split("\\s+").length;
    }

    // Character frequency
    static Map<Character, Integer> charFrequency(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }
        return freq;
    }

    // String compression
    static String compress(String s) {
        if (s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (int i = 1; i <= s.length(); i++) {
            if (i < s.length() && s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                sb.append(s.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        return sb.length() < s.length() ? sb.toString() : s;
    }

    // Anagram checker
    static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) return false;
        char[] arr1 = a.toLowerCase().toCharArray();
        char[] arr2 = b.toLowerCase().toCharArray();
        java.util.Arrays.sort(arr1);
        java.util.Arrays.sort(arr2);
        return java.util.Arrays.equals(arr1, arr2);
    }

    public static void main(String[] args) {
        System.out.println("Reversed: " + reverse("hello"));
        System.out.println("Palindrome: " + isPalindrome("A man a plan a canal Panama"));
        System.out.println("Word count: " + countWords("  Hello   World  "));
        System.out.println("Compressed: " + compress("aabcccccaaa"));
        System.out.println("Anagram: " + isAnagram("listen", "silent"));
        System.out.println("Char freq: " + charFrequency("hello"));
    }
}
```
