package academy.javaengineering.hibernate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service demonstrating Hibernate-specific features like caching.
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CRUD Operations
    public Product createProduct(String sku, String name, java.math.BigDecimal price) {
        Product product = new Product(sku, name, price);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, String name, java.math.BigDecimal price) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        product.setName(name);
        product.setPrice(price);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Hibernate-specific operations
    @Transactional(readOnly = true)
    public Optional<Product> findByIdWithReviews(Long id) {
        return productRepository.findByIdWithReviews(id);
    }

    @Transactional(readOnly = true)
    public java.math.BigDecimal getAveragePriceByCategory(Long categoryId) {
        return productRepository.findAveragePriceByCategory(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getProductCountByCategory() {
        return productRepository.countProductsByCategory();
    }

    // Batch operations
    @Transactional
    public List<Product> createBulkProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }
}
