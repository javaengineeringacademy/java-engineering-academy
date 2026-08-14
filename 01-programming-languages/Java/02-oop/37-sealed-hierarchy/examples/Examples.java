package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Sealed Hierarchy Patterns ===\n");

        // WHY: Sealed hierarchies enable exhaustive switch/pattern matching
        // INTERNAL: JVM verifies permitted classes at link time
        // ENGINEERING: Combine with records for powerful domain modeling

        Expr e1 = new Num(5);
        Expr e2 = new Num(3);
        Expr sum = new Add(e1, e2);
        Expr prod = new Mul(new Num(2), e1);

        System.out.println("5 + 3 = " + eval(sum));
        System.out.println("2 * 5 = " + eval(prod));

        // Exhaustive switch (Java 21+)
        System.out.println("Type of sum: " + typeStr(sum));
    }

    static double eval(Expr e) {
        return switch (e) {
            case Num n -> n.value();
            case Add a -> eval(a.left()) + eval(a.right());
            case Mul m -> eval(m.left()) * eval(m.right());
        };
    }

    static String typeStr(Expr e) {
        return switch (e) {
            case Num ignored -> "Num";
            case Add ignored -> "Add";
            case Mul ignored -> "Mul";
        };
    }
}

sealed interface Expr permits Num, Add, Mul {}
record Num(double value) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Mul(Expr left, Expr right) implements Expr {}
