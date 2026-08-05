package academy.javaengineering.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Demonstrates Hibernate basics including SessionFactory,
 * Session, CRUD operations, and HQL.
 */
public class HibernateBasics {

    private static SessionFactory sessionFactory;

    /**
     * Initializes Hibernate SessionFactory.
     */
    public static void init() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class)
                .buildSessionFactory();
    }

    /**
     * Demonstrates CRUD operations using Hibernate Session.
     */
    public static void demonstrateCRUD() {
        System.out.println("=== Hibernate CRUD ===");

        init();

        // CREATE
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Product product1 = new Product("Laptop", 999.99, "Electronics");
        Product product2 = new Product("Phone", 699.99, "Electronics");
        Product product3 = new Product("Book", 29.99, "Education");

        session.persist(product1);
        session.persist(product2);
        session.persist(product3);

        session.getTransaction().commit();
        session.close();
        System.out.println("CREATED: 3 products");

        // READ
        session = sessionFactory.openSession();
        Product found = session.get(Product.class, product1.getId());
        System.out.println("READ: " + found.getName() + " - $" + found.getPrice());
        session.close();

        // UPDATE
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product toUpdate = session.get(Product.class, product1.getId());
        toUpdate.setPrice(899.99);
        session.merge(toUpdate);
        session.getTransaction().commit();
        session.close();
        System.out.println("UPDATE: Price changed to $" + toUpdate.getPrice());

        // DELETE
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product toDelete = session.get(Product.class, product2.getId());
        session.remove(toDelete);
        session.getTransaction().commit();
        session.close();
        System.out.println("DELETE: Removed " + toDelete.getName());

        // Verify
        session = sessionFactory.openSession();
        List<Product> remaining = session.createQuery("FROM Product", Product.class)
                .getResultList();
        System.out.println("VERIFY: " + remaining.size() + " products remain");
        session.close();

        shutdown();
    }

    /**
     * Demonstrates HQL (Hibernate Query Language).
     */
    public static void demonstrateHQL() {
        System.out.println("\n=== HQL Queries ===");

        init();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(new Product("CPU", 349.99, "Components"));
        session.persist(new Product("GPU", 699.99, "Components"));
        session.persist(new Product("RAM", 129.99, "Components"));
        session.persist(new Product("SSD", 89.99, "Storage"));
        session.persist(new Product("HDD", 59.99, "Storage"));

        session.getTransaction().commit();

        // Simple query
        List<Product> all = session.createQuery("FROM Product", Product.class)
                .getResultList();
        System.out.println("All products: " + all.size());

        // Parameterized query
        List<Product> expensive = session.createQuery(
                        "FROM Product WHERE price > :price", Product.class)
                .setParameter("price", 200.0)
                .getResultList();
        System.out.println("Over $200: " + expensive.size());

        // Named query
        List<Product> components = session.createNamedQuery(
                        "Product.findByCategory", Product.class)
                .setParameter("category", "Components")
                .getResultList();
        System.out.println("Components: " + components.size());

        // Projections
        List<Object[]> summaries = session.createQuery(
                        "SELECT name, price FROM Product ORDER BY price DESC", Object[].class)
                .getResultList();
        System.out.println("Products by price:");
        summaries.forEach(row ->
                System.out.println("  " + row[0] + ": $" + row[1]));

        // Aggregate
        Double avgPrice = session.createQuery(
                        "SELECT AVG(price) FROM Product", Double.class)
                .getSingleResult();
        System.out.println("Average price: $" + avgPrice);

        // Update with HQL
        session.beginTransaction();
        int updated = session.createQuery(
                        "UPDATE Product SET price = price * 0.9 WHERE category = :cat")
                .setParameter("cat", "Storage")
                .executeUpdate();
        System.out.println("Updated " + updated + " storage products (10% discount)");
        session.getTransaction().commit();

        session.close();
        shutdown();
    }

    /**
     * Demonstrates entity states in Hibernate.
     */
    public static void demonstrateEntityStates() {
        System.out.println("\n=== Entity States ===");

        init();

        // Transient
        Product transientProduct = new Product("Transient", 10.0, "Test");
        System.out.println("TRANSIENT: " + transientProduct.getName() + " (null ID: " +
                (transientProduct.getId() == null) + ")");

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Persistent
        session.persist(transientProduct);
        System.out.println("PERSISTENT: ID assigned = " + transientProduct.getId());

        // Dirty checking
        transientProduct.setPrice(15.0);
        System.out.println("DIRTY: Price modified");

        session.getTransaction().commit();
        session.close();

        // Detached
        System.out.println("DETACHED: Session closed");

        // Reattach
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product reattached = session.merge(transientProduct);
        System.out.println("MERGED: Reattached = " + reattached.getName());
        session.getTransaction().commit();
        session.close();

        // Remove
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product toRemove = session.get(Product.class, reattached.getId());
        session.remove(toRemove);
        System.out.println("REMOVED: " + toRemove.getName());
        session.getTransaction().commit();
        session.close();

        shutdown();
    }

    /**
     * Runs all Hibernate basics demonstrations.
     */
    public static void main(String[] args) {
        demonstrateCRUD();
        demonstrateHQL();
        demonstrateEntityStates();
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
