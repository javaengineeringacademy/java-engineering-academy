package academy.javaengineering.testing.mutation.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1MutationKillingSolution {

    static class GradeCalculator {
        String getGrade(int score) {
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        }
    }

    @Test
    void shouldReturnCorrectGrade() {
        GradeCalculator calc = new GradeCalculator();
        // Boundary tests for each grade
        assertEquals("A", calc.getGrade(90));
        assertEquals("A", calc.getGrade(100));
        assertEquals("B", calc.getGrade(80));
        assertEquals("B", calc.getGrade(89));
        assertEquals("C", calc.getGrade(70));
        assertEquals("C", calc.getGrade(79));
        assertEquals("D", calc.getGrade(60));
        assertEquals("D", calc.getGrade(69));
        assertEquals("F", calc.getGrade(59));
        assertEquals("F", calc.getGrade(0));
    }
}
