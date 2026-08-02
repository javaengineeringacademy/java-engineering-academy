package academy.javaengineering.strings;

public class StringBasicsExample {
    
    // String creation
    private static final String CONSTANT = "Constant Value";
    private static final String POOL_STRING = "Pooled";
    
    public static void main(String[] args) {
        demonstrateStringCreation();
        demonstrateStringPool();
        demonstrateStringMethods();
        demonstrateStringBuilder();
        demonstrateStringBuffer();
        demonstrateRegex();
        demonstratePerformance();
    }
    
    private static void demonstrateStringCreation() {
        System.out.println("=== String Creation ===");
        
        // String literals (stored in pool)
        String literal1 = "Hello";
        String literal2 = "Hello";
        
        // String objects (stored on heap)
        String object1 = new String("Hello");
        String object2 = new String("Hello");
        
        // Empty string
        String empty = "";
        String emptyObj = new String();
        
        // Char array to string
        char[] chars = {'J', 'a', 'v', 'a'};
        String fromChars = new String(chars);
        String fromCharsValue = String.valueOf(chars);
        
        System.out.println("Literal 1: " + literal1);
        System.out.println("Literal 2: " + literal2);
        System.out.println("Object 1: " + object1);
        System.out.println("Object 2: " + object2);
        System.out.println("Empty: '" + empty + "'");
        System.out.println("Empty Object: '" + emptyObj + "'");
        System.out.println("From chars: " + fromChars);
        System.out.println("From chars value: " + fromCharsValue);
        
        // Reference comparison
        System.out.println("\nReference comparison:");
        System.out.println("literal1 == literal2: " + (literal1 == literal2));
        System.out.println("literal1 == object1: " + (literal1 == object1));
        System.out.println("object1 == object2: " + (object1 == object2));
        
        // Content comparison
        System.out.println("\nContent comparison:");
        System.out.println("literal1.equals(object1): " + literal1.equals(object1));
        System.out.println("object1.equals(object2): " + object1.equals(object2));
    }
    
    private static void demonstrateStringPool() {
        System.out.println("\n=== String Pool ===");
        
        String s1 = "Programming";
        String s2 = "Programming";
        String s3 = new String("Programming");
        String s4 = s3.intern();
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true - same pool reference
        System.out.println("s1 == s3: " + (s1 == s3)); // false - different objects
        System.out.println("s1 == s4: " + (s1 == s4)); // true - intern() returns pool reference
        
        // Dynamic string not in pool
        String dynamic = "Hello" + " " + "World";
        String staticStr = "Hello World";
        System.out.println("\nDynamic vs Static:");
        System.out.println("dynamic == staticStr: " + (dynamic == staticStr));
        
        // String concatenation with variables
        String prefix = "Hello";
        String full = prefix + " World";
        String literal = "Hello World";
        System.out.println("\nVariable concatenation:");
        System.out.println("full == literal: " + (full == literal));
    }
    
    private static void demonstrateStringMethods() {
        System.out.println("\n=== String Methods ===");
        
        String text = "  Hello, World!  ";
        
        // Basic methods
        System.out.println("Original: '" + text + "'");
        System.out.println("Length: " + text.length());
        System.out.println("Char at 0: " + text.charAt(0));
        System.out.println("Trimmed: '" + text.trim() + "'");
        System.out.println("To upper: " + text.toUpperCase());
        System.out.println("To lower: " + text.toLowerCase());
        
        // Search methods
        System.out.println("\nSearch methods:");
        System.out.println("Contains 'World': " + text.contains("World"));
        System.out.println("Starts with '  H': " + text.startsWith("  H"));
        System.out.println("Ends with '!  ': " + text.endsWith("!  "));
        System.out.println("Index of 'World': " + text.indexOf("World"));
        System.out.println("Last index of 'l': " + text.lastIndexOf('l'));
        
        // Substring methods
        System.out.println("\nSubstring methods:");
        System.out.println("Substring(2, 7): " + text.substring(2, 7));
        System.out.println("Substring(2): " + text.substring(2));
        
        // Replace methods
        System.out.println("\nReplace methods:");
        System.out.println("Replace 'World' with 'Java': " + text.replace("World", "Java"));
        System.out.println("Replace 'l' with 'L': " + text.replace('l', 'L'));
        
        // Split method
        System.out.println("\nSplit method:");
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        for (String fruit : fruits) {
            System.out.println("  Fruit: " + fruit);
        }
        
        // Comparison methods
        System.out.println("\nComparison methods:");
        String a = "Hello";
        String b = "hello";
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.equalsIgnoreCase(b): " + a.equalsIgnoreCase(b));
        System.out.println("a.compareTo(b): " + a.compareTo(b));
        
        // Utility methods
        System.out.println("\nUtility methods:");
        System.out.println("Value of 42: " + String.valueOf(42));
        System.out.println("Value of 3.14: " + String.valueOf(3.14));
        System.out.println("Is empty: " + "".isEmpty());
        System.out.println("Is blank: " + "   ".isBlank());
    }
    
    private static void demonstrateStringBuilder() {
        System.out.println("\n=== StringBuilder ===");
        
        // Basic operations
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Basic: " + sb.toString());
        
        // Chaining
        String result = new StringBuilder()
                .append("Java")
                .append(" ")
                .append("Programming")
                .append(" ")
                .append("Language")
                .toString();
        System.out.println("Chained: " + result);
        
        // Insert and delete
        StringBuilder sb2 = new StringBuilder("Hello World");
        sb2.insert(5, ",");
        System.out.println("After insert: " + sb2);
        
        sb2.delete(5, 6);
        System.out.println("After delete: " + sb2);
        
        sb2.replace(6, 11, "Java");
        System.out.println("After replace: " + sb2);
        
        // Reverse
        StringBuilder sb3 = new StringBuilder("Hello");
        System.out.println("Original: " + sb3);
        System.out.println("Reversed: " + sb3.reverse());
        
        // Capacity
        StringBuilder sb4 = new StringBuilder(100);
        System.out.println("Initial capacity: " + sb4.capacity());
        System.out.println("Initial length: " + sb4.length());
        
        sb4.append("Hello");
        System.out.println("After append - capacity: " + sb4.capacity());
        System.out.println("After append - length: " + sb4.length());
        
        // Delete char at index
        StringBuilder sb5 = new StringBuilder("Hello");
        sb5.deleteCharAt(1);
        System.out.println("After deleteCharAt: " + sb5);
        
        // Index of
        StringBuilder sb6 = new StringBuilder("Hello World");
        System.out.println("Index of 'World': " + sb6.indexOf("World"));
    }
    
    private static void demonstrateStringBuffer() {
        System.out.println("\n=== StringBuffer ===");
        
        // Basic operations
        StringBuffer sbf = new StringBuffer();
        sbf.append("Hello");
        sbf.append(" ");
        sbf.append("World");
        System.out.println("Basic: " + sbf.toString());
        
        // Thread-safe demonstration
        StringBuffer sharedBuffer = new StringBuffer();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedBuffer.append("A");
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedBuffer.append("B");
            }
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Thread-safe buffer length: " + sharedBuffer.length());
        System.out.println("Contains only A and B: " + sharedBuffer.toString().matches("[AB]+"));
    }
    
    private static void demonstrateRegex() {
        System.out.println("\n=== Regex ===");
        
        String text = "Hello World 123 Java 456 Programming";
        
        // Pattern matching
        System.out.println("Text: " + text);
        System.out.println("Matches \\w+: " + text.matches("\\w+"));
        System.out.println("Contains \\d+: " + text.matches(".*\\d+.*"));
        
        // Split with regex
        String[] words = text.split("\\s+");
        System.out.println("Split by whitespace:");
        for (String word : words) {
            System.out.println("  " + word);
        }
        
        // Replace with regex
        String numbers = text.replaceAll("[a-zA-Z]+", "");
        System.out.println("Numbers only: " + numbers.trim());
        
        // Email validation
        String email = "user@example.com";
        String emailRegex = "[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}";
        System.out.println("\nEmail validation:");
        System.out.println(email + " is valid: " + email.matches(emailRegex));
        
        // Phone validation
        String phone = "+1-555-123-4567";
        String phoneRegex = "\\+?\\d{1,3}-?\\d{3}-?\\d{3}-?\\d{4}";
        System.out.println(phone + " is valid: " + phone.matches(phoneRegex));
        
        // Compile pattern for reuse
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b\\w{5}\\b");
        java.util.regex.Matcher matcher = pattern.matcher(text);
        System.out.println("\n5-letter words:");
        while (matcher.find()) {
            System.out.println("  " + matcher.group());
        }
    }
    
    private static void demonstratePerformance() {
        System.out.println("\n=== Performance Comparison ===");
        
        int iterations = 100000;
        
        // String concatenation
        long start = System.currentTimeMillis();
        String concat = "";
        for (int i = 0; i < iterations; i++) {
            concat += "a";
        }
        long concatTime = System.currentTimeMillis() - start;
        
        // StringBuilder
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.currentTimeMillis() - start;
        
        // StringBuffer
        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a");
        }
        String bufferResult = sbf.toString();
        long bufferTime = System.currentTimeMillis() - start;
        
        System.out.println("Iterations: " + iterations);
        System.out.println("String concat time: " + concatTime + "ms");
        System.out.println("StringBuilder time: " + builderTime + "ms");
        System.out.println("StringBuffer time: " + bufferTime + "ms");
        
        // String.intern() performance
        System.out.println("\nIntern performance:");
        String[] strings = new String[10000];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = new String("String" + i);
        }
        
        start = System.currentTimeMillis();
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].intern();
        }
        long internTime = System.currentTimeMillis() - start;
        System.out.println("Intern 10000 strings: " + internTime + "ms");
    }
}
