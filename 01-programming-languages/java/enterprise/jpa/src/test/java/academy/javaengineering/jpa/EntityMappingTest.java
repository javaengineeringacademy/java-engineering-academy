package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JPA entity mapping.
 */
@DisplayName("Entity Mapping Tests")
class EntityMappingTest {

    private static EntityManagerFactory emf;

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

    @Test
    @DisplayName("Should persist entity with all mapped fields")
    void shouldPersistAllFields() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Product product = new Product("Full Product", 199.99, "Electronics",
                "Full description here");
        product.setStatus(Product.ProductStatus.ACTIVE);
        em.persist(product);
        tx.commit();

        Product found = em.find(Product.class, product.getId());
        assertEquals("Full Product", found.getName());
        assertEquals(199.99, found.getPrice());
        assertEquals("Electronics", found.getCategory());
        assertEquals(Product.ProductStatus.ACTIVE, found.getStatus());
        assertNotNull(found.getCreatedDate());
        assertNotNull(found.getDescription());

        em.close();
    }

    @Test
    @DisplayName("Should handle enum mapping correctly")
    void shouldHandleEnumMapping() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        for (Product.ProductStatus status : Product.ProductStatus.values()) {
            Product p = new Product("Enum-" + status.name(), 1.0, "Enum");
            p.setStatus(status);
            em.persist(p);
        }
        tx.commit();

        Product found = em.createQuery(
                "SELECT p FROM Product p WHERE p.name = :name", Product.class)
                .setParameter("name", "Enum-ACTIVE")
                .getSingleResult();
        assertEquals(Product.ProductStatus.ACTIVE, found.getStatus());

        em.close();
    }

    @Test
    @DisplayName("Should handleLOB mapping")
    void shouldHandleLobMapping() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        String longDescription = "A".repeat(5000);
        Product product = new Product("LOB Product", 50.0, "Test");
        product.setDescription(longDescription);
        em.persist(product);
        tx.commit();

        Product found = em.find(Product.class, product.getId());
        assertEquals(5000, found.getDescription().length());

        em.close();
    }

    @Test
    @DisplayName("Should enforce column constraints")
    void shouldEnforceColumnConstraints() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Name too long should throw exception
        String longName = "X".repeat(200);
        Product product = new Product(longName, 10.0, "Test");

        assertThrows(Exception.class, () -> {
            em.persist(product);
            tx.commit();
        });

        if (tx.isActive()) {
            tx.rollback();
        }
        em.close();
    }

    @Test
    @DisplayName("Should set default values correctly")
    void shouldSetDefaultValues() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Product product = new Product("Default Test", 10.0, "Test");
        em.persist(product);
        tx.commit();

        Product found = em.find(Product.class, product.getId());
        assertEquals(Product.ProductStatus.ACTIVE, found.getStatus());
        assertNotNull(found.getCreatedDate());

        em.close();
    }
}
