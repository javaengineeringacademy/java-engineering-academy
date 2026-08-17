package academy.javaengineering.exercises;

/**
 * Exercises: Unicode Operations
 *
 * Complete the TODO sections below.
 */
public class UnicodeExercises {

    // TODO 1: Get the Unicode code point of a character
    public int getCodePoint(char ch) {
        // TODO: implement using Character.codePointAt()
        return 0;
    }

    // TODO 2: Convert a Unicode code point to a string
    public String codePointToString(int codePoint) {
        // TODO: implement using Character.toString() or new String(Character.toChars())
        return "";
    }

    // TODO 3: Count the number of Unicode code points in a string
    // Account for supplementary characters (emoji, etc.) that use 2 char values
    public int countCodePoints(String input) {
        // TODO: implement using input.codePointCount(0, input.length())
        return 0;
    }

    // TODO 4: Check if a string contains any supplementary characters (outside BMP)
    public boolean hasSupplementaryCharacters(String input) {
        // TODO: check if any char value is outside the BMP (0x10000+)
        return false;
    }

    // TODO 5: Convert a string to uppercase using Unicode rules
    public String unicodeUpperCase(String input) {
        // TODO: implement using toUpperCase() which uses Unicode rules
        return "";
    }

    // TODO 6: Check if a code point is an emoji
    public boolean isEmoji(int codePoint) {
        // TODO: check common emoji ranges (0x1F600-0x1F64F, 0x1F300-0x1F5FF, etc.)
        return false;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        UnicodeExercises exercises = new UnicodeExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== UnicodeExercises Tests ===\n");

        // Test 1
        total++;
        if (exercises.getCodePoint('A') == 65 && exercises.getCodePoint('\u00E9') == 233) {
            System.out.println("Test 1 PASSED: getCodePoint");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: getCodePoint");
        }

        // Test 2
        total++;
        if ("A".equals(exercises.codePointToString(65)) && "\u00E9".equals(exercises.codePointToString(233))) {
            System.out.println("Test 2 PASSED: codePointToString");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: codePointToString");
        }

        // Test 3
        total++;
        if (exercises.countCodePoints("Hello") == 5 && exercises.countCodePoints("") == 0) {
            System.out.println("Test 3 PASSED: countCodePoints");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: countCodePoints");
        }

        // Test 4
        total++;
        if (!exercises.hasSupplementaryCharacters("Hello") && exercises.hasSupplementaryCharacters("\uD83D\uDE00")) {
            System.out.println("Test 4 PASSED: hasSupplementaryCharacters");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: hasSupplementaryCharacters");
        }

        // Test 5
        total++;
        if ("HELLO".equals(exercises.unicodeUpperCase("hello")) && "\u00C9".equals(exercises.unicodeUpperCase("\u00E9"))) {
            System.out.println("Test 5 PASSED: unicodeUpperCase");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: unicodeUpperCase");
        }

        // Test 6
        total++;
        if (exercises.isEmoji(0x1F600) && !exercises.isEmoji(65)) {
            System.out.println("Test 6 PASSED: isEmoji");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: isEmoji");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
