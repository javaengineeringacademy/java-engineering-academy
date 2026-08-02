package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OOPInterviewQuestionsTest {

    @Test
    void shouldCalculateCircleArea() {
        OOPInterviewQuestions.Circle circle = new OOPInterviewQuestions.Circle(5);
        assertEquals(Math.PI * 25, circle.area(), 0.001);
    }

    @Test
    void shouldCalculateRectangleArea() {
        OOPInterviewQuestions.Rectangle rect = new OOPInterviewQuestions.Rectangle(4, 6);
        assertEquals(24, rect.area(), 0.001);
    }

    @Test
    void shouldImplementPolymorphism() {
        OOPInterviewQuestions.Shape circle = new OOPInterviewQuestions.Circle(5);
        OOPInterviewQuestions.Shape rect = new OOPInterviewQuestions.Rectangle(4, 6);
        assertEquals("Circle", circle.getType());
        assertEquals("Rectangle", rect.getType());
    }
}
