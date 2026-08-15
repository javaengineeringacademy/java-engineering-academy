package academy.javaengineering.exercises;

import java.util.regex.*;
import java.util.*;

/**
 * Exercises: Regular Expressions (Pattern, Matcher, Real-world Patterns)
 *
 * Complete the TODO sections below.
 */
public class RegexExercises {

    // TODO 1: Validate an email address using regex
    // Basic pattern: word chars, @, word chars, ., 2-10 letter suffix
    public boolean isValidEmail(String email) {
        // TODO: implement using Pattern and Matcher
        return false;
    }

    // TODO 2: Extract all phone numbers from text
    // Phone formats: (123) 456-7890, 123-456-7890, 123.456.7890
    // Return list of found phone numbers
    public List<String> extractPhoneNumbers(String text) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 3: Find all words that start with a capital letter
    public List<String> findCapitalizedWords(String text) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 4: Validate a strong password
    // Must have: at least 8 chars, one uppercase, one lowercase, one digit, one special char
    public boolean isStrongPassword(String password) {
        // TODO: implement using regex
        return false;
    }

    // TODO 5: Extract HTML tags and their content
    // Given "<b>bold</b> and <i>italic</i>", return Map: {"b": "bold", "i": "italic"}
    public Map<String, String> extractHtmlTags(String html) {
        // TODO: implement
        return new LinkedHashMap<>();
    }

    // TODO 6: Replace multiple spaces with single space
    // "hello    world  foo" -> "hello world foo"
    public String normalizeSpaces(String input) {
        // TODO: implement using regex
        return "";
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        RegexExercises exercises = new RegexExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== RegexExercises Tests ===\n");

        // Test 1
        total++;
        if (exercises.isValidEmail("user@example.com")
            && exercises.isValidEmail("test.name@domain.org")
            && !exercises.isValidEmail("invalid@")
            && !exercises.isValidEmail("@domain.com")) {
            System.out.println("Test 1 PASSED: isValidEmail");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: isValidEmail");
        }

        // Test 2
        total++;
        String text = "Call (555) 123-4567 or 555.987.6543 or 555-111-2222";
        List<String> phones = exercises.extractPhoneNumbers(text);
        if (phones.size() == 3) {
            System.out.println("Test 2 PASSED: extractPhoneNumbers");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: extractPhoneNumbers - found " + phones.size() + " phones");
        }

        // Test 3
        total++;
        List<String> caps = exercises.findCapitalizedWords("The Quick Brown Fox jumps");
        if (caps.size() == 4 && caps.contains("The") && caps.contains("Quick") && caps.contains("Brown") && caps.contains("Fox")) {
            System.out.println("Test 3 PASSED: findCapitalizedWords");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: findCapitalizedWords - " + caps);
        }

        // Test 4
        total++;
        if (exercises.isStrongPassword("MyP@ss123")
            && !exercises.isStrongPassword("weak")
            && !exercises.isStrongPassword("noupper1!")
            && !exercises.isStrongPassword("NOLOWER1!")) {
            System.out.println("Test 4 PASSED: isStrongPassword");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: isStrongPassword");
        }

        // Test 5
        total++;
        Map<String, String> tags = exercises.extractHtmlTags("<b>bold</b> and <i>italic</i>");
        if ("bold".equals(tags.get("b")) && "italic".equals(tags.get("i"))) {
            System.out.println("Test 5 PASSED: extractHtmlTags");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: extractHtmlTags - " + tags);
        }

        // Test 6
        total++;
        if ("hello world foo".equals(exercises.normalizeSpaces("hello    world  foo"))) {
            System.out.println("Test 6 PASSED: normalizeSpaces");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: normalizeSpaces");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
