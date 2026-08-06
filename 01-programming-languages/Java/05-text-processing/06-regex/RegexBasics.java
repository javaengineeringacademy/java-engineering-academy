import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class RegexBasics {
    public static void main(String[] args) {
        // 1. Basic Pattern and Matcher
        basicPatternMatcher();

        // 2. Common regex patterns
        commonPatterns();

        // 3. Quantifiers
        quantifiersDemo();

        // 4. Character classes
        characterClasses();

        // 5. Groups and capturing
        groupsAndCapturing();

        // 6. Named groups
        namedGroups();

        // 7. Lookahead and lookbehind
        lookaheadLookbehind();

        // 8. find(), matches(), lookingAt()
        matchingMethods();

        // 9. String.split() with regex
        splitWithRegex();

        // 10. String.replaceAll() with regex
        replaceAllWithRegex();
    }

    // 1. Basic Pattern and Matcher
    static void basicPatternMatcher() {
        System.out.println("=== Basic Pattern and Matcher ===");

        String text = "Hello World! Hello Java!";
        String regex = "Hello";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println("Found: " + matcher.group() + " at index " + matcher.start());
        }

        // Case-insensitive matching
        Pattern caseInsensitive = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = caseInsensitive.matcher(text);
        while (matcher2.find()) {
            System.out.println("Case-insensitive found: " + matcher2.group());
        }
        System.out.println();
    }

    // 2. Common regex patterns
    static void commonPatterns() {
        System.out.println("=== Common Regex Patterns ===");

        // Email pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String[] emails = {"user@example.com", "invalid@com", "test.email@domain.org"};
        for (String email : emails) {
            System.out.println(email + " is " + (email.matches(emailRegex) ? "valid" : "invalid"));
        }

        // Phone number pattern
        String phoneRegex = "\\d{3}-\\d{3}-\\d{4}";
        String phone = "123-456-7890";
        System.out.println("\nPhone: " + phone + " matches: " + phone.matches(phoneRegex));

        // URL pattern
        String urlRegex = "https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        String url = "https://www.example.com";
        System.out.println("URL: " + url + " matches: " + url.matches(urlRegex));
        System.out.println();
    }

    // 3. Quantifiers
    static void quantifiersDemo() {
        System.out.println("=== Quantifiers ===");

        String text = "aab abc aabc aAbc";

        // * - zero or more
        System.out.println("'a*' matches:");
        Matcher matcher1 = Pattern.compile("a*").matcher(text);
        while (matcher1.find()) {
            System.out.print("[" + matcher1.group() + "] ");
        }
        System.out.println();

        // + - one or more
        System.out.println("'a+b' matches:");
        Matcher matcher2 = Pattern.compile("a+b").matcher(text);
        while (matcher2.find()) {
            System.out.print("[" + matcher2.group() + "] ");
        }
        System.out.println();

        // ? - zero or one
        System.out.println("'a?b' matches:");
        Matcher matcher3 = Pattern.compile("a?b").matcher(text);
        while (matcher3.find()) {
            System.out.print("[" + matcher3.group() + "] ");
        }
        System.out.println();

        // {n} - exactly n times
        System.out.println("'a{2}' matches:");
        Matcher matcher4 = Pattern.compile("a{2}").matcher(text);
        while (matcher4.find()) {
            System.out.print("[" + matcher4.group() + "] ");
        }
        System.out.println();

        // {n,m} - between n and m times
        System.out.println("'a{1,3}b' matches:");
        Matcher matcher5 = Pattern.compile("a{1,3}b").matcher(text);
        while (matcher5.find()) {
            System.out.print("[" + matcher5.group() + "] ");
        }
        System.out.println();
        System.out.println();
    }

    // 4. Character classes
    static void characterClasses() {
        System.out.println("=== Character Classes ===");

        String text = "abc def 123 ABC";

        // [abc] - matches a, b, or c
        System.out.println("'[abc]+':");
        Matcher matcher1 = Pattern.compile("[abc]+").matcher(text);
        while (matcher1.find()) {
            System.out.print("[" + matcher1.group() + "] ");
        }
        System.out.println();

        // [^abc] - matches anything except a, b, or c
        System.out.println("'[^abc]+':");
        Matcher matcher2 = Pattern.compile("[^abc]+").matcher(text);
        while (matcher2.find()) {
            System.out.print("[" + matcher2.group() + "] ");
        }
        System.out.println();

        // [a-z] - matches lowercase letters
        System.out.println("'[a-z]+':");
        Matcher matcher3 = Pattern.compile("[a-z]+").matcher(text);
        while (matcher3.find()) {
            System.out.print("[" + matcher3.group() + "] ");
        }
        System.out.println();

        // [A-Z] - matches uppercase letters
        System.out.println("'[A-Z]+':");
        Matcher matcher4 = Pattern.compile("[A-Z]+").matcher(text);
        while (matcher4.find()) {
            System.out.print("[" + matcher4.group() + "] ");
        }
        System.out.println();

        // [0-9] - matches digits
        System.out.println("'[0-9]+':");
        Matcher matcher5 = Pattern.compile("[0-9]+").matcher(text);
        while (matcher5.find()) {
            System.out.print("[" + matcher5.group() + "] ");
        }
        System.out.println();

        // \\w - word character [a-zA-Z0-9_]
        System.out.println("'\\w+':");
        Matcher matcher6 = Pattern.compile("\\w+").matcher(text);
        while (matcher6.find()) {
            System.out.print("[" + matcher6.group() + "] ");
        }
        System.out.println();

        // \\d - digit [0-9]
        System.out.println("'\\d+':");
        Matcher matcher7 = Pattern.compile("\\d+").matcher(text);
        while (matcher7.find()) {
            System.out.print("[" + matcher7.group() + "] ");
        }
        System.out.println();
        System.out.println();
    }

    // 5. Groups and capturing
    static void groupsAndCapturing() {
        System.out.println("=== Groups and Capturing ===");

        String text = "John Smith, 25, Engineer";
        String regex = "(\\w+)\\s(\\w+),\\s(\\d+),\\s(\\w+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            System.out.println("Full match: " + matcher.group(0));
            System.out.println("First name: " + matcher.group(1));
            System.out.println("Last name: " + matcher.group(2));
            System.out.println("Age: " + matcher.group(3));
            System.out.println("Job: " + matcher.group(4));
        }

        // Non-capturing groups
        String text2 = "2024-01-15";
        String regex2 = "(?:\\d{4})-(\\d{2})-(\\d{2})";
        Matcher matcher2 = Pattern.compile(regex2).matcher(text2);
        if (matcher2.matches()) {
            System.out.println("\nNon-capturing group example:");
            System.out.println("Full match: " + matcher2.group(0));
            System.out.println("Month: " + matcher2.group(1));
            System.out.println("Day: " + matcher2.group(2));
        }
        System.out.println();
    }

    // 6. Named groups
    static void namedGroups() {
        System.out.println("=== Named Groups ===");

        String text = "2024-01-15";
        String regex = "(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            System.out.println("Year: " + matcher.group("year"));
            System.out.println("Month: " + matcher.group("month"));
            System.out.println("Day: " + matcher.group("day"));
        }
        System.out.println();
    }

    // 7. Lookahead and lookbehind
    static void lookaheadLookbehind() {
        System.out.println("=== Lookahead and Lookbehind ===");

        String text = "cat123 dog456 cat789";

        // Positive lookahead: cat followed by digits
        System.out.println("Positive lookahead (cat followed by \\d):");
        Matcher matcher1 = Pattern.compile("cat(?=\\d)").matcher(text);
        while (matcher1.find()) {
            System.out.print("[" + matcher1.group() + "] ");
        }
        System.out.println();

        // Negative lookahead: cat not followed by digits
        System.out.println("Negative lookahead (cat not followed by \\d):");
        Matcher matcher2 = Pattern.compile("cat(?!\\d)").matcher(text);
        while (matcher2.find()) {
            System.out.print("[" + matcher2.group() + "] ");
        }
        System.out.println();

        String text2 = "123cat 456cat 789dog";

        // Positive lookbehind: digits before cat
        System.out.println("Positive lookbehind (\\d before cat):");
        Matcher matcher3 = Pattern.compile("(?<=\\d)cat").matcher(text2);
        while (matcher3.find()) {
            System.out.print("[" + matcher3.group() + "] ");
        }
        System.out.println();

        // Negative lookbehind: cat not preceded by digits
        System.out.println("Negative lookbehind (cat not preceded by \\d):");
        Matcher matcher4 = Pattern.compile("(?<!\\d)cat").matcher(text2);
        while (matcher4.find()) {
            System.out.print("[" + matcher4.group() + "] ");
        }
        System.out.println();
        System.out.println();
    }

    // 8. find(), matches(), lookingAt()
    static void matchingMethods() {
        System.out.println("=== Matching Methods ===");

        String text = "abc123def456";
        String regex = "\\d+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // find() - searches for next match
        System.out.println("find() method:");
        matcher.reset();
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group() + " at index " + matcher.start());
        }

        // matches() - checks if entire string matches
        System.out.println("\nmatches() method:");
        System.out.println("'\\d+'.matches(\"123\"): " + "123".matches("\\d+"));
        System.out.println("'\\d+'.matches(\"abc123\"): " + "abc123".matches("\\d+"));

        // lookingAt() - checks if pattern matches at beginning
        System.out.println("\nlookingAt() method:");
        matcher.reset();
        System.out.println("lookingAt 'abc' at start of text: " + matcher.lookingAt());
        matcher.reset();
        System.out.println("lookingAt '123' at start of text: " + Pattern.compile("123").matcher(text).lookingAt());
        System.out.println();
    }

    // 9. String.split() with regex
    static void splitWithRegex() {
        System.out.println("=== String.split() with Regex ===");

        // Split by comma
        String csv = "apple,banana,cherry,date";
        String[] fruits = csv.split(",");
        System.out.println("Split by comma:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        // Split by whitespace
        String sentence = "Hello   World\tJava";
        String[] words = sentence.split("\\s+");
        System.out.println("\nSplit by whitespace:");
        for (String word : words) {
            System.out.println("  " + word);
        }

        // Split with limit
        String data = "one:two:three:four";
        String[] parts = data.split(":", 2);
        System.out.println("\nSplit with limit 2:");
        for (String part : parts) {
            System.out.println("  " + part);
        }
        System.out.println();
    }

    // 10. String.replaceAll() with regex
    static void replaceAllWithRegex() {
        System.out.println("=== String.replaceAll() with Regex ===");

        String text = "Hello World! Hello Java!";

        // Replace all occurrences
        String result1 = text.replaceAll("Hello", "Hi");
        System.out.println("Replace 'Hello' with 'Hi': " + result1);

        // Replace with backreference
        String result2 = text.replaceAll("(Hello) (World)", "$2 $1");
        System.out.println("Swap 'Hello World': " + result2);

        // Replace digits with #
        String result3 = "abc123def456".replaceAll("\\d", "#");
        System.out.println("Replace digits: " + result3);

        // Replace with case-insensitive flag
        String result4 = text.replaceAll("(?i)hello", "Hi");
        System.out.println("Case-insensitive replace: " + result4);
    }
}
