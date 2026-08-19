package academy.javaengineering.patterns.enterprise.specification;

/**
 * Composite specification that negates another specification with logical NOT.
 * Satisfied when the inner specification is NOT satisfied.
 */
public class NotSpecification<T> implements Specification<T> {

    private final Specification<T> wrapped;

    public NotSpecification(Specification<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !wrapped.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(NOT " + wrapped + ")";
    }
}
