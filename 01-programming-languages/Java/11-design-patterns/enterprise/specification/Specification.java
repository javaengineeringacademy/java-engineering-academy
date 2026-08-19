package academy.javaengineering.patterns.enterprise.specification;

/**
 * Specification interface defining the contract for business rules.
 * Each specification encapsulates a single rule that can be evaluated
 * against a candidate object.
 *
 * @param <T> the type of object this specification applies to
 */
public interface Specification<T> {

    /**
     * Check whether the candidate satisfies this specification.
     *
     * @param candidate the object to test
     * @return true if the candidate satisfies the rule
     */
    boolean isSatisfiedBy(T candidate);

    /**
     * Combine this specification with another using logical AND.
     *
     * @param other the other specification
     * @return a new specification that is satisfied only if both are satisfied
     */
    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    /**
     * Combine this specification with another using logical OR.
     *
     * @param other the other specification
     * @return a new specification that is satisfied if either is satisfied
     */
    default Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    /**
     * Negate this specification using logical NOT.
     *
     * @return a new specification that is satisfied when this one is not
     */
    default Specification<T> not() {
        return new NotSpecification<>(this);
    }
}
