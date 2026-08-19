package academy.javaengineering.patterns.enterprise.specification;

/**
 * Specification that checks if a product belongs to a specific category.
 */
public class CategorySpecification implements Specification<Product> {

    private final String category;

    public CategorySpecification(String category) {
        this.category = category;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getCategory().equalsIgnoreCase(category);
    }

    @Override
    public String toString() {
        return "Category = '" + category + "'";
    }
}
