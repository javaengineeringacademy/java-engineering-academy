package academy.javaengineering.text.examples;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Internationalization Examples - Practical demonstrations of i18n usage.
 * 
 * WHY INTERNATIONALIZATION IS IMPORTANT:
 * - Global applications need to support multiple locales
 * - Number/date/currency formatting varies by region
 * - User experience improves with localized content
 * 
 * KEY CONCEPTS:
 * - Locale: Language + country/region
 * - Resource Bundle: Locale-specific resources
 * - NumberFormat/DateFormat: Locale-aware formatting
 */
public class InternationalizationExamples {

    public static void main(String[] args) {
        System.out.println("=== Internationalization Examples ===\n");

        // Example 1: Locale Basics
        example1_LocaleBasics();

        // Example 2: Number Formatting
        example2_NumberFormatting();

        // Example 3: Currency Formatting
        example3_CurrencyFormatting();

        // Example 4: Date Formatting
        example4_DateFormatting();
    }

    /**
     * WHY: Locale represents user's language and region.
     * 
     * ENGINEERING DECISION: Always use Locale for user-facing content.
     */
    private static void example1_LocaleBasics() {
        System.out.println("--- Example 1: Locale Basics ---");

        Locale us = Locale.US;
        Locale france = Locale.FRANCE;
        Locale japan = Locale.JAPAN;

        System.out.println("US: " + us.getDisplayCountry());
        System.out.println("France: " + france.getDisplayCountry());
        System.out.println("Japan: " + japan.getDisplayCountry());

        System.out.println("\nUS language: " + us.getDisplayLanguage());
        System.out.println("France language: " + france.getDisplayLanguage());
        System.out.println("Japan language: " + japan.getDisplayLanguage());
    }

    /**
     * WHY: Number formatting varies by locale.
     * 
     * ENGINEERING DECISION: Use NumberFormat for locale-aware number display.
     */
    private static void example2_NumberFormatting() {
        System.out.println("\n--- Example 2: Number Formatting ---");

        double number = 1234567.89;

        NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat franceFormat = NumberFormat.getNumberInstance(Locale.FRANCE);
        NumberFormat japanFormat = NumberFormat.getNumberInstance(Locale.JAPAN);

        System.out.println("Number: " + number);
        System.out.println("US: " + usFormat.format(number));
        System.out.println("France: " + franceFormat.format(number));
        System.out.println("Japan: " + japanFormat.format(number));
    }

    /**
     * WHY: Currency formatting is locale-specific.
     * 
     * ENGINEERING DECISION: Use Currency.getInstance() for accurate currency codes.
     */
    private static void example3_CurrencyFormatting() {
        System.out.println("\n--- Example 3: Currency Formatting ---");

        double amount = 1234.56;

        NumberFormat usCurrency = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat euroCurrency = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        NumberFormat japanCurrency = NumberFormat.getCurrencyInstance(Locale.JAPAN);

        System.out.println("Amount: " + amount);
        System.out.println("USD: " + usCurrency.format(amount));
        System.out.println("EUR: " + euroCurrency.format(amount));
        System.out.println("JPY: " + japanCurrency.format(amount));

        System.out.println("\nUSD currency: " + Currency.getInstance(Locale.US));
        System.out.println("EUR currency: " + Currency.getInstance(Locale.FRANCE));
    }

    /**
     * WHY: Date formatting varies by locale.
     * 
     * ENGINEERING DECISION: Use DateFormat for locale-aware date display.
     */
    private static void example4_DateFormatting() {
        System.out.println("\n--- Example 4: Date Formatting ---");

        java.util.Date now = new java.util.Date();

        DateFormat usDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        DateFormat franceDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);
        DateFormat japanDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.JAPAN);

        System.out.println("Date: " + now);
        System.out.println("US: " + usDate.format(now));
        System.out.println("France: " + franceDate.format(now));
        System.out.println("Japan: " + japanDate.format(now));
    }
}
