package academy.javaengineering.interview;

import java.util.*;

/**
 * Demonstrates common Java interview algorithms.
 */
public class InterviewAlgorithms {

    public int fibonacci(int n) {
        if (n <= 1) return n;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    public boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        
        int[] charCount = new int[26];
        for (char c : s1.toLowerCase().toCharArray()) {
            charCount[c - 'a']++;
        }
        for (char c : s2.toLowerCase().toCharArray()) {
            charCount[c - 'a']--;
        }
        return Arrays.stream(charCount).allMatch(count -> count == 0);
    }

    public int maxSubarraySum(int[] nums) {
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
}
