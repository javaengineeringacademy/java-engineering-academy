package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JPA basics operations.
 */
@DisplayName("JPA Basics Tests")
class JpaBasicsTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUp() {
        emf = Persistence.createEntityManagerFactory("jpa-demo");
    }

    @AfterAll
    static void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    private void beginTransaction() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    private void commitAndClose() {
        tx.commit();
        em.close();
    }

    @Test
    @DisplayName("Should persist entity successfully")
    void shouldPersistEntity() {
        beginTransaction();
        Product product = new Product("Test Product", 99.99, "Test");
        em.persist(product);
        commitAndClose();

        assertNotNull(product.getId());
        assertTrue(product.getId() > 0);
    }

    @Test
    @DisplayName("Should find entity by ID")
    void shouldFindEntityById() {
        beginTransaction();
        Product product = new Product("Find Me", 49.99, "Test");
        em.persist(product);
        Long id = product.getId();
        commitAndClose();

        em = emf.createEntityManager();
        Product found = em.find(Product.class, id);
        assertNotNull(found);
        assertEquals("Find Me", found.getName());
        assertEquals(49.99, found.getPrice());
        em.close();
    }

    @Test
    @DisplayName("Should update entity via merge")
    void shouldUpdateEntity() {
        beginTransaction();
        Product product = new Product("Original", 100.0, "Test");
        em.persist(product);
        Long id = product.getId();
        commitAndClose();

        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        Product found = em.find(Product.class, id);
        found.setPrice(150.0);
        em.merge(found);
        tx.commit();

        em = emf.createEntityManager();
        Product updated = em.find(Product.class, id);
        assertEquals(150.0, updated.getPrice());
        em.close();
    }

    @Test
    @DisplayName("Should remove entity")
    void shouldRemoveEntity() {
        beginTransaction();
        Product product = new Product("Delete Me", 25.0, "Test");
        em.persist(product);
        Long id = product.getId();
        commitAndClose();

        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        Product toDelete = em.find(Product.class, id);
        em.remove(toDelete);
        tx.commit();

        em = emf.createEntityManager();
        Product deleted = em.find(Product.class, id);
        assertNull(deleted);
        em.close();
    }

    @Test
    @DisplayName("Should execute JPQL query")
    void shouldExecuteJPQLQuery() {
        beginTransaction();
        em.persist(new Product("JPQL Test 1", 50.0, "Query"));
        em.persist(new Product("JPQL Test 2", 150.0, "Query"));
        commitAndClose();

        em = emf.createEntityManager();
        List<Product> results = em.createQuery(
                "SELECT p FROM Product p WHERE p.category = :cat", Product.class)
                .setParameter("cat", "Query")
                .getResultList();
        assertEquals(2, results.size());
        em.close();
    }

    @Test
    @DisplayName("Should handle entity lifecycle transitions")
    void shouldHandleLifecycleTransitions() {
        // Transient
        Product product = new Product("Lifecycle", 10.0, "Test");
        assertNull(product.getId());

        beginTransaction();
        // Managed
        em.persist(product);
        assertNotNull(product.getId());

        // Dirty checking
        product.setPrice(20.0);
        commitAndClose();

        // Detached
        em = emf.createEntityManager();
        Product detached = em.find(Product.class, product.getId());
        assertNotSame(product, detached);
        em.close();
    }
}
