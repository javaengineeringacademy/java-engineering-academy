package academy.javaengineering.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Hibernate Repository demonstrating advanced query features.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Natural ID lookup (uses cache)
    Optional<Product> findBySku(String sku);

    // Derived queries
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByPriceBetween(java.math.BigDecimal min, java.math.BigDecimal max);

    // JPQL with fetching
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.reviews WHERE p.id = :id")
    Optional<Product> findByIdWithReviews(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

    // Aggregate queries
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.category.id = :categoryId")
    java.math.BigDecimal findAveragePriceByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT p.category.name, COUNT(p) FROM Product p GROUP BY p.category.name")
    List<Object[]> countProductsByCategory();
}
