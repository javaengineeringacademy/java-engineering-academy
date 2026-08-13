package academy.javaengineering.text.internals;

import java.util.regex.*;

public class RegexInternals {

    public static void main(String[] args) {
        System.out.println("=== Regex Internals ===\n");

        // 1. Basic Patterns
        System.out.println("--- Basic Patterns ---");
        String text = "Hello World 123";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        System.out.println("Find digits: " + matcher.find());
        System.out.println("Match: " + matcher.group());

        // 2. Pattern Syntax
        System.out.println("\n--- Pattern Syntax ---");
        System.out.println("\\d: digit");
        System.out.println("\\w: word character");
        System.out.println("\\s: whitespace");
        System.out.println(".: any character");
        System.out.println("*: zero or more");
        System.out.println("+: one or more");
        System.out.println("?: zero or one");

        // 3. Groups
        System.out.println("\n--- Groups ---");
        pattern = Pattern.compile("(\\w+) (\\w+)");
        matcher = pattern.matcher("Hello World");
        if (matcher.find()) {
            System.out.println("Group 0: " + matcher.group(0));
            System.out.println("Group 1: " + matcher.group(1));
            System.out.println("Group 2: " + matcher.group(2));
        }
    }
}
