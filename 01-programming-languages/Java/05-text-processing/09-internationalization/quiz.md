# Quiz: Internationalization

## Multiple Choice Questions

1. What does i18n stand for?
   - A) Internationalization
   - B) Internet
   - C) Interface
   - D) Integration

2. What does l10n stand for?
   - A) Localization
   - B) Language
   - C) Library
   - D) Layout

3. What class represents a locale?
   - A) Language
   - B) Locale
   - C) Country
   - D) Region

4. What is a Resource Bundle?
   - A) A file with translations
   - B) A collection of locale-specific resources
   - C) A class file
   - D) A configuration file

5. Which method formats numbers for a locale?
   - A) `NumberFormat.getInstance()`
   - B) `String.format()`
   - C) `Locale.format()`
   - D) `Number.toLocaleString()`

## True/False Questions

6. Locale.US and Locale.UK have the same formatting.
   - True / False

7. Resource bundles are cached by the JVM.
   - True / False

8. DateFormat can format dates for different locales.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Locale us = Locale.US;
Locale france = Locale.FRANCE;
System.out.println(us.getDisplayCountry());
System.out.println(france.getDisplayCountry());
```

10. What will this code print?
```java
double num = 1234567.89;
NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
NumberFormat deFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
System.out.println(usFormat.format(num));
System.out.println(deFormat.format(num));
```

## Answers

1. A - Internationalization (18 letters between i and n)
2. A - Localization (10 letters between l and n)
3. B - Locale class represents a locale
4. B - Resource bundle is a collection of locale-specific resources
5. A - NumberFormat.getInstance() formats numbers
6. False - US uses commas, UK uses periods for decimals
7. True - Resource bundles are cached for performance
8. True - DateFormat supports locale-specific formatting
9. Output:
```
United States
France
```
10. Output:
```
1,234,567.89
1.234.567,89
```
