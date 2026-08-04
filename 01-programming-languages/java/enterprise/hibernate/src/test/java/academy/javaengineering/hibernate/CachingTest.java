package academy.javaengineering.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Hibernate caching.
 */
@DisplayName("Caching Tests")
class CachingTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class);
        config.setProperty("hibernate.generate_statistics", "true");
        sessionFactory = config.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("Should return same instance from first-level cache")
    void shouldReturnSameInstanceFromCache() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Cache Instance", 10.0, "Cache");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product first = session.get(Product.class, id);
        Product second = session.get(Product.class, id);
        assertSame(first, second, "Should return same object from session cache");
        session.close();
    }

    @Test
    @DisplayName("Should not return same instance across sessions")
    void shouldNotReturnSameInstanceAcrossSessions() {
        Session session1 = sessionFactory.openSession();
        session1.beginTransaction();

        Product product = new Product("Cross Session", 20.0, "Cache");
        session1.persist(product);
        Long id = product.getId();
        session1.getTransaction().commit();
        session1.close();

        Session session2 = sessionFactory.openSession();
        Product fromSession2 = session2.get(Product.class, id);
        assertNotSame(product, fromSession2, "Different sessions should have different instances");
        session2.close();
    }

    @Test
    @DisplayName("Should track statistics correctly")
    void shouldTrackStatistics() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        long initialInserts = stats.getEntityInsertCount();
        session.persist(new Product("Stats Test", 30.0, "Stats"));
        session.getTransaction().commit();

        assertEquals(1, stats.getEntityInsertCount() - initialInserts);

        session = sessionFactory.openSession();
        long initialLoads = stats.getEntityLoadCount();
        session.get(Product.class, 1L);
        assertTrue(stats.getEntityLoadCount() > initialLoads);

        session.close();
    }

    @Test
    @DisplayName("Should evict entity from session cache")
    void shouldEvictEntityFromCache() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Evict Test", 40.0, "Cache");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product p1 = session.get(Product.class, id);
        session.evict(p1);

        Product p2 = session.get(Product.class, id);
        assertNotSame(p1, p2, "After eviction, should load new instance");
        session.close();
    }

    @Test
    @DisplayName("Should clear session cache")
    void shouldClearSessionCache() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product = new Product("Clear Test", 50.0, "Cache");
        session.persist(product);
        Long id = product.getId();
        session.getTransaction().commit();

        session = sessionFactory.openSession();
        Product p1 = session.get(Product.class, id);
        session.clear();

        Product p2 = session.get(Product.class, id);
        assertNotSame(p1, p2, "After clear, should load new instance");
        session.close();
    }
}
