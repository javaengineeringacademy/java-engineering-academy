package com.javaacademy.sprint1.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * SmartCalculator - A menu-driven calculator application demonstrating Sprint 1 concepts.
 *
 * <p>Features:
 * <ul>
 *   <li>Basic operations (+, -, *, /, %)</li>
 *   <li>Advanced operations (power, square root, factorial)</li>
 *   <li>History tracking (last 10 calculations)</li>
 *   <li>Expression evaluation (e.g., "3 + 4 * 2")</li>
 *   <li>Menu-driven CLI interface</li>
 *   <li>Comprehensive error handling</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b> Like a scientific calculator with memory - 
 * you can perform calculations, see history, and evaluate complex expressions.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class CalculatorApp {

    private static final int MAX_HISTORY = 10;
    private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);

    private final Deque<Calculation> history = new ArrayDeque<>(MAX_HISTORY);
    private final Scanner scanner = new Scanner(System.in);

    private CalculatorApp() {}

    /**
     * Main entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new CalculatorApp().run();
    }

    /**
     * Runs the calculator application loop.
     */
    public void run() {
        System.out.println("=== Smart Calculator ===");
        System.out.println("Type 'help' for commands, 'quit' to exit\n");

        boolean running = true;
        while (running) {
            try {
                System.out.print("calc> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                running = processCommand(input.toLowerCase());

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    /**
     * Processes a user command.
     *
     * @param command the user input
     * @return true to continue, false to quit
     */
    private boolean processCommand(String command) {
        return switch (command) {
            case "quit", "exit", "q" -> false;
            case "help", "h", "?" -> { showHelp(); yield true; }
            case "history", "hist" -> { showHistory(); yield true; }
            case "clear" -> { clearHistory(); yield true; }
            case "basic" -> { runBasicOperation(); yield true; }
            case "advanced" -> { runAdvancedOperation(); yield true; }
            case "eval", "evaluate" -> { runExpressionEvaluation(); yield true; }
            default -> {
                // Try to evaluate as expression
                try {
                    BigDecimal result = evaluateExpression(command);
                    addToHistory(command, result);
                    System.out.println("Result: " + result.stripTrailingZeros().toPlainString());
                } catch (Exception e) {
                    System.err.println("Unknown command: " + command + " (type 'help' for commands)");
                }
                yield true;
            }
        };
    }

    /**
     * Runs a basic arithmetic operation.
     */
    private void runBasicOperation() {
        System.out.println("--- Basic Operation ---");
        System.out.println("Operators: +, -, *, /, %");
        System.out.print("Enter expression (e.g., 10 + 5): ");
        
        String input = scanner.nextLine().trim();
        try {
            BigDecimal result = evaluateExpression(input);
            addToHistory(input, result);
            System.out.println("Result: " + result.stripTrailingZeros().toPlainString());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Runs an advanced operation.
     */
    private void runAdvancedOperation() {
        System.out.println("--- Advanced Operations ---");
        System.out.println("1. Power (a^b)");
        System.out.println("2. Square Root (√a)");
        System.out.println("3. Factorial (a!)");
        System.out.print("Choose operation (1-3): ");
        
        String choice = scanner.nextLine().trim();
        try {
            String expression;
            BigDecimal result;
            
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter base: ");
                    String base = scanner.nextLine().trim();
                    System.out.print("Enter exponent: ");
                    String exp = scanner.nextLine().trim();
                    expression = "pow(" + base + ", " + exp + ")";
                    result = power(new BigDecimal(base), new BigDecimal(exp));
                }
                case "2" -> {
                    System.out.print("Enter number: ");
                    String num = scanner.nextLine().trim();
                    expression = "sqrt(" + num + ")";
                    result = sqrt(new BigDecimal(num));
                }
                case "3" -> {
                    System.out.print("Enter integer (0-20): ");
                    String num = scanner.nextLine().trim();
                    int n = Integer.parseInt(num.trim());
                    if (n < 0 || n > 20) throw new IllegalArgumentException("Factorial: 0-20 only");
                    expression = num + "!";
                    result = factorial(n);
                }
                default -> {
                    System.err.println("Invalid choice");
                    return;
                }
            }
            
            addToHistory(expression, result);
            System.out.println("Result: " + result.stripTrailingZeros().toPlainString());
            
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Runs expression evaluation.
     */
    private void runExpressionEvaluation() {
        System.out.println("--- Expression Evaluation ---");
        System.out.println("Supported: +, -, *, /, %, parentheses");
        System.out.println("Example: (3 + 4) * 2 - 10 / 5");
        System.out.print("Enter expression: ");
        
        String input = scanner.nextLine().trim();
        try {
            BigDecimal result = evaluateExpression(input);
            addToHistory(input, result);
            System.out.println("Result: " + result.stripTrailingZeros().toPlainString());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Evaluates a mathematical expression.
     * Supports +, -, *, /, %, parentheses, and operator precedence.
     *
     * @param expression the expression to evaluate
     * @return the result
     * @throws IllegalArgumentException if expression is invalid
     */
    public static BigDecimal evaluateExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        // Tokenize and convert to postfix (Shunting Yard algorithm)
        List<String> tokens = tokenize(expression);
        List<String> postfix = toPostfix(tokens);
        return evaluatePostfix(postfix);
    }

    /**
     * Tokenizes an expression into numbers, operators, and parentheses.
     */
    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isWhitespace(c)) {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }
                continue;
            }
            
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }
                
                if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' 
                    || c == '(' || c == ')' || c == '^') {
                    tokens.add(String.valueOf(c));
                } else {
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
        }
        
        if (number.length() > 0) {
            tokens.add(number.toString());
        }
        
        return tokens;
    }

    /**
     * Converts infix tokens to postfix using Shunting Yard algorithm.
     */
    private static List<String> toPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();
        
        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && isOperator(operators.peek()) 
                       && precedence(operators.peek()) >= precedence(token)
                       && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop(); // Remove '('
            }
        }
        
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(op);
        }
        
        return output;
    }

    /**
     * Evaluates a postfix expression.
     */
    private static BigDecimal evaluatePostfix(List<String> postfix) {
        Deque<BigDecimal> stack = new ArrayDeque<>();
        
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(new BigDecimal(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: insufficient operands");
                }
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                BigDecimal result = applyOperator(a, b, token);
                stack.push(result);
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        
        return stack.pop();
    }

    /**
     * Applies an operator to two operands.
     */
    private static BigDecimal applyOperator(BigDecimal a, BigDecimal b, String operator) {
        return switch (operator) {
            case "+" -> a.add(b, MATH_CONTEXT);
            case "-" -> a.subtract(b, MATH_CONTEXT);
            case "*" -> a.multiply(b, MATH_CONTEXT);
            case "/" -> {
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a.divide(b, MATH_CONTEXT);
            }
            case "%" -> {
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                yield a.remainder(b, MATH_CONTEXT);
            }
            case "^" -> power(a, b);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    /**
     * Computes a^b using exponentiation by squaring.
     */
    private static BigDecimal power(BigDecimal base, BigDecimal exponent) {
        if (exponent.scale() != 0) {
            throw new IllegalArgumentException("Exponent must be integer");
        }
        
        int exp = exponent.intValueExact();
        if (exp < 0) {
            return BigDecimal.ONE.divide(power(base, BigDecimal.valueOf(-exp)), MATH_CONTEXT);
        }
        
        BigDecimal result = BigDecimal.ONE;
        BigDecimal current = base;
        int n = exp;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = result.multiply(current, MATH_CONTEXT);
            }
            current = current.multiply(current, MATH_CONTEXT);
            n >>= 1;
        }
        return result;
    }

    /**
     * Computes square root using Newton's method.
     */
    private static BigDecimal sqrt(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Square root of negative number");
        }
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal x = value;
        BigDecimal y = value.add(BigDecimal.ONE).divide(BigDecimal.valueOf(2), MATH_CONTEXT);
        
        while (y.compareTo(x) < 0) {
            x = y;
            y = value.divide(x, MATH_CONTEXT).add(x).divide(BigDecimal.valueOf(2), MATH_CONTEXT);
        }
        return x;
    }

    /**
     * Computes factorial.
     */
    private static BigDecimal factorial(int n) {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigDecimal.valueOf(i), MATH_CONTEXT);
        }
        return result;
    }

    /**
     * Checks if token is a number.
     */
    private static boolean isNumber(String token) {
        try {
            new BigDecimal(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if token is an operator.
     */
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") 
            || token.equals("/") || token.equals("%") || token.equals("^");
    }

    /**
     * Returns operator precedence.
     */
    private static int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/", "%" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    /**
     * Adds calculation to history.
     */
    private void addToHistory(String expression, BigDecimal result) {
        if (history.size() >= MAX_HISTORY) {
            history.removeLast();
        }
        history.addFirst(new Calculation(expression, result));
    }

    /**
     * Shows calculation history.
     */
    private void showHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty");
            return;
        }
        
        System.out.println("--- Calculation History (last " + MAX_HISTORY + ") ---");
        int i = 1;
        for (Calculation calc : history) {
            System.out.printf("%d. %s = %s%n", i++, calc.expression(), 
                calc.result().stripTrailingZeros().toPlainString());
        }
    }

    /**
     * Clears calculation history.
     */
    private void clearHistory() {
        history.clear();
        System.out.println("History cleared");
    }

    /**
     * Shows help message.
     */
    private void showHelp() {
        System.out.println("""
            === Smart Calculator Help ===
            
            Commands:
              help, h, ?      Show this help
              quit, exit, q   Exit the calculator
              history, hist   Show calculation history
              clear           Clear history
              basic           Basic arithmetic (+, -, *, /, %)
              advanced        Advanced operations (power, sqrt, factorial)
              eval, evaluate  Evaluate expression
              
            Expressions:
              Directly type expressions like:
                10 + 5
                (3 + 4) * 2
                10 / 3 + 2 * 5
                2^10
                
            Operators:
              +  Addition
              -  Subtraction
              *  Multiplication
              /  Division
              %  Modulo/remainder
              ^  Power (e.g., 2^10)
              () Parentheses for grouping
              
            Examples:
              calc> 10 + 5 * 2
              Result: 20
              
              calc> (3 + 4) * 2
              Result: 14
              
              calc> 2^10
              Result: 1024
            """);
    }

    /**
     * Record for calculation history.
     */
    private record Calculation(String expression, BigDecimal result) {}
}