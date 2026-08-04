package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ControlFlow}.
 */
class ControlFlowTest {

    @Test
    @DisplayName("if-else selects correct branch based on condition")
    void testIfElse() {
        int age = 20;
        String result;
        if (age >= 18) {
            result = "Adult";
        } else {
            result = "Minor";
        }
        assertEquals("Adult", result);
    }

    @Test
    @DisplayName("if-else if-else chain returns correct grade")
    void testIfElseIfChain() {
        assertEquals("A", getGrade(95));
        assertEquals("B", getGrade(85));
        assertEquals("C", getGrade(75));
        assertEquals("D", getGrade(65));
        assertEquals("F", getGrade(50));
    }

    private String getGrade(int score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }

    @Test
    @DisplayName("Nested if conditions work correctly")
    void testNestedIf() {
        boolean a = true;
        boolean b = true;
        String result;
        if (a) {
            if (b) {
                result = "both true";
            } else {
                result = "only a true";
            }
        } else {
            result = "a false";
        }
        assertEquals("both true", result);
    }

    @Test
    @DisplayName("Traditional switch returns correct day name")
    void testTraditionalSwitch() {
        assertEquals("Monday", getDayName(1));
        assertEquals("Friday", getDayName(5));
        assertEquals("Weekend", getDayName(6));
        assertEquals("Weekend", getDayName(7));
        assertEquals("Invalid", getDayName(8));
    }

    private String getDayName(int day) {
        switch (day) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: case 7: return "Weekend";
            default: return "Invalid";
        }
    }

    @Test
    @DisplayName("Switch with String works correctly")
    void testSwitchString() {
        assertEquals("Greeting", getCommandResult("hello"));
        assertEquals("Farewell", getCommandResult("bye"));
        assertEquals("Unknown", getCommandResult("other"));
    }

    private String getCommandResult(String cmd) {
        switch (cmd.toLowerCase()) {
            case "hello": return "Greeting";
            case "bye": return "Farewell";
            default: return "Unknown";
        }
    }

    @Test
    @DisplayName("For loop counts correct iterations")
    void testForLoop() {
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += i;
        }
        assertEquals(55, sum);
    }

    @Test
    @DisplayName("Nested for loop produces correct result")
    void testNestedForLoop() {
        int product = 1;
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                product *= (i + j);
            }
        }
        assertTrue(product > 0);
    }

    @Test
    @DisplayName("While loop executes correct number of times")
    void testWhileLoop() {
        int count = 0;
        int n = 100;
        while (n > 1) {
            n /= 2;
            count++;
        }
        assertEquals(6, count); // 100 -> 50 -> 25 -> 12 -> 6 -> 3 -> 1
    }

    @Test
    @DisplayName("Do-while executes at least once")
    void testDoWhile() {
        int count = 0;
        int x = 0;
        do {
            count++;
            x++;
        } while (x < 0);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("break exits innermost loop")
    void testBreak() {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 5) break;
            result += i;
        }
        assertEquals(10, result); // 0+1+2+3+4
    }

    @Test
    @DisplayName("continue skips current iteration")
    void testContinue() {
        List<Integer> odds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) continue;
            odds.add(i);
        }
        assertEquals(List.of(1, 3, 5, 7, 9), odds);
    }

    @Test
    @DisplayName("Labeled break exits outer loop")
    void testLabeledBreak() {
        int result = 0;
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 2 && j == 3) break outer;
                result++;
            }
        }
        assertEquals(13, result); // 5 + 5 + 3
    }

    @Test
    @DisplayName("Labeled continue skips to next outer iteration")
    void testLabeledContinue() {
        List<String> visited = new ArrayList<>();
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) continue outer;
                visited.add(i + "," + j);
            }
        }
        assertEquals(List.of("0,0", "1,0", "2,0"), visited);
    }

    @Test
    @DisplayName("Enhanced switch with arrow syntax works correctly")
    void testEnhancedSwitch() {
        assertEquals("Monday", getDayEnhanced(1));
        assertEquals("Weekend", getDayEnhanced(6));
        assertEquals("Invalid", getDayEnhanced(8));
    }

    private String getDayEnhanced(int day) {
        return switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6, 7 -> "Weekend";
            default -> "Invalid";
        };
    }

    @Test
    @DisplayName("Switch expression with yield works correctly")
    void testSwitchWithYield() {
        assertEquals("Excellent", getFeedback(95));
        assertEquals("Good", getFeedback(82));
        assertEquals("Average", getFeedback(73));
    }

    private String getFeedback(int score) {
        return switch (score / 10) {
            case 10, 9 -> {
                yield "Excellent";
            }
            case 8 -> {
                yield "Good";
            }
            default -> "Average";
        };
    }

    @Test
    @DisplayName("Ternary operator selects correct value")
    void testTernary() {
        int a = 10, b = 20;
        assertEquals(20, (a > b) ? a : b);
    }

    @Test
    @DisplayName("For-each iterates over all elements")
    void testForEach() {
        int[] arr = {1, 2, 3, 4, 5};
        int sum = 0;
        for (int val : arr) {
            sum += val;
        }
        assertEquals(15, sum);
    }
}
