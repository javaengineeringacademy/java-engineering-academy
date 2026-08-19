package academy.javaengineering.patterns.interpreter;

/**
 * Demonstrates the Interpreter pattern with arithmetic expressions.
 * Builds expression trees manually to show how non-terminal expressions
 * compose terminal expressions for evaluation.
 */
public class InterpreterExample {

    public static void main(String[] args) {
        System.out.println("=== Interpreter Pattern Demo ===\n");

        Expression three = new Number(3);
        Expression four = new Number(4);
        Expression five = new Number(5);
        Expression two = new Number(2);

        // 1. Simple addition: 3 + 4
        Expression addition = new Add(three, four);
        printExpression("(3 + 4)", addition);

        // 2. Subtraction: 10 - 3
        Expression ten = new Number(10);
        Expression subtraction = new Subtract(ten, three);
        printExpression("(10 - 3)", subtraction);

        // 3. Multiplication: 4 * 5
        Expression multiplication = new Multiply(four, five);
        printExpression("(4 * 5)", multiplication);

        // 4. Complex expression: (3 + 4) * 5
        //    The tree:
        //        *
        //       / \
        //      +   5
        //     / \
        //    3   4
        Expression complexMultiply = new Multiply(
                new Add(three, four),
                five
        );
        printExpression("(3 + 4) * 5", complexMultiply);

        // 5. Nested expression: (10 - 3) * (4 + 2)
        //    Tree:
        //        *
        //       / \
        //      -   +
        //     / \ / \
        //    10 3 4  2
        Expression nested = new Multiply(
                new Subtract(ten, three),
                new Add(four, two)
        );
        printExpression("(10 - 3) * (4 + 2)", nested);

        // 6. Deeply nested: ((3 + 4) * 5) - (2 * 3)
        Expression deeplyNested = new Subtract(
                new Multiply(
                        new Add(three, four),
                        five
                ),
                new Multiply(two, three)
        );
        printExpression("((3 + 4) * 5) - (2 * 3)", deeplyNested);

        System.out.println("\n=== Key Insight ===");
        System.out.println("Each Expression node evaluates itself by recursively");
        System.out.println("evaluating its children. The tree structure naturally");
        System.out.println("represents the grammar of arithmetic expressions.");
    }

    private static void printExpression(String label, Expression expression) {
        System.out.println("Expression: " + label);
        System.out.println("AST:        " + expression);
        System.out.println("Result:     " + expression.evaluate());
        System.out.println();
    }
}
