package academy.javaengineering.patterns.behavioral.visitor;

/**
 * Concrete Element A implementation.
 * Represents one type of element that can be visited.
 */
public class ConcreteElementA implements Element {

    private final String name;

    public ConcreteElementA(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public String operationA() {
        return "ElementA[" + name + "]";
    }
}
