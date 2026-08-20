package academy.javaengineering.testing.integration.solutions;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1RepositoryTestSolution {

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
            if (entity.getId() == null) store.put(nextId++, entity);
            else store.put(entity.getId(), entity);
        }
        public Optional<Product> findById(Long id) { return Optional.ofNullable(store.get(id)); }
        public List<Product> findAll() { return new ArrayList<>(store.values()); }
        public void delete(Long id) { store.remove(id); }
    }

    private InMemoryProductRepository repository;

    @BeforeEach
    void setUp() { repository = new InMemoryProductRepository(); }

    @Test
    void shouldSaveAndRetrieve() {
        Product product = new Product(null, "Laptop", 999.99);
        repository.save(product);
        Optional<Product> found = repository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
    }

    @Test
    void shouldReturnEmptyForNonExistent() {
        assertFalse(repository.findById(999L).isPresent());
    }

    @Test
    void shouldFindAll() {
        repository.save(new Product(null, "A", 10));
        repository.save(new Product(null, "B", 20));
        assertEquals(2, repository.findAll().size());
    }
}
