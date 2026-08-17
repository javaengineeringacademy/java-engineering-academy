package academy.javaengineering.oop.practices;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Practice: Internationalization (i18n) in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Using Locale for regional formatting
 * - NumberFormat for different locales
 * - Currency formatting by locale
 * - Understanding locale-sensitive string operations
 * - Formatting dates and numbers appropriately
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 09-internationalization ===\n");

        // Test Exercise 1: formatNumberByLocale
        String usFormat = formatNumberByLocale(1234567.891, Locale.US);
        String germanFormat = formatNumberByLocale(1234567.891, Locale.GERMANY);
        System.out.println("Exercise 1 - formatNumberByLocale: "
            + (usFormat.contains(",") && germanFormat.contains(".") ? "PASS" : "FAIL"));

        // Test Exercise 2: formatCurrency
        String usCurrency = formatCurrency(1234.56, Locale.US);
        String japanCurrency = formatCurrency(1234.56, Locale.JAPAN);
        System.out.println("Exercise 2 - formatCurrency: "
            + (usCurrency.contains("$") && !usCurrency.contains(",1234") ? "PASS" : "FAIL"));

        // Test Exercise 3: getCurrencySymbol
        String usdSymbol = getCurrencySymbol(Locale.US);
        String eurSymbol = getCurrencySymbol(Locale.GERMANY);
        System.out.println("Exercise 3 - getCurrencySymbol: "
            + ("$".equals(usdSymbol) && "\u20AC".equals(eurSymbol) ? "PASS" : "FAIL"));

        // Test Exercise 4: formatPercent
        String percent = formatPercent(0.856, Locale.US);
        System.out.println("Exercise 4 - formatPercent: "
            + (percent.contains("85") && percent.contains("%") ? "PASS" : "FAIL"));

        // Test Exercise 5: getLocaleDisplayInfo
        String info = getLocaleDisplayInfo(Locale.JAPAN);
        System.out.println("Exercise 5 - getLocaleDisplayInfo: "
            + (info != null && !info.isEmpty() ? "PASS" : "FAIL"));
    }

    // TODO 1: Format a number according to the given locale
    // Use NumberFormat.getNumberInstance(locale) and format the number
    // US uses commas as group separators, Germany uses dots
    static String formatNumberByLocale(double number, Locale locale) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Format a number as currency for the given locale
    // Use NumberFormat.getCurrencyInstance(locale)
    static String formatCurrency(double amount, Locale locale) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Get the currency symbol for a locale
    // Use Currency.getInstance(locale).getSymbol(locale)
    static String getCurrencySymbol(Locale locale) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Format a decimal as a percentage
    // 0.856 -> "85.6%" for US locale
    // Use NumberFormat.getPercentInstance(locale)
    static String formatPercent(double value, Locale locale) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Get a display string for a locale
    // Return locale.getDisplayName() which gives something like "Japanese (Japan)"
    static String getLocaleDisplayInfo(Locale locale) {
        // YOUR CODE HERE
        return null;
    }
}
