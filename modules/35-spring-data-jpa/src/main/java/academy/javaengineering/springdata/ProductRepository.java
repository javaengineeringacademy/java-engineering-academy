package academy.javaengineering.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Demonstrates Spring Data JPA repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByPriceBetween(double min, double max);

    @Query("SELECT p FROM Product p WHERE p.quantity > :threshold")
    List<Product> findInStock(@Param("threshold") int threshold);

    @Query(value = "SELECT * FROM products WHERE price < :price", nativeQuery = true)
    List<Product> findCheapProducts(@Param("price") double price);
}
