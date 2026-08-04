package academy.javaengineering.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

/**
 * Demonstrates Hibernate caching mechanisms.
 */
public class CachingExample {

    private static SessionFactory sessionFactory;

    /**
     * Initializes SessionFactory with statistics enabled.
     */
    public static void init() {
        Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class);
        config.setProperty("hibernate.generate_statistics", "true");
        sessionFactory = config.buildSessionFactory();
    }

    /**
     * Demonstrates first-level (session) cache.
     */
    public static void demonstrateFirstLevelCache() {
        System.out.println("=== First-Level Cache ===");

        init();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // First query - hits database
        Product p1 = session.get(Product.class, 1L);
        System.out.println("First get: " + p1.getName());

        // Second query - served from session cache (no DB hit)
        Product p2 = session.get(Product.class, 1L);
        System.out.println("Second get: " + p2.getName());
        System.out.println("Same instance? " + (p1 == p2));

        session.getTransaction().commit();
        session.close();

        // New session - cache cleared
        session = sessionFactory.openSession();
        Product p3 = session.get(Product.class, 1L);
        System.out.println("New session get: " + p3.getName());
        System.out.println("Different instance from p1? " + (p1 != p3));

        session.close();
        shutdown();
    }

    /**
     * Demonstrates query cache.
     */
    public static void demonstrateQueryCache() {
        System.out.println("\n=== Query Cache ===");

        init();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        // First query - cache miss
        var query1 = session.createQuery("FROM Product WHERE price > :price", Product.class);
        query1.setParameter("price", 100.0);
        query1.setCacheable(true);
        var results1 = query1.getResultList();
        System.out.println("First query: " + results1.size() + " results");
        System.out.println("Query cache hits: " + stats.getQueryCacheHitCount());
        System.out.println("Query cache misses: " + stats.getQueryCacheMissCount());

        // Second query - cache hit
        var query2 = session.createQuery("FROM Product WHERE price > :price", Product.class);
        query2.setParameter("price", 100.0);
        query2.setCacheable(true);
        var results2 = query2.getResultList();
        System.out.println("Second query: " + results2.size() + " results");
        System.out.println("Query cache hits: " + stats.getQueryCacheHitCount());

        session.getTransaction().commit();
        session.close();
        shutdown();
    }

    /**
     * Demonstrates entity cache statistics.
     */
    public static void demonstrateStatistics() {
        System.out.println("\n=== Hibernate Statistics ===");

        init();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        // Perform operations
        for (int i = 0; i < 5; i++) {
            Product p = new Product("Stat Product " + i, 10.0 * i, "Test");
            session.persist(p);
        }
        session.getTransaction().commit();

        System.out.println("Entities inserted: " + stats.getEntityInsertCount());
        System.out.println("Entities loaded: " + stats.getEntityLoadCount());
        System.out.println("Sessions opened: " + stats.getSessionOpenCount());
        System.out.println("Sessions closed: " + stats.getSessionCloseCount());

        session.close();
        shutdown();
    }

    /**
     * Runs all caching demonstrations.
     */
    public static void main(String[] args) {
        demonstrateFirstLevelCache();
        demonstrateQueryCache();
        demonstrateStatistics();
    }

    /**
     * Shuts down the SessionFactory.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
