# Internationalization (i18n) in Java

Internationalization (i18n) is the process of designing applications
to support multiple languages and regions without code changes.
Java provides comprehensive APIs for i18n support.

## What is i18n?

Internationalization allows applications to:
- Display text in different languages
- Format numbers, dates, and currencies according to locale
- Handle cultural differences in formatting
- Support right-to-left languages

## Locale

A `Locale` represents a specific geographical, political, or cultural
region. It's used to format data according to regional conventions.

```java
import java.util.Locale;

// Get default locale
Locale defaultLocale = Locale.getDefault();

// Specific locales
Locale us = Locale.US;
Locale france = Locale.FRANCE;
Locale japan = Locale.JAPAN;

// Custom locale
Locale spanish = new Locale("es", "ES");

// Display information
System.out.println(defaultLocale.getDisplayName());
System.out.println(defaultLocale.getLanguage());
System.out.println(defaultLocale.getCountry());
```

## ResourceBundle

`ResourceBundle` provides a mechanism for loading locale-specific
resources (strings, images, etc.) at runtime.

```java
import java.util.ResourceBundle;

// Load resource bundle
ResourceBundle bundle = ResourceBundle.getBundle("Messages");

// Get localized strings
String greeting = bundle.getString("greeting");
String farewell = bundle.getString("farewell");

// Load for specific locale
Locale french = Locale.FRANCE;
ResourceBundle frenchBundle = ResourceBundle.getBundle("Messages", french);
```

### Resource Bundle Files

```
Messages.properties          (default)
Messages_en.properties       (English)
Messages_fr.properties       (French)
Messages_de.properties       (German)
Messages_es.properties       (Spanish)
Messages_ja.properties       (Japanese)
```

## NumberFormat

`NumberFormat` formats and parses numbers according to locale.

```java
import java.text.NumberFormat;
import java.util.Locale;

double number = 1234567.891;

// Number format
NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
NumberFormat germanyFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

System.out.println(usFormat.format(number));      // 1,234,567.891
System.out.println(germanyFormat.format(number)); // 1.234.567,891

// Currency format
NumberFormat usCurrency = NumberFormat.getCurrencyInstance(Locale.US);
System.out.println(usCurrency.format(number));    // $1,234,567.89

// Percent format
NumberFormat usPercent = NumberFormat.getPercentInstance(Locale.US);
System.out.println(usPercent.format(0.85));       // 85%
```

## DateFormat

`DateFormat` formats and parses dates and times according to locale.

```java
import java.text.DateFormat;
import java.util.Locale;
import java.util.Date;

Date now = new Date();

// Date format
DateFormat usFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
DateFormat franceFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);

System.out.println(usFormat.format(now));      // January 15, 2024
System.out.println(franceFormat.format(now)); // 15 janvier 2024

// Time format
DateFormat usTime = DateFormat.getTimeInstance(DateFormat.LONG, Locale.US);
System.out.println(usTime.format(now));       // 3:45:30 PM EST

// Date and time format
DateFormat usDateTime = DateFormat.getDateTimeInstance(
    DateFormat.LONG, DateFormat.LONG, Locale.US);
System.out.println(usDateTime.format(now));   // January 15, 2024 3:45:30 PM EST
```

## CurrencyFormat

`Currency` and `NumberFormat` work together for currency formatting.

```java
import java.util.Currency;
import java.util.Locale;
import java.text.NumberFormat;

// Get currency for locale
Currency usd = Currency.getInstance(Locale.US);
Currency eur = Currency.getInstance(Locale.GERMANY);

System.out.println(usd.getCurrencyCode());  // USD
System.out.println(usd.getDisplayName());   // US Dollar

// Format currency
NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
System.out.println(format.format(1234.56)); // $1,234.56

// Currency symbols
System.out.println(usd.getSymbol(Locale.US));   // $
System.out.println(eur.getSymbol(Locale.GERMANY)); // €
```

## Best Practices

### Use Resource Bundles

```java
// Good: Externalized strings
ResourceBundle bundle = ResourceBundle.getBundle("Messages");
String message = bundle.getString("welcome.message");

// Bad: Hardcoded strings
String message = "Welcome to our application!";
```

### Always Specify Locale

```java
// Good: Explicit locale
NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

// Bad: Uses default locale
NumberFormat format = NumberFormat.getCurrencyInstance();
```

### Handle Missing Resources

```java
try {
    ResourceBundle bundle = ResourceBundle.getBundle("Messages");
    String message = bundle.getString("key");
} catch (MissingResourceException e) {
    // Fallback to default
    String message = "Default message";
}
```

## Summary

- Locale represents a specific region/language
- ResourceBundle loads localized resources
- NumberFormat formats numbers by locale
- DateFormat formats dates/times by locale
- Currency handles currency formatting
- Always specify locale explicitly
- Use resource bundles for externalized strings
