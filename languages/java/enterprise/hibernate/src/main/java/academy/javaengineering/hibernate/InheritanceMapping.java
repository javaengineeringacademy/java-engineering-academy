package academy.javaengineering.hibernate;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Demonstrates Hibernate inheritance mapping strategies.
 */
public class InheritanceMapping {

    private static SessionFactory sessionFactory;

    /**
     * Base entity using SINGLE_TABLE strategy.
     */
    @Entity
    @Table(name = "vehicles")
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
    public static abstract class Vehicle {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private Double speed;

        public Vehicle() {
        }

        public Vehicle(String name, Double speed) {
            this.name = name;
            this.speed = speed;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public abstract String getType();
    }

    /**
     * Car entity.
     */
    @Entity
    @DiscriminatorValue("CAR")
    public static class Car extends Vehicle {

        private Integer doors;

        public Car() {
        }

        public Car(String name, Double speed, Integer doors) {
            super(name, speed);
            this.doors = doors;
        }

        public Integer getDoors() {
            return doors;
        }

        public void setDoors(Integer doors) {
            this.doors = doors;
        }

        @Override
        public String getType() {
            return "CAR";
        }
    }

    /**
     * Motorcycle entity.
     */
    @Entity
    @DiscriminatorValue("MOTORCYCLE")
    public static class Motorcycle extends Vehicle {

        private Boolean hasSidecar;

        public Motorcycle() {
        }

        public Motorcycle(String name, Double speed, Boolean hasSidecar) {
            super(name, speed);
            this.hasSidecar = hasSidecar;
        }

        public Boolean getHasSidecar() {
            return hasSidecar;
        }

        public void setHasSidecar(Boolean hasSidecar) {
            this.hasSidecar = hasSidecar;
        }

        @Override
        public String getType() {
            return "MOTORCYCLE";
        }
    }

    /**
     * Truck entity.
     */
    @Entity
    @DiscriminatorValue("TRUCK")
    public static class Truck extends Vehicle {

        private Double loadCapacity;

        public Truck() {
        }

        public Truck(String name, Double speed, Double loadCapacity) {
            super(name, speed);
            this.loadCapacity = loadCapacity;
        }

        public Double getLoadCapacity() {
            return loadCapacity;
        }

        public void setLoadCapacity(Double loadCapacity) {
            this.loadCapacity = loadCapacity;
        }

        @Override
        public String getType() {
            return "TRUCK";
        }
    }

    /**
     * Initializes SessionFactory.
     */
    public static void init() {
        Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Vehicle.class)
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(Motorcycle.class)
                .addAnnotatedClass(Truck.class);
        sessionFactory = config.buildSessionFactory();
    }

    /**
     * Demonstrates SINGLE_TABLE inheritance.
     */
    public static void demonstrateSingleTable() {
        System.out.println("=== SINGLE_TABLE Inheritance ===");

        init();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Car car = new Car("Sedan", 200.0, 4);
        Motorcycle moto = new Motorcycle("Sport Bike", 250.0, false);
        Truck truck = new Truck("Heavy Duty", 120.0, 10000.0);

        session.persist(car);
        session.persist(moto);
        session.persist(truck);

        session.getTransaction().commit();

        // Query all vehicles
        List<Vehicle> vehicles = session.createQuery("FROM Vehicle", Vehicle.class)
                .getResultList();
        System.out.println("Total vehicles: " + vehicles.size());

        // Query by type
        List<Car> cars = session.createQuery("FROM Car", Car.class)
                .getResultList();
        System.out.println("Cars: " + cars.size());

        // Polymorphic query
        vehicles.forEach(v ->
                System.out.println("  " + v.getType() + ": " + v.getName()));

        session.close();
        shutdown();
    }

    /**
     * Runs inheritance mapping demonstration.
     */
    public static void main(String[] args) {
        demonstrateSingleTable();
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
