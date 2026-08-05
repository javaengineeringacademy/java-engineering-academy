package academy.javaengineering.patterns.behavioral.visitor;

/**
 * Concrete Visitor implementation.
 * Implements operations for each element type.
 */
public class ConcreteVisitor implements Visitor {

    private StringBuilder result = new StringBuilder();

    @Override
    public void visit(ConcreteElementA element) {
        result.append("Visiting ").append(element.operationA()).append("\n");
    }

    @Override
    public void visit(ConcreteElementB element) {
        result.append("Visiting ").append(element.operationB()).append("\n");
    }

    public String getResult() {
        return result.toString();
    }

    public void reset() {
        result = new StringBuilder();
    }
}
