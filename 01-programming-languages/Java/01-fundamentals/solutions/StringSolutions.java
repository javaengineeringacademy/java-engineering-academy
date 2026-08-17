package academy.javaengineering.exercises.solutions;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Solutions: Strings (Reverse, Anagram, Compression, First Non-Repeating)
 */
public class StringSolutions {

    public String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(" ");
        }
        return sb.toString();
    }

    public boolean isAnagram(String s1, String s2) {
        String clean1 = s1.replaceAll("\\s", "").toLowerCase();
        String clean2 = s2.replaceAll("\\s", "").toLowerCase();
        if (clean1.length() != clean2.length()) return false;
        char[] arr1 = clean1.toCharArray();
        char[] arr2 = clean2.toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }

    public String compress(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                sb.append(s.charAt(i - 1));
                sb.append(count);
                count = 1;
            }
        }
        sb.append(s.charAt(s.length() - 1));
        sb.append(count);
        return sb.length() < s.length() ? sb.toString() : s;
    }

    public int firstNonRepeatingChar(String s) {
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 1) {
                return s.indexOf(entry.getKey());
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        StringSolutions solutions = new StringSolutions();
        System.out.println("=== String Solutions ===\n");

        System.out.println("1. reverseWords('the sky is blue') = " + solutions.reverseWords("the sky is blue"));
        System.out.println("   reverseWords('  hello world  ') = " + solutions.reverseWords("  hello world  "));

        System.out.println("\n2. isAnagram('listen', 'silent') = " + solutions.isAnagram("listen", "silent"));
        System.out.println("   isAnagram('Dormitory', 'Dirty Room') = " + solutions.isAnagram("Dormitory", "Dirty Room"));

        System.out.println("\n3. compress('aabcccccaaa') = " + solutions.compress("aabcccccaaa"));
        System.out.println("   compress('abcd') = " + solutions.compress("abcd"));

        System.out.println("\n4. firstNonRepeating('leetcode') = " + solutions.firstNonRepeatingChar("leetcode"));
        System.out.println("   firstNonRepeating('loveleetcode') = " + solutions.firstNonRepeatingChar("loveleetcode"));
    }
}
