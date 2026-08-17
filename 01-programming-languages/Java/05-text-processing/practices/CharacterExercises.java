package academy.javaengineering.exercises;

/**
 * Exercises: Character Class Operations
 *
 * Complete the TODO sections below.
 */
public class CharacterExercises {

    // TODO 1: Check if a character is a vowel (a, e, i, o, u) - case insensitive
    public boolean isVowel(char ch) {
        // TODO: implement using Character.toLowerCase()
        return false;
    }

    // TODO 2: Count digits in a string
    public int countDigits(String input) {
        // TODO: implement using Character.isDigit()
        return 0;
    }

    // TODO 3: Convert a string to alternating case
    // "hello" -> "HeLlO"
    public String alternatingCase(String input) {
        // TODO: implement using Character.toUpperCase/toLowerCase
        return "";
    }

    // TODO 4: Check if a string contains only letters
    public boolean isAllLetters(String input) {
        // TODO: implement using Character.isLetter()
        return false;
    }

    // TODO 5: Get the numeric value of a Roman numeral character (I=1, V=5, etc.)
    public int romanCharValue(char ch) {
        // TODO: implement using switch and Character.toUpperCase()
        return 0;
    }

    // TODO 6: Count letter frequency of a specific character in a string (case-insensitive)
    public int charFrequency(String input, char target) {
        // TODO: implement using Character.toLowerCase()
        return 0;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        CharacterExercises exercises = new CharacterExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== CharacterExercises Tests ===\n");

        // Test 1
        total++;
        if (exercises.isVowel('a') && exercises.isVowel('E') && !exercises.isVowel('b')) {
            System.out.println("Test 1 PASSED: isVowel");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: isVowel");
        }

        // Test 2
        total++;
        if (exercises.countDigits("abc123def456") == 6 && exercises.countDigits("no digits") == 0) {
            System.out.println("Test 2 PASSED: countDigits");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: countDigits");
        }

        // Test 3
        total++;
        if ("HeLlO".equals(exercises.alternatingCase("hello"))) {
            System.out.println("Test 3 PASSED: alternatingCase");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: alternatingCase");
        }

        // Test 4
        total++;
        if (exercises.isAllLetters("Hello") && !exercises.isAllLetters("Hello123")) {
            System.out.println("Test 4 PASSED: isAllLetters");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: isAllLetters");
        }

        // Test 5
        total++;
        if (exercises.romanCharValue('V') == 5 && exercises.romanCharValue('x') == 10) {
            System.out.println("Test 5 PASSED: romanCharValue");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: romanCharValue");
        }

        // Test 6
        total++;
        if (exercises.charFrequency("Hello World", 'l') == 3) {
            System.out.println("Test 6 PASSED: charFrequency");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: charFrequency");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
