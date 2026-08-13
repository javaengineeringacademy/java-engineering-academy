package academy.javaengineering.oop.internals;

public class ObjectIdentityInternals {

    static class Person {
        String name;
        Person(String name) { this.name = name; }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Identity Internals ===\n");

        // 1. Identity vs Equality
        System.out.println("--- Identity vs Equality ---");
        Person p1 = new Person("Alice");
        Person p2 = new Person("Alice");
        Person p3 = p1;

        System.out.println("p1 == p2: " + (p1 == p2)); // false
        System.out.println("p1 == p3: " + (p1 == p3)); // true
        System.out.println("p1.equals(p2): " + p1.equals(p2)); // depends on override

        // 2. == Operator
        System.out.println("\n--- == Operator ---");
        System.out.println("== compares references (identity)");
        System.out.println("Same memory address");
        System.out.println("Not content");

        // 3. equals() Method
        System.out.println("\n--- equals() Method ---");
        System.out.println("Default: compares identity (same as ==)");
        System.out.println("Override: compare content");
        System.out.println("Always override with hashCode()");

        // 4. hashCode() Contract
        System.out.println("\n--- hashCode() Contract ---");
        System.out.println("1. equal objects must have same hashCode");
        System.out.println("2. unequal objects can have same hashCode");
        System.out.println("3. consistent within execution");
    }
}
