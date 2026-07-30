package com.javaacademy.sprint1.controlflow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IfElseStatementTest {

    @Test
    void testSimpleIf() {
        int score = 85;
        String result = "";
        if (score > 50) result = "Passed";
        assertEquals("Passed", result);
    }

    @Test
    void testIfElse() {
        int score = 45;
        String result = "";
        if (score >= 60) result = "Pass";
        else result = "Fail";
        assertEquals("Fail", result);
    }

    @Test
    void testIfElseIfLadder() {
        int score = 85;
        String grade = "";
        if (score >= 90) grade = "A";
        else if (score >= 80) grade = "B";
        else if (score >= 70) grade = "C";
        else if (score >= 60) grade = "D";
        else grade = "F";
        assertEquals("B", grade);
    }

    @Test
    void testTernary() {
        int score = 85;
        String result = score >= 60 ? "Pass" : "Fail";
        assertEquals("Pass", result);
    }
}

class SwitchStatementTest {

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    enum Status { PENDING, APPROVED, REJECTED }

    @Test
    void testTraditionalSwitch() {
        int day = 3;
        String name = "";
        switch (day) {
            case 1: name = "Monday"; break;
            case 2: name = "Tuesday"; break;
            case 3: name = "Wednesday"; break;
            case 4: name = "Thursday"; break;
            case 5: name = "Friday"; break;
            case 6: case 7: name = "Weekend"; break;
            default: name = "Invalid";
        }
        assertEquals("Wednesday", name);
    }

    @Test
    void testSwitchExpressionArrow() {
        int day = 3;
        String type = switch (day) {
            case 1, 2, 3, 4, 5 -> "Weekday";
            case 6, 7 -> "Weekend";
            default -> "Invalid";
        };
        assertEquals("Weekday", type);
    }

    @Test
    void testSwitchExpressionYield() {
        Day day = Day.WEDNESDAY;
        String desc = switch (day) {
            case MONDAY -> "Start of work week";
            case FRIDAY -> "End of work week";
            case SATURDAY, SUNDAY -> {
                String msg = "Weekend!";
                yield msg;
            }
            default -> "Midweek day";
        };
        assertEquals("Midweek day", desc);
    }

    @Test
    void testExhaustiveEnumSwitch() {
        Status status = Status.APPROVED;
        String action = switch (status) {
            case PENDING -> "Wait";
            case APPROVED -> "Proceed";
            case REJECTED -> "Notify";
            // No default needed - exhaustive
        };
        assertEquals("Proceed", action);
    }

    @Test
    void testPatternMatchingSwitch() {
        Object obj = "Hello";
        String result = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String length: " + s.length();
            case Double d -> "Double: " + d;
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
        assertEquals("String length: 5", result);
    }

    @Test
    void testGuardedPatterns() {
        Object num = 42;
        String classification = switch (num) {
            case Integer i when i > 0 -> "Positive integer: " + i;
            case Integer i when i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            default -> "Not an integer";
        };
        assertEquals("Positive integer: 42", classification);
    }
}

class ForLoopTest {

    @Test
    void testTraditionalFor() {
        int sum = 0;
        for (int i = 1; i <= 5; i++) sum += i;
        assertEquals(15, sum);
    }

    @Test
    void testForEach() {
        int[] numbers = {1, 2, 3, 4, 5};
        int sum = 0;
        for (int n : numbers) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testForEachNoIndex() {
        int[] numbers = {1, 2, 3};
        int[] modified = numbers.clone();
        for (int n : modified) n = n * 2;
        assertArrayEquals(new int[]{1, 2, 3}, numbers);
    }

    @Test
    void testLabeledBreak() {
        int count = 0;
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) break outer;
                count++;
            }
        }
        assertEquals(4, count);
    }

    @Test
    void testLabeledContinue() {
        int count = 0;
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (j == 2) continue outer;
                count++;
            }
        }
        assertEquals(3, count);
    }
}

class WhileDoWhileTest {

    @Test
    void testWhile() {
        int count = 5;
        int sum = 0;
        while (count > 0) {
            sum += count;
            count--;
        }
        assertEquals(15, sum);
    }

    @Test
    void testDoWhile() {
        int count = 0;
        do {
            count++;
        } while (count < 3);
        assertEquals(3, count);
    }

    @Test
    void testDoWhileRunsOnce() {
        int count = 0;
        do {
            count++;
        } while (false);
        assertEquals(1, count);
    }

    @Test
    void testNestedWhile() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i <= 3) {
            int j = 1;
            while (j <= i) {
                sb.append("* ");
                j++;
            }
            sb.append("\n");
            i++;
        }
        assertEquals("* \n* * \n* * * \n", sb.toString());
    }
}

class BreakContinueTest {

    @Test
    void testBreakFor() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            if (i == 5) break;
            sb.append(i);
        }
        assertEquals("1234", sb.toString());
    }

    @Test
    void testContinueFor() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i == 3) continue;
            sb.append(i);
        }
        assertEquals("1245", sb.toString());
    }

    @Test
    void testBreakWhile() {
        int num = 0;
        while (true) {
            num++;
            if (num % 7 == 0) break;
            if (num > 100) fail("Safety");
        }
        assertEquals(7, num);
    }

    @Test
    void testLabeledBreakSearch() {
        int[][] matrix = {{1,2,3}, {4,5,6}, {7,8,9}};
        int target = 5;
        boolean found = false;
        int foundI = -1, foundJ = -1;

        search: for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    found = true;
                    foundI = i; foundJ = j;
                    break search;
                }
            }
        }
        assertTrue(found);
        assertEquals(1, foundI);
        assertEquals(1, foundJ);
    }
}