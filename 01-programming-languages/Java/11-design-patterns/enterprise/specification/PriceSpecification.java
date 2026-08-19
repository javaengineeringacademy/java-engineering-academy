package academy.javaengineering.patterns.enterprise.specification;

/**
 * Specification that checks if a product's price meets a threshold.
 */
public class PriceSpecification implements Specification<Product> {

    private final double maxPrice;

    public PriceSpecification(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getPrice() <= maxPrice;
    }

    @Override
    public String toString() {
        return "Price <= " + maxPrice;
    }
}
