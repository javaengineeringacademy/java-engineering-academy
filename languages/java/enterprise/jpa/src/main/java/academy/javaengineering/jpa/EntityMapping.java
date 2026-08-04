package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 * Demonstrates JPA entity mapping annotations.
 */
public class EntityMapping {

    /**
     * Demonstrates entity creation with various annotations.
     */
    public static void demonstrateEntityMapping() {
        System.out.println("=== Entity Mapping ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Product product = new Product("Gaming Laptop", 1499.99, "Electronics",
                "High-performance gaming laptop with RTX 4080");
        product.setStatus(Product.ProductStatus.ACTIVE);
        em.persist(product);

        Product inactive = new Product("Old Keyboard", 29.99, "Peripherals");
        inactive.setStatus(Product.ProductStatus.INACTIVE);
        inactive.setDescription("Discontinued model");
        em.persist(inactive);

        tx.commit();

        System.out.println("Persisted: " + product);
        System.out.println("Persisted: " + inactive);

        // Verify persistence
        Product found = em.find(Product.class, product.getId());
        System.out.println("Found: " + found.getName());
        System.out.println("Status: " + found.getStatus());
        System.out.println("Description: " + found.getDescription());

        em.close();
        emf.close();
    }

    /**
     * Demonstrates column mapping configurations.
     */
    public static void demonstrateColumnMapping() {
        System.out.println("\n=== Column Mapping ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Product product = new Product("Mechanical Keyboard", 149.99, "Peripherals",
                "Cherry MX Blue switches with RGB lighting");
        em.persist(product);
        tx.commit();

        Product found = em.find(Product.class, product.getId());
        System.out.println("Name length: " + found.getName().length());
        System.out.println("Category: " + found.getCategory());
        System.out.println("Created: " + found.getCreatedDate());

        em.close();
        emf.close();
    }

    /**
     * Demonstrates enum mapping.
     */
    public static void demonstrateEnumMapping() {
        System.out.println("\n=== Enum Mapping ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        for (Product.ProductStatus status : Product.ProductStatus.values()) {
            Product p = new Product("Product-" + status.name(), 10.0, "Test");
            p.setStatus(status);
            em.persist(p);
        }
        tx.commit();

        List<Product> products = em.createQuery(
                "SELECT p FROM Product p WHERE p.category = :cat", Product.class)
                .setParameter("cat", "Test")
                .getResultList();

        products.forEach(p ->
                System.out.println(p.getName() + " -> " + p.getStatus()));

        em.close();
        emf.close();
    }

    /**
     * Runs all entity mapping demonstrations.
     */
    public static void main(String[] args) {
        demonstrateEntityMapping();
        demonstrateColumnMapping();
        demonstrateEnumMapping();
    }
}
