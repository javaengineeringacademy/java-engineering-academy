package academy.javaengineering.strings;

public class StringMethods {

    public static void main(String[] args) {
        demonstrateLengthAndAccess();
        demonstrateSearchMethods();
        demonstrateSubstring();
        demonstrateModification();
        demonstrateSplitAndJoin();
        demonstrateUtilityMethods();
    }

    private static void demonstrateLengthAndAccess() {
        System.out.println("=== Length and Access ===");
        String text = "Hello, World!";

        System.out.println("Original: " + text);
        System.out.println("Length: " + text.length());
        System.out.println("Char at 0: " + text.charAt(0));
        System.out.println("Char at 4: " + text.charAt(4));
        System.out.println("First char: " + text.charAt(0));
        System.out.println("Last char: " + text.charAt(text.length() - 1));
    }

    private static void demonstrateSearchMethods() {
        System.out.println("\n=== Search Methods ===");
        String text = "Hello, World! Welcome to Java Programming";

        System.out.println("Contains 'World': " + text.contains("World"));
        System.out.println("Contains 'Python': " + text.contains("Python"));
        System.out.println("Starts with 'Hello': " + text.startsWith("Hello"));
        System.out.println("Ends with 'Programming': " + text.endsWith("Programming"));
        System.out.println("Index of 'World': " + text.indexOf("World"));
        System.out.println("Index of 'Python': " + text.indexOf("Python"));
        System.out.println("Last index of 'o': " + text.lastIndexOf('o'));

        int fromIndex = 5;
        System.out.println("Index of 'o' from 5: " + text.indexOf('o', fromIndex));
    }

    private static void demonstrateSubstring() {
        System.out.println("\n=== Substring ===");
        String text = "Hello, World!";

        System.out.println("Substring(7): " + text.substring(7));
        System.out.println("Substring(0, 5): " + text.substring(0, 5));
        System.out.println("Substring(7, 12): " + text.substring(7, 12));

        String email = "user@example.com";
        int atIndex = email.indexOf('@');
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);
        System.out.println("\nEmail parsing:");
        System.out.println("Username: " + username);
        System.out.println("Domain: " + domain);
    }

    private static void demonstrateModification() {
        System.out.println("\n=== Modification ===");
        String text = "Hello, World!";

        System.out.println("Replace 'World' with 'Java': " + text.replace("World", "Java"));
        System.out.println("Replace 'l' with 'L': " + text.replace('l', 'L'));
        System.out.println("To upper case: " + text.toUpperCase());
        System.out.println("To lower case: " + text.toLowerCase());
        System.out.println("Trim: '" + "  Hello  ".trim() + "'");

        String padded = "Hello";
        System.out.println("Replace spaces: '" + "  Hello  ".replace(" ", "") + "'");
    }

    private static void demonstrateSplitAndJoin() {
        System.out.println("\n=== Split and Join ===");

        String csv = "apple,banana,cherry,date";
        String[] fruits = csv.split(",");
        System.out.println("CSV: " + csv);
        System.out.println("Split result:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        String joined = String.join(" | ", fruits);
        System.out.println("Joined: " + joined);

        String spaceSeparated = "Hello World Java Programming";
        String[] words = spaceSeparated.split(" ");
        System.out.println("\nSpace separated: " + spaceSeparated);
        System.out.println("Words:");
        for (String word : words) {
            System.out.println("  " + word);
        }

        String multiSpace = "Hello   World   Java";
        String[] multiWords = multiSpace.split("\\s+");
        System.out.println("\nMulti-space: " + multiSpace);
        System.out.println("Split by \\s+:");
        for (String word : multiWords) {
            System.out.println("  " + word);
        }
    }

    private static void demonstrateUtilityMethods() {
        System.out.println("\n=== Utility Methods ===");

        System.out.println("Value of 42: " + String.valueOf(42));
        System.out.println("Value of 3.14: " + String.valueOf(3.14));
        System.out.println("Value of true: " + String.valueOf(true));

        System.out.println("Is empty (''): " + "".isEmpty());
        System.out.println("Is empty ('Hello'): " + "Hello".isEmpty());
        System.out.println("Is blank (''): " + "".isBlank());
        System.out.println("Is blank ('   '): " + "   ".isBlank());
        System.out.println("Is blank ('Hello'): " + "Hello".isBlank());

        System.out.println("Repeat 'Ha' 3 times: " + "Ha".repeat(3));
        System.out.println("Strip leading: '" + "  Hello".stripLeading() + "'");
        System.out.println("Strip trailing: '" + "Hello  ".stripTrailing() + "'");
    }
}
