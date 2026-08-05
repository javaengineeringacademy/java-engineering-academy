package academy.javaengineering.patterns.behavioral.visitor;

/**
 * Visitor interface for defining operations on elements.
 * Each visit method corresponds to a different element type.
 */
public interface Visitor {

    /**
     * Visit ConcreteElementA.
     *
     * @param element the element to visit
     */
    void visit(ConcreteElementA element);

    /**
     * Visit ConcreteElementB.
     *
     * @param element the element to visit
     */
    void visit(ConcreteElementB element);
}
