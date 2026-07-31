package academy.javaengineering.oop.operators;

/**
 * Demonstrates logical operators (&&, ||, !).
 */
public final class LogicalOperators {

    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;

        System.out.println("=== Logical Operators ===");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println();

        // Basic logical operations
        System.out.println("--- Basic Operations ---");
        System.out.println("a && b: " + (a && b)); // false
        System.out.println("a || b: " + (a || b)); // true
        System.out.println("!a: " + (!a));         // false
        System.out.println("!b: " + (!b));         // true

        // Truth tables
        System.out.println("\n--- Truth Table for && ---");
        System.out.println("true && true   = " + (true && true));
        System.out.println("true && false  = " + (true && false));
        System.out.println("false && true  = " + (false && true));
        System.out.println("false && false = " + (false && false));

        System.out.println("\n--- Truth Table for || ---");
        System.out.println("true || true   = " + (true || true));
        System.out.println("true || false  = " + (true || false));
        System.out.println("false || true  = " + (false || true));
        System.out.println("false || false = " + (false || false));

        // Short-circuit evaluation demo
        System.out.println("\n--- Short-Circuit Evaluation ---");
        boolean called = false;
        boolean result;

        // && stops at first false
        result = false && expensiveOperation("&&");
        System.out.println("false && expensive(): " + result); // expensiveOperation() NOT called

        // || stops at first true
        result = true || expensiveOperation("||");
        System.out.println("true || expensive(): " + result); // expensiveOperation() NOT called

        // Non-short-circuit operators (&, |) - evaluate BOTH sides
        System.out.println("\n--- Non-Short-Circuit (&, |) ---");
        called = false;
        result = false & expensiveOperation("&");
        System.out.println("false & expensive(): " + result); // expensiveOperation() CALLED!

        called = false;
        result = true | expensiveOperation("|");
        System.out.println("true | expensive(): " + result);  // expensiveOperation() CALLED!

        // XOR (exclusive OR)
        System.out.println("\n--- XOR (^) ---");
        System.out.println("true ^ true   = " + (true ^ true));   // false
        System.out.println("true ^ false  = " + (true ^ false));  // true
        System.out.println("false ^ true  = " + (false ^ true));  // true
        System.out.println("false ^ false = " + (false ^ false)); // false

        // Precedence: ! > && > ||
        System.out.println("\n--- Precedence: ! > && > || ---");
        boolean x = true, y = false, z = true;
        boolean result = !x && y || z; // (!x) && y || z → false && false || true → false || true → true
        System.out.println("!true && false || true = " + result); // true
    }

    /**
     * Simulates an expensive operation with side effect (prints to console).
     */
    private static boolean expensiveOperation(String operator) {
        System.out.println("  >>> expensiveOperation() called for " + operator + " <<<");
        return true;
    }
}