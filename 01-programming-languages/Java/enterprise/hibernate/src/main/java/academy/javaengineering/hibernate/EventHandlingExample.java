package academy.javaengineering.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.service.ServiceRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates Hibernate event handling with listeners.
 */
public class EventHandlingExample {

    private static SessionFactory sessionFactory;
    private static final List<String> eventLog = new ArrayList<>();

    /**
     * Custom PreInsertEventListener.
     */
    public static class AuditPreInsertListener implements PreInsertEventListener {

        @Override
        public boolean onPreInsert(PreInsertEvent event) {
            String entityName = event.getEntity().getClass().getSimpleName();
            eventLog.add("PRE-INSERT: " + entityName + " at " + LocalDateTime.now());
            System.out.println("PreInsert: " + entityName);
            return false;
        }
    }

    /**
     * Custom PreUpdateEventListener.
     */
    public static class AuditPreUpdateListener implements PreUpdateEventListener {

        @Override
        public boolean onPreUpdate(PreUpdateEvent event) {
            String entityName = event.getEntity().getClass().getSimpleName();
            eventLog.add("PRE-UPDATE: " + entityName + " at " + LocalDateTime.now());
            System.out.println("PreUpdate: " + entityName);
            return false;
        }
    }

    /**
     * Custom PostLoadEventListener.
     */
    public static class AuditPostLoadListener implements PostLoadEventListener {

        @Override
        public void onPostLoad(PostLoadEvent event) {
            String entityName = event.getEntity().getClass().getSimpleName();
            eventLog.add("POST-LOAD: " + entityName + " at " + LocalDateTime.now());
            System.out.println("PostLoad: " + entityName);
        }
    }

    /**
     * Initializes SessionFactory with event listeners.
     */
    public static void init() {
        Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class);

        ServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .applySettings(config.getProperties())
                .build();

        MetadataSources metadataSources = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Category.class);

        Metadata metadata = metadataSources.getMetadataBuilder().build();

        EventListenerRegistry registry = ((SessionFactoryImpl) metadata.buildSessionFactory())
                .getServiceRegistry()
                .getService(EventListenerRegistry.class);

        registry.appendListeners(EventType.PRE_INSERT, new AuditPreInsertListener());
        registry.appendListeners(EventType.PRE_UPDATE, new AuditPreUpdateListener());
        registry.appendListeners(EventType.POST_LOAD, new AuditPostLoadListener());

        sessionFactory = metadata.buildSessionFactory();
    }

    /**
     * Demonstrates event listeners in action.
     */
    public static void demonstrateEventListeners() {
        System.out.println("=== Event Handling ===");

        init();
        eventLog.clear();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // PreInsert event fires
        Product product = new Product("Event Product", 99.99, "Events");
        session.persist(product);

        session.getTransaction().commit();

        // PreUpdate and PostLoad events fire
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product loaded = session.get(Product.class, product.getId());
        loaded.setPrice(79.99);
        session.merge(loaded);
        session.getTransaction().commit();

        // PostLoad fires again
        session = sessionFactory.openSession();
        Product reloaded = session.get(Product.class, product.getId());

        System.out.println("\nEvent Log:");
        eventLog.forEach(System.out::println);

        session.close();
        shutdown();
    }

    /**
     * Runs event handling demonstration.
     */
    public static void main(String[] args) {
        demonstrateEventListeners();
    }

    /**
     * Returns the event log for testing.
     */
    public static List<String> getEventLog() {
        return List.copyOf(eventLog);
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
