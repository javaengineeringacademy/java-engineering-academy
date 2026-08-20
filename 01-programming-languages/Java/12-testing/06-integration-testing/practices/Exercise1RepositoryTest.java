package academy.javaengineering.testing.integration.practices;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Repository Integration Test
 *
 * Tasks:
 * 1. Test CRUD operations on a repository
 * 2. Test find by various criteria
 * 3. Test error handling scenarios
 * 4. Test data consistency
 */
class Exercise1RepositoryTest {

    interface Repository<T> {
        void save(T entity);
        Optional<T> findById(Long id);
        List<T> findAll();
        void delete(Long id);
    }

    static class Product {
        private Long id;
        private String name;
        private double price;
        Product(Long id, String name, double price) {
            this.id = id; this.name = name; this.price = price;
        }
        Long getId() { return id; }
        String getName() { return name; }
        double getPrice() { return price; }
    }

    static class InMemoryProductRepository implements Repository<Product> {
        private final java.util.Map<Long, Product> store = new java.util.HashMap<>();
        private long nextId = 1;

        public void save(Product entity) {
            if (entity.getId() == null) {
                // Simulate auto-generated ID
                store.put(nextId++, entity);
            } else {
                store.put(entity.getId(), entity);
            }
        }
        public Optional<Product> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }
        public List<Product> findAll() {
            return new ArrayList<>(store.values());
        }
        public void delete(Long id) {
            store.remove(id);
        }
    }

    private InMemoryProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    @DisplayName("should save and retrieve product")
    void shouldSaveAndRetrieve() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should return empty for non-existent id")
    void shouldReturnEmptyForNonExistent() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should find all products")
    void shouldFindAll() {
        // Arrange, Act, Assert
    }
}
