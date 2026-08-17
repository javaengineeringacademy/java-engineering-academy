package academy.javaengineering.exercises;

import java.util.Locale;
import java.text.*;

/**
 * Exercises: Internationalization (i18n)
 *
 * Complete the TODO sections below.
 */
public class InternationalizationExercises {

    // TODO 1: Format a number with grouping separators based on locale
    // US locale: 1234567 -> "1,234,456" (approximately)
    // German locale: 1234567 -> "1.234.567"
    public String formatNumber(long number, Locale locale) {
        // TODO: implement using NumberFormat.getNumberInstance(locale)
        return "";
    }

    // TODO 2: Format a currency value for a given locale
    // US: $1,234.56
    // Japan: 1,234.56
    public String formatCurrency(double amount, Locale locale) {
        // TODO: implement using NumberFormat.getCurrencyInstance(locale)
        return "";
    }

    // TODO 3: Format a date in the given locale's style
    // US: January 15, 2024
    // Germany: 15. Januar 2024
    public String formatDate(java.util.Date date, Locale locale) {
        // TODO: implement using DateFormat.getDateInstance(DateFormat.LONG, locale)
        return "";
    }

    // TODO 4: Format a percentage value
    // 0.756 with US locale -> "75.6%"
    public String formatPercentage(double value, Locale locale) {
        // TODO: implement using NumberFormat.getPercentInstance(locale)
        return "";
    }

    // TODO 5: Convert a string to uppercase using locale-specific rules
    // Turkish locale: "i" -> "\u0130" (capital I with dot)
    public String localeUpperCase(String input, Locale locale) {
        // TODO: implement using toUpperCase(locale)
        return "";
    }

    // TODO 6: Parse a formatted number string back to a long
    public long parseNumber(String formatted, Locale locale) throws ParseException {
        // TODO: implement using NumberFormat.getNumberInstance(locale).parse()
        return 0;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        InternationalizationExercises exercises = new InternationalizationExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== InternationalizationExercises Tests ===\n");

        // Test 1
        total++;
        String usNum = exercises.formatNumber(1234567, Locale.US);
        if (usNum.contains("1") && usNum.contains("234") && usNum.contains("567")) {
            System.out.println("Test 1 PASSED: formatNumber");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: formatNumber - got: " + usNum);
        }

        // Test 2
        total++;
        String usCurrency = exercises.formatCurrency(1234.56, Locale.US);
        if (usCurrency.contains("$") && usCurrency.contains("1")) {
            System.out.println("Test 2 PASSED: formatCurrency");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: formatCurrency - got: " + usCurrency);
        }

        // Test 3
        total++;
        java.util.Date now = new java.util.Date();
        String dateStr = exercises.formatDate(now, Locale.US);
        if (dateStr != null && !dateStr.isEmpty()) {
            System.out.println("Test 3 PASSED: formatDate");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: formatDate");
        }

        // Test 4
        total++;
        String pct = exercises.formatPercentage(0.756, Locale.US);
        if (pct.contains("75") && pct.contains("%")) {
            System.out.println("Test 4 PASSED: formatPercentage");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: formatPercentage - got: " + pct);
        }

        // Test 5
        total++;
        String upper = exercises.localeUpperCase("hello", Locale.US);
        if ("HELLO".equals(upper)) {
            System.out.println("Test 5 PASSED: localeUpperCase");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: localeUpperCase - got: " + upper);
        }

        // Test 6
        total++;
        try {
            long parsed = exercises.parseNumber("1,234,567", Locale.US);
            if (parsed == 1234567) {
                System.out.println("Test 6 PASSED: parseNumber");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: parseNumber - got: " + parsed);
            }
        } catch (ParseException e) {
            System.out.println("Test 6 FAILED: parseNumber - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
