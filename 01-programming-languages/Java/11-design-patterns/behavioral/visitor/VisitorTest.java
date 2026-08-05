package academy.javaengineering.patterns.behavioral.visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitorTest {

    @Test
    void visitorShouldProcessElementA() {
        ConcreteElementA element = new ConcreteElementA("Test");
        ConcreteVisitor visitor = new ConcreteVisitor();
        element.accept(visitor);
        assertTrue(visitor.getResult().contains("ElementA[Test]"));
    }

    @Test
    void visitorShouldProcessElementB() {
        ConcreteElementB element = new ConcreteElementB(42);
        ConcreteVisitor visitor = new ConcreteVisitor();
        element.accept(visitor);
        assertTrue(visitor.getResult().contains("ElementB[42]"));
    }

    @Test
    void visitorShouldProcessMultipleElements() {
        ConcreteVisitor visitor = new ConcreteVisitor();
        new ConcreteElementA("A").accept(visitor);
        new ConcreteElementB(1).accept(visitor);
        assertEquals(2, visitor.getResult().lines().count());
    }

    @Test
    void elementAOperationShouldReturnCorrectFormat() {
        ConcreteElementA element = new ConcreteElementA("Test");
        assertEquals("ElementA[Test]", element.operationA());
    }

    @Test
    void elementBOperationShouldReturnCorrectFormat() {
        ConcreteElementB element = new ConcreteElementB(100);
        assertEquals("ElementB[100]", element.operationB());
    }
}
