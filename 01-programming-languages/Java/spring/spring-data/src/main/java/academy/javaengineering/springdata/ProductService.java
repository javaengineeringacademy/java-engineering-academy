package academy.javaengineering.springdata;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Demonstrates Spring Data JPA service layer.
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> getProductsInPriceRange(double min, double max) {
        return productRepository.findByPriceBetween(min, max);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
