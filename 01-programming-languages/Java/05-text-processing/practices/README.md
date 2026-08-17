# Strings Exercises

Practice Java String manipulation through hands-on exercises.

## Exercise 1: String Methods

**Problem Statement:**
Write a set of utility methods that process strings: capitalize the first letter of each word, reverse a string without using StringBuilder.reverse(), count vowel and consonant occurrences, and check if two strings are anagrams (case-insensitive).

**Expected Behavior:**
- `"hello world"` becomes `"Hello World"` with word capitalization.
- `"abcdef"` reversed is `"fedcba"` without using `StringBuilder.reverse()`.
- `"Hello"` has 2 vowels and 3 consonants.
- `"Listen"` and `"Silent"` are anagrams.
- Null and empty strings are handled gracefully.

**Hints:**
- Use `split(" ")` to get words, then `substring(0,1).toUpperCase()`.
- Loop from the end of the string to the beginning for reversal.
- Use a `Set<Character>` of vowels for efficient lookup.
- Sort characters and compare for anagram detection.

---

## Exercise 2: StringBuilder Operations

**Problem Statement:**
Use `StringBuilder` to build a CSV formatter that takes a 2D array of strings and produces properly formatted CSV output with quoted fields containing commas or quotes.

**Expected Behavior:**
- Each row is joined by commas.
- Fields containing commas are wrapped in double quotes.
- Fields containing double quotes have them escaped by doubling.
- A newline is added at the end of each row.
- The final string is a valid CSV format.

**Hints:**
- Check if a field contains `,` or `"` before deciding to quote it.
- Replace `"` with `""` for escaping inside quoted fields.
- Use `StringBuilder.append()` in a loop for efficiency.

---

## Exercise 3: Palindrome Checker

**Problem Statement:**
Write three different palindrome checking methods: a naive approach using string reversal, an efficient two-pointer approach, and a recursive approach. Compare their behavior with various inputs.

**Expected Behavior:**
- `"racecar"` is a palindrome.
- `"A man a plan a canal Panama"` is a palindrome (ignoring spaces and case).
- Each method returns the same result for all inputs.
- The two-pointer approach uses fewer comparisons.
- The recursive approach demonstrates the concept clearly.

**Hints:**
- For the naive approach, reverse the string and compare.
- For two pointers, start from both ends and move inward.
- For recursion, compare first and last characters, then recurse on the substring.
- Strip non-alphanumeric characters for phrase palindromes.

---

## Exercise 4: Anagram Detection

**Problem Statement:**
Implement two anagram detection algorithms: one using character frequency counting (HashMap) and another using sorted character arrays. Write a method to find all anagram substrings of a pattern within a given text.

**Expected Behavior:**
- `"listen"` and `"silent"` are anagrams.
- `"Dormitory"` and `"Dirty Room"` are anagrams (ignoring spaces and case).
- Finding all anagram start indices of `"ab"` in `"abxaba"` returns `[0, 3, 5]`.
- Both approaches return consistent results.

**Hints:**
- For frequency counting, increment counts for one string and decrement for the other.
- For sorting, sort both strings and compare directly.
- For the substring search, use a sliding window with frequency comparison.

---

## Exercise 5: String Compression

**Problem Statement:**
Implement run-length encoding string compression. Write a method that compresses `"aabcccccaaa"` into `"a2b1c5a3"`. If the compressed string is not smaller than the original, return the original string.

**Expected Behavior:**
- `"aabcccccaaa"` compresses to `"a2b1c5a3"` (10 vs 9 chars).
- `"abcdef"` returns `"abcdef"` since compression is not shorter.
- `"aabb"` returns `"aabb"` since `"a2b2"` is the same length.
- Handle single characters by appending count of 1.

**Hints:**
- Use `StringBuilder` to build the compressed string.
- Iterate through the string, counting consecutive characters.
- Compare lengths before and after compression.
- Use `charCount > 1` check to avoid `"a1"` for single characters.

---

## Exercise 6: String Pooling Behavior

**Problem Statement:**
Write a program that demonstrates Java String pool behavior. Create strings using literals, `new String()`, `String.intern()`, and `String.format()` to observe which are pooled and which are not.

**Expected Behavior:**
- String literals with the same value share the same reference.
- `new String("hello")` creates a new object even if the literal exists.
- `intern()` returns the canonical representation from the pool.
- `==` comparison distinguishes pooled vs non-pooled strings.
- `equals()` compares content regardless of pooling.

**Hints:**
- Use `==` to compare references (identity) and `.equals()` for content.
- Call `intern()` on a `new String()` and compare with the literal.
- Use `System.identityHashCode()` to verify object identity.
- Test with substring operations to see when substrings are pooled.

---

## Exercise 7: Regular Expressions

**Problem Statement:**
Write regex-based validators for: email addresses, phone numbers (US format), and URLs. Then write a regex search that finds and extracts all email addresses from a given text, replacing them with a masked version.

**Expected Behavior:**
- Valid emails like `"user@example.com"` pass validation.
- Valid phone numbers like `"(555) 123-4567"` pass validation.
- Valid URLs like `"https://www.example.com/path"` pass validation.
- Email extraction from text returns a list of all found emails.
- Replacing emails in text masks them as `"***@***.com"`.

**Hints:**
- Use `Pattern.compile()` and `Matcher.matches()` for validation.
- Use `Pattern.compile()` and `Matcher.find()` for extraction.
- Use `Matcher.replaceAll()` with a replacement lambda or `$1***$3` pattern.
- Build regex incrementally, testing each part separately.

---

## Exercise 8: String Performance Comparison

**Problem Statement:**
Benchmark and compare three approaches for building a large string from many small pieces: repeated `+` concatenation, `StringBuilder`, and `String.concat()`. Measure execution time for concatenations of 100, 1,000, and 10,000 iterations.

**Expected Behavior:**
- `+` concatenation becomes progressively slower with more iterations.
- `StringBuilder` maintains near-constant performance across all sizes.
- `String.concat()` performs moderately but degrades with many calls.
- The performance difference is measurable and visible in timing output.
- Results are printed in a formatted table for comparison.

**Hints:**
- Use `System.nanoTime()` for high-resolution timing.
- Run each test multiple times and average to reduce noise.
- Use `String.format("%-12s %15d ns", ...)` for formatted output.
- Warm up the JVM by running a few iterations before measuring.

---

## Exercise 9: Character Class Operations

**Problem Statement:**
Write utility methods using the `Character` class to classify characters, count digits in a string, create alternating-case strings, validate all-letter strings, get Roman numeral values, and count character frequency.

**Expected Behavior:**
- `isVowel('a')` and `isVowel('E')` return `true`, `isVowel('b')` returns `false`.
- `countDigits("abc123def456")` returns `6`.
- `alternatingCase("hello")` returns `"HeLlO"`.
- `isAllLetters("Hello")` returns `true`, `isAllLetters("Hello123")` returns `false`.
- `romanCharValue('V')` returns `5`, `romanCharValue('x')` returns `10`.
- `charFrequency("Hello World", 'l')` returns `3`.

**Hints:**
- Use `Character.toLowerCase()` / `Character.toUpperCase()` for case operations.
- Use `Character.isDigit()`, `Character.isLetter()` for classification.
- Use `switch` with `Character.toUpperCase()` for Roman numeral mapping.

---

## Exercise 10: Text Blocks (Java 15+)

**Problem Statement:**
Use text blocks (`"""`) to build SQL queries, JSON objects, HTML templates, and demonstrate `stripIndent()`, `formatted()`, and escape sequences.

**Expected Behavior:**
- SQL queries are clean multi-line strings.
- JSON is properly formatted with indentation.
- HTML templates contain title and body content.
- `stripIndent()` removes common leading whitespace.
- `formatted()` replaces `%s` and `%d` placeholders.

**Hints:**
- Use `"""` for multi-line string literals.
- Use `String.join()` for column lists.
- Use `.stripIndent()` to normalize indentation.
- Use `.formatted(args)` for placeholder substitution.

---

## Exercise 11: Charset and Encoding

**Problem Statement:**
Convert strings to and from byte arrays using different charsets, compare encoding equivalence, calculate byte lengths, safely decode malformed bytes, and detect UTF-8 BOM markers.

**Expected Behavior:**
- `toUtf8Bytes("Hello")` returns a 5-byte array starting with `72`.
- `fromUtf8Bytes(bytes)` correctly decodes back to the original string.
- ASCII and UTF-8 are equivalent for ASCII-only strings.
- `byteLength("Hello", UTF_8)` returns `5`.
- Malformed bytes are replaced with the replacement character.

**Hints:**
- Use `StandardCharsets.UTF_8` and `StandardCharsets.US_ASCII`.
- Use `CharsetDecoder` with `CodingErrorAction.REPLACE` for safe decoding.
- Check for UTF-8 BOM bytes: `0xEF, 0xBB, 0xBF`.

---

## Exercise 12: Unicode Operations

**Problem Statement:**
Work with Unicode code points, count code points in strings (including supplementary characters), detect supplementary characters, apply Unicode-aware uppercase, and identify emoji code points.

**Expected Behavior:**
- `getCodePoint('A')` returns `65`, `getCodePoint('\u00E9')` returns `233`.
- `codePointToString(65)` returns `"A"`.
- `countCodePoints` accounts for supplementary characters using `codePointCount()`.
- `hasSupplementaryCharacters("\uD83D\uDE00")` returns `true` for emoji.
- `unicodeUpperCase("\u00E9")` returns `"\u00C9"`.

**Hints:**
- Use `Character.codePointAt()` and `Character.toChars()`.
- Use `String.codePointCount()` for accurate counting.
- Supplementary characters have char values above `0xFFFF` (above BMP).
- Check common emoji Unicode ranges: `0x1F600-0x1F64F`, `0x1F300-0x1F5FF`.

---

## Exercise 13: Internationalization (i18n)

**Problem Statement:**
Format numbers, currencies, dates, and percentages for different locales. Parse formatted numbers back to values and apply locale-specific case conversion rules.

**Expected Behavior:**
- US locale formats `1234567` with commas, German with dots.
- Currency formatting includes the correct symbol (`$`, etc.).
- Date formatting uses locale-appropriate patterns.
- Percentage formatting: `0.756` becomes `"75.6%"`.
- Turkish locale uppercases `"i"` to `"\u0130"` (capital I with dot).

**Hints:**
- Use `NumberFormat.getNumberInstance(locale)` for number formatting.
- Use `NumberFormat.getCurrencyInstance(locale)` for currency.
- Use `DateFormat.getDateInstance(DateFormat.LONG, locale)` for dates.
- Use `String.toUpperCase(locale)` for locale-specific case conversion.
