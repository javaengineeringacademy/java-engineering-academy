package academy.javaengineering.patterns.enterprise.specification;

/**
 * Composite specification that combines two specifications with logical OR.
 * Satisfied when at least one inner specification is satisfied.
 */
public class OrSpecification<T> implements Specification<T> {

    private final Specification<T> left;
    private final Specification<T> right;

    public OrSpecification(Specification<T> left, Specification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(" + left + " OR " + right + ")";
    }
}
