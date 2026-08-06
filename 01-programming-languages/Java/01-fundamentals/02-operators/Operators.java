package academy.javaengineering.fundamentals;

/**
 * Operators in Java
 *
 * This file covers:
 * - Arithmetic operators (+, -, *, /, %)
 * - Relational operators (==, !=, >, <, >=, <=)
 * - Logical operators (&&, ||, !)
 * - Assignment operators (=, +=, -=, *=, /=)
 * - Ternary operator (? :)
 * - Bitwise operators (&, |, ^, ~, <<, >>)
 */
public class Operators {

    public static void main(String[] args) {

        // =========================================================
        // 1. ARITHMETIC OPERATORS
        // =========================================================
        System.out.println("=== Arithmetic Operators ===");

        int a = 17;
        int b = 5;

        int sum = a + b;       // Addition: 22
        int diff = a - b;      // Subtraction: 12
        int product = a * b;   // Multiplication: 85
        int quotient = a / b;  // Division: 3 (integer division truncates)
        int remainder = a % b; // Modulus: 2 (remainder of 17 / 5)

        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a + b = " + sum);        // 22
        System.out.println("a - b = " + diff);       // 12
        System.out.println("a * b = " + product);    // 85
        System.out.println("a / b = " + quotient);   // 3 (not 3.4!)
        System.out.println("a % b = " + remainder);  // 2

        // Floating-point division
        double preciseQuotient = (double) a / b;
        System.out.println("(double)a / b = " + preciseQuotient); // 3.4

        // Increment and decrement operators
        int x = 10;
        System.out.println("\nOriginal x = " + x);

        x++;  // Post-increment: x becomes 11
        System.out.println("x++ -> x = " + x);  // 11

        x--;  // Post-decrement: x becomes 10
        System.out.println("x-- -> x = " + x);  // 10

        // Pre-increment vs post-increment
        int y = 5;
        int result1 = y++;  // Assigns 5 first, then increments y to 6
        int result2 = ++y;  // Increments y to 7 first, then assigns 7
        System.out.println("\ny starts at 5:");
        System.out.println("y++ assigns " + result1 + ", then y becomes " + y);
        System.out.println("++y makes y " + y + ", then assigns " + result2);

        // =========================================================
        // 2. RELATIONAL OPERATORS
        // =========================================================
        System.out.println("\n=== Relational Operators ===");

        int num1 = 10;
        int num2 = 20;

        System.out.println("num1 = " + num1 + ", num2 = " + num2);
        System.out.println("num1 == num2 : " + (num1 == num2));  // false (equal to)
        System.out.println("num1 != num2 : " + (num1 != num2));  // true  (not equal to)
        System.out.println("num1 > num2  : " + (num1 > num2));   // false (greater than)
        System.out.println("num1 < num2  : " + (num1 < num2));   // true  (less than)
        System.out.println("num1 >= num2 : " + (num1 >= num2));  // false (greater or equal)
        System.out.println("num1 <= num2 : " + (num1 <= num2));  // true  (less or equal)

        // Comparing strings with == vs equals()
        String str1 = "Hello";
        String str2 = "Hello";
        String str3 = new String("Hello");

        System.out.println("\nString comparison:");
        System.out.println("str1 == str2    : " + (str1 == str2));       // true (same reference)
        System.out.println("str1 == str3    : " + (str1 == str3));       // false (different objects)
        System.out.println("str1.equals(str3): " + str1.equals(str3));   // true  (same content)

        // =========================================================
        // 3. LOGICAL OPERATORS
        // =========================================================
        System.out.println("\n=== Logical Operators ===");

        boolean p = true;
        boolean q = false;

        System.out.println("p = " + p + ", q = " + q);
        System.out.println("p && q (AND) : " + (p && q));  // false - both must be true
        System.out.println("p || q (OR)  : " + (p || q));  // true  - at least one must be true
        System.out.println("!p (NOT)     : " + (!p));       // false - inverts the value
        System.out.println("!q (NOT)     : " + (!q));       // true  - inverts the value

        // Practical example: checking ranges
        int age = 25;
        boolean isAdult = (age >= 18);
        boolean isSenior = (age >= 65);
        boolean canVote = isAdult && !isSenior;

        System.out.println("\nAge = " + age);
        System.out.println("isAdult  : " + isAdult);   // true
        System.out.println("isSenior : " + isSenior);  // false
        System.out.println("canVote  : " + canVote);   // true

        // Short-circuit evaluation
        // && stops at first false, || stops at first true
        int value = 0;
        boolean shortCircuit = (value != 0) && (10 / value > 1);
        System.out.println("\nShort-circuit prevents division by zero: " + shortCircuit);

        // =========================================================
        // 4. ASSIGNMENT OPERATORS
        // =========================================================
        System.out.println("\n=== Assignment Operators ===");

        int num = 10;
        System.out.println("Starting num = " + num);

        num += 5;   // num = num + 5  -> 15
        System.out.println("num += 5  -> " + num);

        num -= 3;   // num = num - 3  -> 12
        System.out.println("num -= 3  -> " + num);

        num *= 2;   // num = num * 2  -> 24
        System.out.println("num *= 2  -> " + num);

        num /= 4;   // num = num / 4  -> 6
        System.out.println("num /= 4  -> " + num);

        num %= 4;   // num = num % 4  -> 2
        System.out.println("num %= 4  -> " + num);

        // Bitwise assignment operators
        int bits = 0b1010; // 10 in decimal
        bits &= 0b1100;    // bits = bits & 0b1100 -> 0b1000 (8)
        System.out.println("\nBitwise AND assignment: " + bits);

        bits |= 0b0011;    // bits = bits | 0b0011 -> 0b1011 (11)
        System.out.println("Bitwise OR assignment:  " + bits);

        bits ^= 0b1111;    // bits = bits ^ 0b1111 -> 0b0100 (4)
        System.out.println("Bitwise XOR assignment: " + bits);

        // =========================================================
        // 5. TERNARY OPERATOR
        // =========================================================
        System.out.println("\n=== Ternary Operator ===");

        // Syntax: condition ? valueIfTrue : valueIfFalse
        int testScore = 85;
        String grade = (testScore >= 90) ? "A" :
                       (testScore >= 80) ? "B" :
                       (testScore >= 70) ? "C" :
                       (testScore >= 60) ? "D" : "F";

        System.out.println("Score: " + testScore + " -> Grade: " + grade); // B

        // Find maximum of two numbers
        int x1 = 15, y1 = 23;
        int max = (x1 > y1) ? x1 : y1;
        System.out.println("Max of " + x1 + " and " + y1 + " is: " + max); // 23

        // Check even or odd
        int number = 7;
        String parity = (number % 2 == 0) ? "even" : "odd";
        System.out.println(number + " is " + parity);

        // =========================================================
        // 6. BITWISE OPERATORS
        // =========================================================
        System.out.println("\n=== Bitwise Operators ===");

        int m = 12;   // binary: 1100
        int n = 10;   // binary: 1010

        System.out.println("m = " + m + " (binary: " + Integer.toBinaryString(m) + ")");
        System.out.println("n = " + n + " (binary: " + Integer.toBinaryString(n) + ")");

        int andResult = m & n;    // AND: 1000 = 8
        int orResult = m | n;     // OR:  1110 = 14
        int xorResult = m ^ n;    // XOR: 0110 = 6
        int notResult = ~m;       // NOT: bitwise complement

        System.out.println("m & n  = " + andResult + " (binary: " + Integer.toBinaryString(andResult) + ")");
        System.out.println("m | n  = " + orResult + " (binary: " + Integer.toBinaryString(orResult) + ")");
        System.out.println("m ^ n  = " + xorResult + " (binary: " + Integer.toBinaryString(xorResult) + ")");
        System.out.println("~m     = " + notResult);

        // Shift operators
        int shiftVal = 8;  // binary: 1000
        int leftShift = shiftVal << 2;   // Shift left by 2: 100000 = 32
        int rightShift = shiftVal >> 1;  // Shift right by 1: 100 = 4

        System.out.println("\nShift operators (shiftVal = " + shiftVal + "):");
        System.out.println("shiftVal << 2 = " + leftShift + " (multiply by 2^2 = 4)");
        System.out.println("shiftVal >> 1 = " + rightShift + " (divide by 2^1 = 2)");

        // Signed right shift (preserves sign bit)
        int negative = -16;
        int signedRightShift = negative >> 2;   // -4 (arithmetic shift)
        int unsignedRightShift = negative >>> 2; // Large positive (logical shift)

        System.out.println("\nSigned vs unsigned right shift (value = " + negative + "):");
        System.out.println(">>  (signed)  : " + signedRightShift);
        System.out.println(">>> (unsigned): " + unsignedRightShift);

        // =========================================================
        // 7. OPERATOR PRECEDENCE
        // =========================================================
        System.out.println("\n=== Operator Precedence (simplified) ===");
        System.out.println("Highest to Lowest:");
        System.out.println("  1. Postfix     : x++, x--");
        System.out.println("  2. Unary       : ++x, --x, !, ~, +, -");
        System.out.println("  3. Multiplicative: *, /, %");
        System.out.println("  4. Additive    : +, -");
        System.out.println("  5. Shift       : <<, >>, >>>");
        System.out.println("  6. Relational  : <, >, <=, >=");
        System.out.println("  7. Equality    : ==, !=");
        System.out.println("  8. Bitwise AND : &");
        System.out.println("  9. Bitwise XOR : ^");
        System.out.println(" 10. Bitwise OR  : |");
        System.out.println(" 11. Logical AND : &&");
        System.out.println(" 12. Logical OR  : ||");
        System.out.println(" 13. Ternary     : ? :");
        System.out.println(" 14. Assignment  : =, +=, -=, etc.");

        // Example
        int result = 2 + 3 * 4;  // Multiplication first: 2 + 12 = 14
        System.out.println("\n2 + 3 * 4 = " + result + " (multiplication before addition)");
        result = (2 + 3) * 4;    // Parentheses first: 5 * 4 = 20
        System.out.println("(2 + 3) * 4 = " + result + " (parentheses override precedence)");

        System.out.println("\n=== Operators Demo Complete ===");
    }
}
