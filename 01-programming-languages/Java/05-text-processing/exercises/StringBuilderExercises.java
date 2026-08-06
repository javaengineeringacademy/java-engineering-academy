package academy.javaengineering.exercises;

/**
 * Exercises: StringBuilder and StringBuffer Operations
 *
 * Complete the TODO sections below.
 */
public class StringBuilderExercises {

    // TODO 1: Build a CSV string from an array of strings
    // ["a", "b", "c"] -> "a,b,c"
    // Empty array -> ""
    public String buildCsv(String[] values) {
        // TODO: implement using StringBuilder
        return "";
    }

    // TODO 2: Remove all whitespace from a string using StringBuilder
    // "H e l l o" -> "Hello"
    public String removeWhitespace(String input) {
        // TODO: implement using StringBuilder
        return "";
    }

    // TODO 3: Interleave two strings character by character
    // "abc" and "123" -> "a1b2c3"
    // If different lengths, append remaining characters
    public String interleaveStrings(String s1, String s2) {
        // TODO: implement using StringBuilder
        return "";
    }

    // TODO 4: Repeat a string n times without using String.repeat()
    // "ab" repeated 3 -> "ababab"
    public String repeatString(String input, int n) {
        // TODO: implement using StringBuilder
        return "";
    }

    // TODO 5: Build a multiplication table string
    // For n=3:
    // "1x1=1\n1x2=2\n1x3=3\n2x1=2\n2x2=4\n2x3=6\n3x1=3\n3x2=6\n3x3=9"
    public String multiplicationTable(int n) {
        // TODO: implement using StringBuilder
        return "";
    }

    // TODO 6: Efficiently concatenate strings in a loop
    // Given a list of words, join them with spaces
    // Use StringBuilder for efficiency
    public String joinWords(String[] words) {
        // TODO: implement
        return "";
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        StringBuilderExercises exercises = new StringBuilderExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== StringBuilderExercises Tests ===\n");

        // Test 1
        total++;
        if ("a,b,c".equals(exercises.buildCsv(new String[]{"a", "b", "c"}))
            && "".equals(exercises.buildCsv(new String[]{}))) {
            System.out.println("Test 1 PASSED: buildCsv");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: buildCsv");
        }

        // Test 2
        total++;
        if ("Hello".equals(exercises.removeWhitespace("H e l l o"))
            && "Test".equals(exercises.removeWhitespace("T e s t"))) {
            System.out.println("Test 2 PASSED: removeWhitespace");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: removeWhitespace");
        }

        // Test 3
        total++;
        if ("a1b2c3".equals(exercises.interleaveStrings("abc", "123"))
            && "a1b2c3d".equals(exercises.interleaveStrings("abcd", "123"))) {
            System.out.println("Test 3 PASSED: interleaveStrings");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: interleaveStrings");
        }

        // Test 4
        total++;
        if ("ababab".equals(exercises.repeatString("ab", 3))
            && "".equals(exercises.repeatString("x", 0))) {
            System.out.println("Test 4 PASSED: repeatString");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: repeatString");
        }

        // Test 5
        total++;
        String table = exercises.multiplicationTable(2);
        if ("1x1=1\n1x2=2\n2x1=2\n2x2=4".equals(table)) {
            System.out.println("Test 5 PASSED: multiplicationTable");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: multiplicationTable - got '" + table + "'");
        }

        // Test 6
        total++;
        if ("hello world".equals(exercises.joinWords(new String[]{"hello", "world"}))
            && "".equals(exercises.joinWords(new String[]{}))) {
            System.out.println("Test 6 PASSED: joinWords");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: joinWords");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
