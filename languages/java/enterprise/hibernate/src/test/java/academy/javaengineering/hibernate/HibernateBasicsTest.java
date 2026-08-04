package academy.javaengineering.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Hibernate basics.
 */
@DisplayName("Hibernate Basics Tests")
class HibernateBasicsTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class);
        sessionFactory = config.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("Should persist entity via Session")
    void shouldPersistEntity() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Test Product", 99.99, "Test");
        session.persist(product);
        session.getTransaction().commit();

        assertNotNull(product.getId());
        assertTrue(product.getId() > 0);
        session.close();
    }

    @Test
    @DisplayName("Should retrieve entity by ID")
    void shouldRetrieveEntity() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Retrieve Test", 49.99, "Test");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product found = session.get(Product.class, id);
        assertNotNull(found);
        assertEquals("Retrieve Test", found.getName());
        session.close();
    }

    @Test
    @DisplayName("Should update entity via merge")
    void shouldUpdateEntity() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Update Test", 100.0, "Test");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Product toUpdate = session.get(Product.class, id);
        toUpdate.setPrice(150.0);
        session.merge(toUpdate);
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product updated = session.get(Product.class, id);
        assertEquals(150.0, updated.getPrice());
        session.close();
    }

    @Test
    @DisplayName("Should delete entity via remove")
    void shouldDeleteEntity() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Delete Test", 25.0, "Test");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Product toDelete = session.get(Product.class, id);
        session.remove(toDelete);
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product deleted = session.get(Product.class, id);
        assertNull(deleted);
        session.close();
    }

    @Test
    @DisplayName("Should execute HQL query")
    void shouldExecuteHQL() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(new Product("HQL Test 1", 50.0, "HQL"));
        session.persist(new Product("HQL Test 2", 150.0, "HQL"));
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        List<Product> results = session.createQuery(
                        "FROM Product WHERE category = :cat", Product.class)
                .setParameter("cat", "HQL")
                .getResultList();
        assertEquals(2, results.size());
        session.close();
    }

    @Test
    @DisplayName("Should use first-level cache")
    void shouldUseFirstLevelCache() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Cache Test", 10.0, "Cache");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product first = session.get(Product.class, id);
        Product second = session.get(Product.class, id);
        assertSame(first, second);
        session.close();
    }
}
