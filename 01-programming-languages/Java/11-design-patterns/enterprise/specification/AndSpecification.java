package academy.javaengineering.patterns.enterprise.specification;

/**
 * Composite specification that combines two specifications with logical AND.
 * Satisfied only when both inner specifications are satisfied.
 */
public class AndSpecification<T> implements Specification<T> {

    private final Specification<T> left;
    private final Specification<T> right;

    public AndSpecification(Specification<T> left, Specification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(" + left + " AND " + right + ")";
    }
}
