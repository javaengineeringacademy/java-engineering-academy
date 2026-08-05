package academy.javaengineering.patterns.behavioral.visitor;

/**
 * Element interface for accepting visitors.
 * Defines the accept method for the Visitor pattern.
 */
public interface Element {

    /**
     * Accept a visitor.
     *
     * @param visitor the visitor to accept
     */
    void accept(Visitor visitor);
}
