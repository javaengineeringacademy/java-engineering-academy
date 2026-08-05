package academy.javaengineering.patterns.behavioral.visitor;

/**
 * Concrete Element B implementation.
 * Represents another type of element that can be visited.
 */
public class ConcreteElementB implements Element {

    private final int value;

    public ConcreteElementB(int value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getValue() {
        return value;
    }

    public String operationB() {
        return "ElementB[" + value + "]";
    }
}
