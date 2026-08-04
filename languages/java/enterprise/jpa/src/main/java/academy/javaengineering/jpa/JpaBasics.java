package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 * Demonstrates JPA basics including EntityManagerFactory,
 * EntityManager, CRUD operations, and entity lifecycle.
 */
public class JpaBasics {

    private static final String PERSISTENCE_UNIT = "jpa-demo";

    /**
     * Creates and configures EntityManagerFactory.
     */
    public static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    /**
     * Demonstrates entity lifecycle states.
     */
    public static void demonstrateLifecycle() {
        System.out.println("=== Entity Lifecycle ===");

        EntityManagerFactory emf = createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        // TRANSIENT state - entity not associated with persistence context
        Product product = new Product("Laptop", 999.99, "Electronics");
        System.out.println("1. TRANSIENT: " + product.getName() + " (ID: " + product.getId() + ")");

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // MANAGED state - entity associated with persistence context
        em.persist(product);
        System.out.println("2. MANAGED: Persisted with ID " + product.getId());

        // Modify while managed - changes tracked by dirty checking
        product.setPrice(899.99);
        System.out.println("3. DIRTY CHECKING: Price changed to " + product.getPrice());

        tx.commit();

        // DETACHED state - entity no longer associated with persistence context
        em.close();
        System.out.println("4. DETACHED: EntityManager closed");

        // Re-associate detached entity
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        Product managed = em.merge(product);
        System.out.println("5. MERGED: Re-attached with ID " + managed.getId());

        // REMOVED state - entity marked for deletion
        em.remove(managed);
        System.out.println("6. REMOVED: Entity marked for deletion");

        tx.commit();
        em.close();
        emf.close();
    }

    /**
     * Demonstrates basic CRUD operations.
     */
    public static void demonstrateCRUD() {
        System.out.println("\n=== CRUD Operations ===");

        EntityManagerFactory emf = createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // CREATE
        tx.begin();
        Product product1 = new Product("Keyboard", 79.99, "Peripherals");
        Product product2 = new Product("Mouse", 49.99, "Peripherals");
        Product product3 = new Product("Monitor", 599.99, "Display");
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);
        tx.commit();
        System.out.println("CREATED: 3 products");

        // READ
        Product found = em.find(Product.class, product1.getId());
        System.out.println("READ: Found " + found.getName() + " at $" + found.getPrice());

        // READ ALL
        List<Product> allProducts = em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
        System.out.println("READ ALL: Found " + allProducts.size() + " products");

        // UPDATE
        tx.begin();
        found.setPrice(69.99);
        em.merge(found);
        tx.commit();
        System.out.println("UPDATE: Price changed to $" + found.getPrice());

        // DELETE
        tx.begin();
        Product toDelete = em.find(Product.class, product2.getId());
        em.remove(toDelete);
        tx.commit();
        System.out.println("DELETE: Removed " + toDelete.getName());

        // Verify deletion
        List<Product> remaining = em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
        System.out.println("VERIFY: " + remaining.size() + " products remain");

        em.close();
        emf.close();
    }

    /**
     * Demonstrates persistence context behavior.
     */
    public static void demonstratePersistenceContext() {
        System.out.println("\n=== Persistence Context ===");

        EntityManagerFactory emf = createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Product product = new Product("Tablet", 449.99, "Electronics");
        em.persist(product);
        Long id = product.getId();
        tx.commit();

        System.out.println("Product persisted with ID: " + id);

        // Same persistence context returns same instance
        Product same = em.find(Product.class, id);
        System.out.println("Same instance? " + (product == same));

        em.close();

        // Different persistence context
        EntityManager em2 = emf.createEntityManager();
        Product different = em2.find(Product.class, id);
        System.out.println("Different context - same instance? " + (product == different));

        em2.close();
        emf.close();
    }

    /**
     * Demonstrates JPQL queries.
     */
    public static void demonstrateJPQL() {
        System.out.println("\n=== JPQL Queries ===");

        EntityManagerFactory emf = createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(new Product("CPU", 349.99, "Components"));
        em.persist(new Product("GPU", 699.99, "Components"));
        em.persist(new Product("RAM", 129.99, "Components"));
        em.persist(new Product("SSD", 89.99, "Storage"));
        tx.commit();

        // Named parameter query
        List<Product> expensive = em.createQuery(
                        "SELECT p FROM Product p WHERE p.price > :price", Product.class)
                .setParameter("price", 200.0)
                .getResultList();
        System.out.println("Products over $200: " + expensive.size());

        // Like query
        List<Product> components = em.createQuery(
                        "SELECT p FROM Product p WHERE p.category = :cat", Product.class)
                .setParameter("cat", "Components")
                .getResultList();
        System.out.println("Components: " + components.size());

        // Order by
        List<Product> ordered = em.createQuery(
                        "SELECT p FROM Product p ORDER BY p.price DESC", Product.class)
                .getResultList();
        System.out.println("Most expensive: " + ordered.get(0).getName());

        em.close();
        emf.close();
    }

    /**
     * Runs all JPA basics demonstrations.
     */
    public static void main(String[] args) {
        demonstrateLifecycle();
        demonstrateCRUD();
        demonstratePersistenceContext();
        demonstrateJPQL();
    }
}
