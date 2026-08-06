import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18nDemo.java
 *
 * Demonstrates internationalization (i18n) in Java.
 */
public class I18nDemo {

    public static void main(String[] args) {
        System.out.println("=== Internationalization (i18n) Demo ===\n");

        // 1. Locale basics
        localeBasics();

        // 2. ResourceBundle
        resourceBundleDemo();

        // 3. NumberFormat
        numberFormatDemo();

        // 4. DateFormat
        dateFormatDemo();

        // 5. CurrencyFormat
        currencyFormatDemo();
    }

    private static void localeBasics() {
        System.out.println("--- 1. Locale Basics ---");

        // Default locale
        Locale defaultLocale = Locale.getDefault();
        System.out.println("Default locale: " + defaultLocale);
        System.out.println("Display name: " + defaultLocale.getDisplayName());
        System.out.println("Language: " + defaultLocale.getLanguage());
        System.out.println("Country: " + defaultLocale.getCountry());

        // Creating specific locales
        Locale us = Locale.US;
        Locale france = Locale.FRANCE;
        Locale japan = Locale.JAPAN;
        Locale china = Locale.CHINA;

        System.out.println("\nSpecific locales:");
        System.out.println("US: " + us.getDisplayName());
        System.out.println("France: " + france.getDisplayName());
        System.out.println("Japan: " + japan.getDisplayName());
        System.out.println("China: " + china.getDisplayName());

        // Locale from language and country
        Locale custom = new Locale("es", "ES");  // Spanish, Spain
        System.out.println("\nCustom locale (es_ES): " + custom.getDisplayName());

        System.out.println();
    }

    private static void resourceBundleDemo() {
        System.out.println("--- 2. ResourceBundle ---");

        // Note: This requires resource bundle files
        // For demo purposes, we'll show the concept

        try {
            // Load resource bundle for default locale
            ResourceBundle bundle = ResourceBundle.getBundle("Messages");

            // Get localized strings
            String greeting = bundle.getString("greeting");
            String farewell = bundle.getString("farewell");

            System.out.println("Greeting: " + greeting);
            System.out.println("Farewell: " + farewell);

            // Load for specific locale
            Locale french = Locale.FRANCE;
            ResourceBundle frenchBundle = ResourceBundle.getBundle("Messages", french);

            System.out.println("\nFrench greeting: " + frenchBundle.getString("greeting"));
        } catch (MissingResourceException e) {
            System.out.println("Resource bundle not found (expected in demo)");
            System.out.println("In real app, create Messages.properties files:");
            System.out.println("  Messages.properties (default)");
            System.out.println("  Messages_fr.properties (French)");
            System.out.println("  Messages_es.properties (Spanish)");
        }

        System.out.println();
    }

    private static void numberFormatDemo() {
        System.out.println("--- 3. NumberFormat ---");

        double number = 1234567.891;

        // Get number format for different locales
        NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat germanyFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        NumberFormat franceFormat = NumberFormat.getNumberInstance(Locale.FRANCE);

        System.out.println("Number: " + number);
        System.out.println("US format: " + usFormat.format(number));
        System.out.println("Germany format: " + germanyFormat.format(number));
        System.out.println("France format: " + franceFormat.format(number));

        // Currency format
        NumberFormat usCurrency = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat japanCurrency = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        NumberFormat germanyCurrency = NumberFormat.getCurrencyInstance(Locale.GERMANY);

        System.out.println("\nCurrency formats:");
        System.out.println("US: " + usCurrency.format(number));
        System.out.println("Japan: " + japanCurrency.format(number));
        System.out.println("Germany: " + germanyCurrency.format(number));

        // Percent format
        NumberFormat usPercent = NumberFormat.getPercentInstance(Locale.US);
        System.out.println("\nPercent format (US): " + usPercent.format(0.85));

        System.out.println();
    }

    private static void dateFormatDemo() {
        System.out.println("--- 4. DateFormat ---");

        java.util.Date now = new java.util.Date();

        // Get date format for different locales
        DateFormat usFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        DateFormat franceFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);
        DateFormat japanFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.JAPAN);

        System.out.println("Date formats:");
        System.out.println("US: " + usFormat.format(now));
        System.out.println("France: " + franceFormat.format(now));
        System.out.println("Japan: " + japanFormat.format(now));

        // Time format
        DateFormat usTime = DateFormat.getTimeInstance(DateFormat.LONG, Locale.US);
        DateFormat germanyTime = DateFormat.getTimeInstance(DateFormat.LONG, Locale.GERMANY);

        System.out.println("\nTime formats:");
        System.out.println("US: " + usTime.format(now));
        System.out.println("Germany: " + germanyTime.format(now));

        // Date and time format
        DateFormat usDateTime = DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG, Locale.US);
        System.out.println("\nDateTime (US): " + usDateTime.format(now));

        System.out.println();
    }

    private static void currencyFormatDemo() {
        System.out.println("--- 5. CurrencyFormat ---");

        double amount = 1234.56;

        // Get currency for different locales
        Currency usd = Currency.getInstance(Locale.US);
        Currency eur = Currency.getInstance(Locale.GERMANY);
        Currency jpy = Currency.getInstance(Locale.JAPAN);

        System.out.println("Currency information:");
        System.out.println("USD: " + usd.getCurrencyCode() + " (" +
            usd.getDisplayName() + ")");
        System.out.println("EUR: " + eur.getCurrencyCode() + " (" +
            eur.getDisplayName() + ")");
        System.out.println("JPY: " + jpy.getCurrencyCode() + " (" +
            jpy.getDisplayName() + ")");

        // Format currency
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat germanyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        NumberFormat japanFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN);

        System.out.println("\nCurrency formatting:");
        System.out.println("US (USD): " + usFormat.format(amount));
        System.out.println("Germany (EUR): " + germanyFormat.format(amount));
        System.out.println("Japan (JPY): " + japanFormat.format(amount));

        // Currency symbols
        System.out.println("\nCurrency symbols:");
        System.out.println("USD symbol: " + usd.getSymbol(Locale.US));
        System.out.println("EUR symbol: " + eur.getSymbol(Locale.GERMANY));
        System.out.println("JPY symbol: " + jpy.getSymbol(Locale.JAPAN));

        System.out.println();
    }
}
