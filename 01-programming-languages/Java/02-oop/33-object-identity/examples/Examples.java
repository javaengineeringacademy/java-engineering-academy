package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Object Identity Patterns ===\n");

        // WHY: == checks reference equality, .equals() checks logical equality
        // INTERNAL: == compares memory addresses, equals() compares content
        // ENGINEERING: Always override equals/hashCode for value-like objects

        String s1 = new String("hello");
        String s2 = new String("hello");
        String s3 = "hello";
        String s4 = "hello";

        System.out.println("s1 == s2: " + (s1 == s2));           // false (different objects)
        System.out.println("s1.equals(s2): " + s1.equals(s2));   // true (same content)
        System.out.println("s3 == s4: " + (s3 == s4));           // true (string pool)
        System.out.println("s1 == s3: " + (s1 == s3));           // false

        // Identity vs Equality
        User u1 = new User(1, "Alice");
        User u2 = new User(1, "Alice");
        System.out.println("\nu1 == u2: " + (u1 == u2));
        System.out.println("u1.equals(u2): " + u1.equals(u2));

        // TRADE-OFF: Reference vs value equality
        // Reference: fast, useful for singleton checks
        // Value: logical, useful for collections
    }
}

class User {
    final int id;
    final String name;

    User(int id, String name) { this.id = id; this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return id == u.id && name.equals(u.name);
    }

    @Override
    public int hashCode() { return java.util.Objects.hash(id, name); }
}
