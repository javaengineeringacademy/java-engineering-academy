package academy.javaengineering.strings;

public class StringBasics {

    private static final String CONSTANT = "Constant Value";

    public static void main(String[] args) {
        demonstrateStringCreation();
        demonstrateImmutability();
        demonstrateStringPool();
        demonstrateComparison();
    }

    private static void demonstrateStringCreation() {
        System.out.println("=== String Creation ===");

        String literal1 = "Hello";
        String literal2 = "Hello";
        String object1 = new String("Hello");
        String empty = "";
        char[] chars = {'J', 'a', 'v', 'a'};
        String fromChars = new String(chars);

        System.out.println("Literal 1: " + literal1);
        System.out.println("Literal 2: " + literal2);
        System.out.println("Object 1: " + object1);
        System.out.println("Empty: '" + empty + "'");
        System.out.println("From chars: " + fromChars);

        System.out.println("\nReference comparison:");
        System.out.println("literal1 == literal2: " + (literal1 == literal2));
        System.out.println("literal1 == object1: " + (literal1 == object1));
    }

    private static void demonstrateImmutability() {
        System.out.println("\n=== Immutability ===");

        String original = "Hello";
        String modified = original.concat(" World");

        System.out.println("Original: " + original);
        System.out.println("Modified: " + modified);
        System.out.println("Original unchanged: " + original.equals("Hello"));

        String upper = original.toUpperCase();
        System.out.println("After toUpperCase - original: " + original);
        System.out.println("New string: " + upper);

        String replaced = original.replace('l', 'L');
        System.out.println("After replace - original: " + original);
        System.out.println("New string: " + replaced);
    }

    private static void demonstrateStringPool() {
        System.out.println("\n=== String Pool ===");

        String s1 = "Programming";
        String s2 = "Programming";
        String s3 = new String("Programming");
        String s4 = s3.intern();

        System.out.println("s1 == s2: " + (s1 == s2));
        System.out.println("s1 == s3: " + (s1 == s3));
        System.out.println("s1 == s4: " + (s1 == s4));

        String dynamic = "Hello" + " " + "World";
        String staticStr = "Hello World";
        System.out.println("\nDynamic vs Static:");
        System.out.println("dynamic == staticStr: " + (dynamic == staticStr));

        String prefix = "Hello";
        String full = prefix + " World";
        System.out.println("full == staticStr: " + (full == staticStr));
    }

    private static void demonstrateComparison() {
        System.out.println("\n=== Comparison ===");

        String a = "Hello";
        String b = "hello";
        String c = new String("Hello");

        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.equalsIgnoreCase(b): " + a.equalsIgnoreCase(b));
        System.out.println("a.equals(c): " + a.equals(c));
        System.out.println("a.compareTo(b): " + a.compareTo(b));
        System.out.println("a.compareTo(c): " + a.compareTo(c));

        System.out.println("\nNull safety:");
        String nullStr = null;
        String nonNull = "Hello";
        System.out.println("Objects.equals(null, 'Hello'): " + java.util.Objects.equals(nullStr, nonNull));
        System.out.println("Objects.equals('Hello', 'Hello'): " + java.util.Objects.equals(nonNull, "Hello"));
    }
}
