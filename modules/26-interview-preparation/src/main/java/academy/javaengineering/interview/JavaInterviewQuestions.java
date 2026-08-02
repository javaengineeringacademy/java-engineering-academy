package academy.javaengineering.interview;

import java.util.*;

/**
 * Java Interview Questions - Core Java questions and answers.
 */
public class JavaInterviewQuestions {

    public boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public boolean isPalindrome(String s) {
        String clean = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return clean.equals(reverseString(clean));
    }

    public int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    public List<Integer> findDuplicates(int[] arr) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        for (int num : arr) {
            if (!seen.add(num)) duplicates.add(num);
        }
        return new ArrayList<>(duplicates);
    }

    public static void main(String[] args) {
        JavaInterviewQuestions q = new JavaInterviewQuestions();
        System.out.println("Is Prime 17: " + q.isPrime(17));
        System.out.println("Reverse: " + q.reverseString("hello"));
        System.out.println("Is Palindrome: " + q.isPalindrome("racecar"));
        System.out.println("Fibonacci 10: " + q.fibonacci(10));
        System.out.println("Duplicates: " + q.findDuplicates(new int[]{1, 2, 3, 2, 4, 3}));
    }
}
