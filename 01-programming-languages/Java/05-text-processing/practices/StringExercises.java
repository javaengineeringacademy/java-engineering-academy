package academy.javaengineering.exercises;

/**
 * Exercises: String Operations and Immutability
 *
 * Complete the TODO sections below.
 */
public class StringExercises {

    // TODO 1: Count vowels and consonants in a string
    // Return an int array: [vowels, consonants]
    // Ignore case, only count letters a-z
    public int[] countVowelsConsonants(String input) {
        // TODO: implement this
        return new int[]{0, 0};
    }

    // TODO 2: Reverse a string without using StringBuilder.reverse()
    public String reverseString(String input) {
        // TODO: implement this
        return "";
    }

    // TODO 3: Check if two strings are anagrams (same characters, different order)
    // Ignore case and spaces
    public boolean areAnagrams(String s1, String s2) {
        // TODO: implement this
        return false;
    }

    // TODO 4: Find the first non-repeating character in a string
    // Return the character or '\0' if none exists
    public char firstNonRepeatingChar(String input) {
        // TODO: implement this
        return '\0';
    }

    // TODO 5: Compress a string using counts of repeated characters
    // "aabcccccaaa" -> "a2b1c5a3"
    // If compressed is not shorter than original, return original
    public String compressString(String input) {
        // TODO: implement this
        return input;
    }

    // TODO 6: Implement isPalindrome that ignores non-alphanumeric characters
    // "A man, a plan, a canal: Panama" should return true
    public boolean isPalindrome(String input) {
        // TODO: implement this
        return false;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        StringExercises exercises = new StringExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== StringExercises Tests ===\n");

        // Test 1
        total++;
        int[] vc = exercises.countVowelsConsonants("Hello World");
        if (vc[0] == 3 && vc[1] == 7) {
            System.out.println("Test 1 PASSED: countVowelsConsonants");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: countVowelsConsonants - expected [3,7], got [" + vc[0] + "," + vc[1] + "]");
        }

        // Test 2
        total++;
        if ("dlrow".equals(exercises.reverseString("world")) && "".equals(exercises.reverseString(""))) {
            System.out.println("Test 2 PASSED: reverseString");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: reverseString");
        }

        // Test 3
        total++;
        if (exercises.areAnagrams("Listen", "Silent") && !exercises.areAnagrams("Hello", "World")) {
            System.out.println("Test 3 PASSED: areAnagrams");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: areAnagrams");
        }

        // Test 4
        total++;
        char first = exercises.firstNonRepeatingChar("swiss");
        if (first == 'w') {
            System.out.println("Test 4 PASSED: firstNonRepeatingChar");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: firstNonRepeatingChar - expected 'w', got '" + first + "'");
        }

        // Test 5
        total++;
        String compressed = exercises.compressString("aabcccccaaa");
        if ("a2b1c5a3".equals(compressed)) {
            System.out.println("Test 5 PASSED: compressString");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: compressString - expected 'a2b1c5a3', got '" + compressed + "'");
        }

        // Test 6
        total++;
        if (exercises.isPalindrome("A man, a plan, a canal: Panama")
            && !exercises.isPalindrome("race a car")) {
            System.out.println("Test 6 PASSED: isPalindrome");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: isPalindrome");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
