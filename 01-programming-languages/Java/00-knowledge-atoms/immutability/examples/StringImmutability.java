public class StringImmutability {
    public static void main(String[] args) {
        // Example 1: String concat does not modify original
        String s = "Hello";
        System.out.println("Original: " + s);

        s.concat(" World");
        System.out.println("After concat (not assigned): " + s); // Still "Hello"

        s = s.concat(" World");
        System.out.println("After concat (assigned): " + s); // "Hello World"

        // Example 2: String methods return new strings
        String upper = s.toUpperCase();
        System.out.println("toUpperCase: " + upper);
        System.out.println("Original unchanged: " + s);

        String replaced = s.replace('l', 'x');
        System.out.println("replace: " + replaced);
        System.out.println("Original unchanged: " + s);

        // Example 3: String pool - same literal shares object
        String a = "Hello";
        String b = "Hello";
        System.out.println("a == b (pool): " + (a == b)); // true

        String c = new String("Hello");
        System.out.println("a == c (new): " + (a == c)); // false
        System.out.println("a.equals(c): " + a.equals(c)); // true

        // Example 4: String concatenation in loop is inefficient
        String result = "";
        for (int i = 1; i <= 5; i++) {
            result += i + " "; // Creates new String each iteration
        }
        System.out.println("Loop result: " + result.trim());

        // Example 5: Use StringBuilder for loop concatenation
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i).append(" ");
        }
        String efficientResult = sb.toString();
        System.out.println("StringBuilder result: " + efficientResult.trim());
    }
}
