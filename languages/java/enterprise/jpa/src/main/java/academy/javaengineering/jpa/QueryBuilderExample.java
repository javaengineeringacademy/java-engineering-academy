package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Demonstrates JPQL, Criteria API, Named Queries, and Native Queries.
 */
public class QueryBuilderExample {

    /**
     * Demonstrates JPQL queries.
     */
    public static void demonstrateJPQL() {
        System.out.println("=== JPQL Queries ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();

        // Simple select
        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
        System.out.println("All products: " + products.size());

        // Where clause with parameter
        TypedQuery<Product> expensiveQuery = em.createQuery(
                "SELECT p FROM Product p WHERE p.price > :price", Product.class);
        expensiveQuery.setParameter("price", 100.0);
        List<Product> expensive = expensiveQuery.getResultList();
        System.out.println("Products > $100: " + expensive.size());

        // Order by
        List<Product> ordered = em.createQuery(
                "SELECT p FROM Product p ORDER BY p.price DESC", Product.class)
                .getResultList();
        ordered.forEach(p -> System.out.println("  " + p.getName() + ": $" + p.getPrice()));

        // Aggregate functions
        Double avgPrice = em.createQuery(
                "SELECT AVG(p.price) FROM Product p", Double.class)
                .getSingleResult();
        System.out.println("Average price: $" + avgPrice);

        // Group by
        List<Object[]> categoryGroups = em.createQuery(
                "SELECT p.category, COUNT(p), AVG(p.price) FROM Product p GROUP BY p.category",
                Object[].class)
                .getResultList();
        categoryGroups.forEach(row ->
                System.out.println("  " + row[0] + ": " + row[1] + " items, avg $" + row[2]));

        // Like query
        List<Product> peripherals = em.createQuery(
                "SELECT p FROM Product p WHERE p.name LIKE :pattern", Product.class)
                .setParameter("pattern", "%Keyboard%")
                .getResultList();
        System.out.println("Keyboards found: " + peripherals.size());

        em.close();
        emf.close();
    }

    /**
     * Demonstrates Criteria API for type-safe queries.
     */
    public static void demonstrateCriteriaAPI() {
        System.out.println("\n=== Criteria API ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Simple query
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);
        List<Product> all = em.createQuery(query).getResultList();
        System.out.println("All products: " + all.size());

        // Where clause
        CriteriaQuery<Product> priceQuery = cb.createQuery(Product.class);
        Root<Product> priceRoot = priceQuery.from(Product.class);
        priceQuery.select(priceRoot)
                .where(cb.greaterThan(priceRoot.get("price"), 100.0));
        List<Product> overHundred = em.createQuery(priceQuery).getResultList();
        System.out.println("Over $100: " + overHundred.size());

        // Multiple predicates
        CriteriaQuery<Product> multiQuery = cb.createQuery(Product.class);
        Root<Product> multiRoot = multiQuery.from(Product.class);
        Predicate pricePredicate = cb.greaterThan(multiRoot.get("price"), 50.0);
        Predicate categoryPredicate = cb.equal(multiRoot.get("category"), "Components");
        multiQuery.select(multiRoot)
                .where(cb.and(pricePredicate, categoryPredicate));
        List<Product> components = em.createQuery(multiQuery).getResultList();
        System.out.println("Components over $50: " + components.size());

        // Order by
        CriteriaQuery<Product> orderQuery = cb.createQuery(Product.class);
        Root<Product> orderRoot = orderQuery.from(Product.class);
        orderQuery.select(orderRoot)
                .orderBy(cb.desc(orderRoot.get("price")));
        List<Product> ordered = em.createQuery(orderQuery).getResultList();
        System.out.println("Most expensive: " + ordered.get(0).getName());

        // Aggregate with Criteria
        CriteriaQuery<Double> avgQuery = cb.createQuery(Double.class);
        Root<Product> avgRoot = avgQuery.from(Product.class);
        avgQuery.select(cb.avg(avgRoot.get("price")));
        Double avg = em.createQuery(avgQuery).getSingleResult();
        System.out.println("Average price: $" + avg);

        em.close();
        emf.close();
    }

    /**
     * Demonstrates native SQL queries.
     */
    public static void demonstrateNativeQueries() {
        System.out.println("\n=== Native Queries ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();

        // Native query
        List<Object[]> rawResults = em.createNativeQuery(
                "SELECT product_name, price FROM products WHERE price > ?1", Object[].class)
                .setParameter(1, 100.0)
                .getResultList();
        System.out.println("Products over $100:");
        rawResults.forEach(row ->
                System.out.println("  " + row[0] + ": $" + row[1]));

        // Native query with entity result
        List<Product> products = em.createNativeQuery(
                "SELECT * FROM products WHERE category = ?1", Product.class)
                .setParameter(1, "Components")
                .getResultList();
        System.out.println("Components: " + products.size());

        em.close();
        emf.close();
    }

    /**
     * Runs all query demonstrations.
     */
    public static void main(String[] args) {
        demonstrateJPQL();
        demonstrateCriteriaAPI();
        demonstrateNativeQueries();
    }
}
