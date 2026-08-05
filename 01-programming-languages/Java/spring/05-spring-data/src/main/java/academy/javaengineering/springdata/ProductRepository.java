package academy.javaengineering.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.price < :price AND p.stockQuantity > 0")
    List<Product> findAffordableInStock(@Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findMostExpensiveFirst();

    long countByStockQuantityGreaterThan(Integer quantity);
}
