package academy.javaengineering.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 * Demonstrates JPA relationships with CRUD operations.
 */
public class RelationshipsExample {

    /**
     * Demonstrates OneToOne relationship.
     */
    public static void demonstrateOneToOne() {
        System.out.println("=== OneToOne Relationship ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        em.persist(address);

        Order order = new Order("ORD-1001");
        order.setShippingAddress(address);
        em.persist(order);
        tx.commit();

        Order found = em.find(Order.class, order.getId());
        System.out.println("Order: " + found.getOrderNumber());
        System.out.println("Shipping: " + found.getShippingAddress().getCity());

        em.close();
        emf.close();
    }

    /**
     * Demonstrates OneToMany/ManyToOne relationship.
     */
    public static void demonstrateOneToMany() {
        System.out.println("\n=== OneToMany Relationship ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Customer customer = new Customer("John Doe", "john@example.com");
        em.persist(customer);

        Order order1 = new Order("ORD-2001");
        order1.setCustomer(customer);
        em.persist(order1);

        Order order2 = new Order("ORD-2002");
        order2.setCustomer(customer);
        em.persist(order2);
        tx.commit();

        Customer found = em.find(Customer.class, customer.getId());
        System.out.println("Customer: " + found.getName());
        System.out.println("Orders: " + found.getOrders().size());

        em.close();
        emf.close();
    }

    /**
     * Demonstrates ManyToMany relationship.
     */
    public static void demonstrateManyToMany() {
        System.out.println("\n=== ManyToMany Relationship ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Tag urgentTag = new Tag("URGENT");
        Tag expressTag = new Tag("EXPRESS");
        em.persist(urgentTag);
        em.persist(expressTag);

        Order order = new Order("ORD-3001");
        order.addTag(urgentTag);
        order.addTag(expressTag);
        em.persist(order);
        tx.commit();

        Order found = em.find(Order.class, order.getId());
        System.out.println("Order: " + found.getOrderNumber());
        System.out.println("Tags: " + found.getTags().size());
        found.getTags().forEach(tag -> System.out.println("  - " + tag.getName()));

        em.close();
        emf.close();
    }

    /**
     * Demonstrates cascade operations.
     */
    public static void demonstrateCascade() {
        System.out.println("\n=== Cascade Operations ===");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-demo");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Customer customer = new Customer("Jane Smith", "jane@example.com");
        Order order = new Order("ORD-4001");
        customer.addOrder(order);
        em.persist(customer);
        tx.commit();

        System.out.println("Customer and order persisted via cascade");

        em.close();
        emf.close();
    }

    /**
     * Runs all relationship demonstrations.
     */
    public static void main(String[] args) {
        demonstrateOneToOne();
        demonstrateOneToMany();
        demonstrateManyToMany();
        demonstrateCascade();
    }
}
