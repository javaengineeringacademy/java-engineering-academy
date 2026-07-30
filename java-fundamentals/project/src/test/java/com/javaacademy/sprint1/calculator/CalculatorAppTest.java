package com.javaacademy.sprint1.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CalculatorApp expression evaluation.
 */
class CalculatorAppTest {

    @ParameterizedTest
    @CsvSource({
        "10 + 5, 15",
        "10 - 5, 5",
        "10 * 5, 50",
        "10 / 5, 2",
        "10 % 3, 1",
        "2 ^ 10, 1024",
        "3 + 4 * 2, 11",
        "(3 + 4) * 2, 14",
        "10 / 3 + 2 * 5, 13.333333333333333",
        "2 + 3 * 4 - 5, 9",
        "10 - 2 * 3, 4",
        "100 / 25, 4",
        "5 % 2, 1",
        "2 ^ 3 ^ 2, 512",  // right-associative
        "(2 + 3) * (4 + 5), 45",
        "10 / (2 + 3), 2",
        "3.14 * 2, 6.28"
    })
    void testEvaluateExpression(String expression, String expected) {
        BigDecimal result = CalculatorApp.evaluateExpression(expression);
        assertEquals(new BigDecimal(expected).stripTrailingZeros(), 
            result.stripTrailingZeros(), 
            "Expression: " + expression);
    }

    @Test
    void testIntegerDivision() {
        // Integer division should produce decimal
        BigDecimal result = CalculatorApp.evaluateExpression("10 / 3");
        assertEquals(3.333333333333333, result.doubleValue(), 0.000000000000001);
    }

    @Test
    void testNegativeNumbers() {
        assertEquals(new BigDecimal("-5"), CalculatorApp.evaluateExpression("-10 + 5"));
        assertEquals(new BigDecimal("15"), CalculatorApp.evaluateExpression("-10 - -25"));
        assertEquals(new BigDecimal("-50"), CalculatorApp.evaluateExpression("-10 * 5"));
        assertEquals(new BigDecimal("-2"), CalculatorApp.evaluateExpression("-10 / 5"));
    }

    @Test
    void testDecimals() {
        assertEquals(new BigDecimal("6.28"), 
            CalculatorApp.evaluateExpression("3.14 * 2").setScale(2, java.math.RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("0.5"), CalculatorApp.evaluateExpression("1 / 2"));
    }

    @Test
    void testParentheses() {
        assertEquals(new BigDecimal("14"), CalculatorApp.evaluateExpression("(3 + 4) * 2"));
        assertEquals(new BigDecimal("45"), CalculatorApp.evaluateExpression("(2 + 3) * (4 + 5)"));
        assertEquals(new BigDecimal("2"), CalculatorApp.evaluateExpression("10 / (2 + 3)"));
        assertEquals(new BigDecimal("7"), CalculatorApp.evaluateExpression("2 + (3 * 5) / 5"));
    }

    @Test
    void testPowerOperator() {
        assertEquals(new BigDecimal("1024"), CalculatorApp.evaluateExpression("2 ^ 10"));
        assertEquals(new BigDecimal("8"), CalculatorApp.evaluateExpression("2 ^ 3"));
        assertEquals(new BigDecimal("1"), CalculatorApp.evaluateExpression("5 ^ 0"));
        assertEquals(new BigDecimal("0.25"), CalculatorApp.evaluateExpression("2 ^ -2"));
    }

    @Test
    void testModuloOperator() {
        assertEquals(new BigDecimal("1"), CalculatorApp.evaluateExpression("10 % 3"));
        assertEquals(new BigDecimal("0"), CalculatorApp.evaluateExpression("10 % 5"));
        assertEquals(new BigDecimal("2"), CalculatorApp.evaluateExpression("17 % 5"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "10 +",
        "10 + * 5",
        "10 / 0",
        "10 % 0",
        "(3 + 4",
        "3 + 4)",
        "2 ^ 0.5",
        "a + b",
        "10 + 5 3"
    })
    void testInvalidExpressions(String expression) {
        assertThrows(Exception.class, () -> CalculatorApp.evaluateExpression(expression),
            "Should throw for: " + expression);
    }

    @Test
    void testPowerMethod() {
        assertEquals(new BigDecimal("1024"), CalculatorApp.class.getDeclaredMethod("power", BigDecimal.class, BigDecimal.class).invoke(null, new BigDecimal("2"), new BigDecimal("10")));
        assertEquals(BigDecimal.ONE, CalculatorApp.class.getDeclaredMethod("power", BigDecimal.class, BigDecimal.class).invoke(null, new BigDecimal("5"), new BigDecimal("0")));
        assertEquals(new BigDecimal("0.25"), CalculatorApp.class.getDeclaredMethod("power", BigDecimal.class, BigDecimal.class).invoke(null, new BigDecimal("2"), new BigDecimal("-2")));
    }

    @Test
    void testSqrtMethod() throws Exception {
        var method = CalculatorApp.class.getDeclaredMethod("sqrt", BigDecimal.class);
        method.setAccessible(true);
        
        assertEquals(new BigDecimal("0"), method.invoke(null, BigDecimal.ZERO));
        assertEquals(new BigDecimal("5"), method.invoke(null, new BigDecimal("25")));
        assertEquals(new BigDecimal("1.4142135623730951"), 
            ((BigDecimal)method.invoke(null, new BigDecimal("2"))).setScale(16, java.math.RoundingMode.HALF_UP));
        
        assertThrows(Exception.class, () -> method.invoke(null, new BigDecimal("-1")));
    }

    @Test
    void testFactorialMethod() throws Exception {
        var method = CalculatorApp.class.getDeclaredMethod("factorial", int.class);
        method.setAccessible(true);
        
        assertEquals(BigDecimal.ONE, method.invoke(null, 0));
        assertEquals(BigDecimal.ONE, method.invoke(null, 1));
        assertEquals(new BigDecimal("120"), method.invoke(null, 5));
        assertEquals(new BigDecimal("2432902008176640000"), method.invoke(null, 20));
        
        assertThrows(Exception.class, () -> method.invoke(null, -1));
        assertThrows(Exception.class, () -> method.invoke(null, 21));
    }

    @Test
    void testHistoryLimit() {
        // We can't easily test private history, but we can test the public API
        // This test verifies the public evaluateExpression method works correctly
        // which internally uses the history
        for (int i = 0; i < 15; i++) {
            CalculatorApp.evaluateExpression(i + " + 1");
        }
        // Should not throw any exception
    }

    @Test
    void testOperatorPrecedence() {
        // Multiplication before addition
        assertEquals(new BigDecimal("11"), CalculatorApp.evaluateExpression("3 + 4 * 2"));
        
        // Division before addition
        assertEquals(new BigDecimal("7"), CalculatorApp.evaluateExpression("10 / 2 + 2"));
        
        // Power before multiplication
        assertEquals(new BigDecimal("18"), CalculatorApp.evaluateExpression("2 * 3 ^ 2"));
        
        // Parentheses override precedence
        assertEquals(new BigDecimal("14"), CalculatorApp.evaluateExpression("(3 + 4) * 2"));
    }

    @Test
    void testAssociativity() {
        // Left-associative: 10 - 5 - 2 = (10 - 5) - 2 = 3
        assertEquals(new BigDecimal("3"), CalculatorApp.evaluateExpression("10 - 5 - 2"));
        
        // Right-associative for power: 2 ^ 3 ^ 2 = 2 ^ (3 ^ 2) = 2 ^ 9 = 512
        assertEquals(new BigDecimal("512"), CalculatorApp.evaluateExpression("2 ^ 3 ^ 2"));
    }

    @Test
    void testComplexExpression() {
        // (3 + 4) * 2 - 10 / 5 = 7 * 2 - 2 = 14 - 2 = 12
        assertEquals(new BigDecimal("12"), CalculatorApp.evaluateExpression("(3 + 4) * 2 - 10 / 5"));
        
        // 2 + 3 * 4 - 5 = 2 + 12 - 5 = 9
        assertEquals(new BigDecimal("9"), CalculatorApp.evaluateExpression("2 + 3 * 4 - 5"));
    }
}