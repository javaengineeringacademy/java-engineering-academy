# Strings - Examples

```java
public class StringExamples {
    public static void main(String[] args) {
        // Immutability
        String s1 = "Hello";
        String s2 = s1.concat(" World");
        System.out.println(s1);  // still "Hello"
        System.out.println(s2);  // "Hello World"

        // String pool / interning
        String a = "java";
        String b = "java";
        String c = new String("java");
        System.out.println(a == b);          // true (same pool reference)
        System.out.println(a == c);          // false (different object)
        System.out.println(a.equals(c));     // true

        // Common methods
        String msg = "  Hello, Java!  ";
        System.out.println(msg.trim());                      // "Hello, Java!"
        System.out.println(msg.trim().toUpperCase());        // "HELLO, JAVA!"
        System.out.println(msg.trim().toLowerCase());        // "hello, java!"
        System.out.println(msg.trim().contains("Java"));     // true
        System.out.println(msg.trim().startsWith("Hello"));  // true
        System.out.println(msg.trim().charAt(0));            // 'H'
        System.out.println(msg.trim().indexOf("Java"));      // 8
        System.out.println(msg.trim().substring(2, 7));      // "Hello"

        // Split and join
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        String joined = String.join(" | ", fruits);
        System.out.println("Split: " + java.util.Arrays.toString(fruits));
        System.out.println("Joined: " + joined);

        // Format
        String name = "Alice";
        int age = 30;
        System.out.printf("Name: %s, Age: %d%n", name, age);

        // StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i).append(" ");
        System.out.println("Built: " + sb.toString().trim());
        System.out.println("Reversed: " + sb.reverse().toString());
    }
}
```
