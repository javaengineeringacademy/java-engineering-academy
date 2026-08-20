package academy.javaengineering.testing.coverage.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1CoverageTargetsSolution {

    static class NumberClassifier {
        String classify(int number) {
            if (number < 0) return "NEGATIVE";
            if (number == 0) return "ZERO";
            if (number % 2 == 0) return "EVEN";
            return "ODD";
        }
    }

    @Test
    void shouldClassifyNumbers() {
        NumberClassifier classifier = new NumberClassifier();
        assertEquals("NEGATIVE", classifier.classify(-5));
        assertEquals("ZERO", classifier.classify(0));
        assertEquals("EVEN", classifier.classify(4));
        assertEquals("ODD", classifier.classify(3));
    }
}
