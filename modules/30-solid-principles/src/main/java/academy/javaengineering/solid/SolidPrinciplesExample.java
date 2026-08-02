package academy.javaengineering.solid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.Instant;

/**
 * Comprehensive SOLID principles example demonstrating all five principles.
 */
public class SolidPrinciplesExample {

    public static void main(String[] args) {
        System.out.println("=== SOLID Principles Example ===\n");
        
        singleResponsibilityExample();
        openClosedExample();
        liskovSubstitutionExample();
        interfaceSegregationExample();
        dependencyInversionExample();
        
        System.out.println("\n=== Examples Complete ===");
    }

    // ==========================================
    // Single Responsibility Principle (SRP)
    // ==========================================
    
    private static void singleResponsibilityExample() {
        System.out.println("--- Single Responsibility Principle ---");
        
        // Bad: Multiple responsibilities in one class
        System.out.println("Bad example:");
        UserManager badManager = new UserManager();
        badManager.createUser("John", "john@example.com");
        
        // Good: Separate classes for each responsibility
        System.out.println("Good example:");
        UserService service = new UserService(new UserRepository());
        EmailService emailService = new EmailService();
        
        User user = service.createUser("Jane", "jane@example.com");
        emailService.sendWelcomeEmail(user);
    }

    // ==========================================
    // Open/Closed Principle (OCP)
    // ==========================================
    
    private static void openClosedExample() {
        System.out.println("\n--- Open/Closed Principle ---");
        
        // Good: New shapes can be added without modifying AreaCalculator
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5.0));
        shapes.add(new Rectangle(4.0, 6.0));
        shapes.add(new Triangle(3.0, 4.0));
        
        AreaCalculator calculator = new AreaCalculator();
        double totalArea = calculator.calculateTotalArea(shapes);
        System.out.println("Total area: " + totalArea);
        
        // New shape type can be added without modifying AreaCalculator
        shapes.add(new Pentagon(5.0));
        totalArea = calculator.calculateTotalArea(shapes);
        System.out.println("Total area with pentagon: " + totalArea);
    }

    // ==========================================
    // Liskov Substitution Principle (LSP)
    // ==========================================
    
    private static void liskovSubstitutionExample() {
        System.out.println("\n--- Liskov Substitution Principle ---");
        
        // Good: Any Bird subtype can be used where Bird is expected
        List<Bird> birds = new ArrayList<>();
        birds.add(new Sparrow());
        birds.add(new Eagle());
        birds.add(new Penguin());  // Non-flying bird
        
        BirdWatcher watcher = new BirdWatcher();
        for (Bird bird : birds) {
            watcher.observeBird(bird);
        }
    }

    // ==========================================
    // Interface Segregation Principle (ISP)
    // ==========================================
    
    private static void interfaceSegregationExample() {
        System.out.println("\n--- Interface Segregation Principle ---");
        
        // Good: Each worker implements only the interfaces it needs
        Worker humanWorker = new HumanWorker();
        Worker robotWorker = new RobotWorker();
        Worker managerWorker = new ManagerWorker();
        
        WorkStation station = new WorkStation();
        station.assignWorker(humanWorker);
        station.assignWorker(robotWorker);
        station.assignWorker(managerWorker);
    }

    // ==========================================
    // Dependency Inversion Principle (DIP)
    // ==========================================
    
    private static void dependencyInversionExample() {
        System.out.println("\n--- Dependency Inversion Principle ---");
        
        // Good: High-level module depends on abstraction
        UserRepository repository = new InMemoryUserRepository();
        NotificationService notificationService = new EmailNotificationService();
        
        UserRegistrationService registrationService = 
            new UserRegistrationService(repository, notificationService);
        
        registrationService.registerUser("Alice", "alice@example.com");
    }

    // ==========================================
    // Supporting Classes and Interfaces
    // ==========================================
    
    // SRP: Separate classes for each responsibility
    static class User {
        private final String name;
        private final String email;
        
        User(String name, String email) {
            this.name = name;
            this.email = email;
        }
        
        String getName() { return name; }
        String getEmail() { return email; }
    }
    
    static class UserRepository {
        private final List<User> users = new ArrayList<>();
        
        void save(User user) {
            users.add(user);
            System.out.println("User saved: " + user.getName());
        }
        
        User findByEmail(String email) {
            return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        }
    }
    
    static class UserService {
        private final UserRepository repository;
        
        UserService(UserRepository repository) {
            this.repository = repository;
        }
        
        User createUser(String name, String email) {
            User user = new User(name, email);
            repository.save(user);
            System.out.println("User created: " + name);
            return user;
        }
    }
    
    static class EmailService {
        void sendWelcomeEmail(User user) {
            System.out.println("Welcome email sent to: " + user.getEmail());
        }
    }
    
    // Bad example - violates SRP
    static class UserManager {
        void createUser(String name, String email) {
            // Creates user
            User user = new User(name, email);
            System.out.println("User created: " + name);
            
            // Saves user
            System.out.println("User saved: " + name);
            
            // Sends email
            System.out.println("Welcome email sent to: " + email);
            
            // Generates report
            System.out.println("User report generated for: " + name);
        }
    }
    
    // OCP: Shape hierarchy
    interface Shape {
        double area();
        String getType();
    }
    
    static class Circle implements Shape {
        private final double radius;
        
        Circle(double radius) {
            this.radius = radius;
        }
        
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
        
        @Override
        public String getType() {
            return "Circle";
        }
    }
    
    static class Rectangle implements Shape {
        private final double width;
        private final double height;
        
        Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }
        
        @Override
        public double area() {
            return width * height;
        }
        
        @Override
        public String getType() {
            return "Rectangle";
        }
    }
    
    static class Triangle implements Shape {
        private final double base;
        private final double height;
        
        Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }
        
        @Override
        public double area() {
            return 0.5 * base * height;
        }
        
        @Override
        public String getType() {
            return "Triangle";
        }
    }
    
    // New shape added without modifying AreaCalculator
    static class Pentagon implements Shape {
        private final double side;
        
        Pentagon(double side) {
            this.side = side;
        }
        
        @Override
        public double area() {
            return 0.25 * Math.sqrt(5 * (5 + 2 * Math.sqrt(5))) * side * side;
        }
        
        @Override
        public String getType() {
            return "Pentagon";
        }
    }
    
    static class AreaCalculator {
        double calculateTotalArea(List<Shape> shapes) {
            return shapes.stream()
                .mapToDouble(Shape::area)
                .sum();
        }
    }
    
    // LSP: Bird hierarchy
    static abstract class Bird {
        protected String name;
        
        Bird(String name) {
            this.name = name;
        }
        
        abstract void move();
        
        String getName() {
            return name;
        }
    }
    
    static class FlyingBird extends Bird {
        FlyingBird(String name) {
            super(name);
        }
        
        @Override
        void move() {
            System.out.println(name + " is flying");
        }
    }
    
    static class Sparrow extends FlyingBird {
        Sparrow() {
            super("Sparrow");
        }
    }
    
    static class Eagle extends FlyingBird {
        Eagle() {
            super("Eagle");
        }
    }
    
    static class Penguin extends Bird {
        Penguin() {
            super("Penguin");
        }
        
        @Override
        void move() {
            System.out.println(name + " is swimming");
        }
    }
    
    static class BirdWatcher {
        void observeBird(Bird bird) {
            System.out.println("Observing " + bird.getName());
            bird.move();
        }
    }
    
    // ISP: Segregated interfaces
    interface Workable {
        void work();
    }
    
    interface Feedable {
        void feed();
    }
    
    interface Sleepable {
        void sleep();
    }
    
    interface Manageable {
        void manage();
    }
    
    // Each worker implements only what it needs
    static class HumanWorker implements Workable, Feedable, Sleepable {
        @Override
        public void work() {
            System.out.println("Human worker is working");
        }
        
        @Override
        public void feed() {
            System.out.println("Human worker is eating");
        }
        
        @Override
        public void sleep() {
            System.out.println("Human worker is sleeping");
        }
    }
    
    static class RobotWorker implements Workable {
        @Override
        public void work() {
            System.out.println("Robot worker is working");
        }
    }
    
    static class ManagerWorker implements Workable, Feedable, Sleepable, Manageable {
        @Override
        public void work() {
            System.out.println("Manager is working");
        }
        
        @Override
        public void feed() {
            System.out.println("Manager is eating");
        }
        
        @Override
        public void sleep() {
            System.out.println("Manager is sleeping");
        }
        
        @Override
        public void manage() {
            System.out.println("Manager is managing");
        }
    }
    
    static class WorkStation {
        void assignWorker(Worker worker) {
            if (worker instanceof Workable) {
                ((Workable) worker).work();
            }
        }
    }
    
    interface Worker {}
    
    // DIP: High-level modules depend on abstractions
    interface UserRepositoryInterface {
        void save(User user);
        User findById(String id);
    }
    
    interface NotificationService {
        void sendNotification(String to, String message);
    }
    
    static class InMemoryUserRepository implements UserRepositoryInterface {
        private final Map<String, User> users = new HashMap<>();
        
        @Override
        public void save(User user) {
            users.put(user.getEmail(), user);
            System.out.println("User saved to in-memory repository: " + user.getName());
        }
        
        @Override
        public User findById(String id) {
            return users.get(id);
        }
    }
    
    static class EmailNotificationService implements NotificationService {
        @Override
        public void sendNotification(String to, String message) {
            System.out.println("Email sent to " + to + ": " + message);
        }
    }
    
    static class UserRegistrationService {
        private final UserRepositoryInterface userRepository;
        private final NotificationService notificationService;
        
        UserRegistrationService(UserRepositoryInterface userRepository, 
                              NotificationService notificationService) {
            this.userRepository = userRepository;
            this.notificationService = notificationService;
        }
        
        void registerUser(String name, String email) {
            User user = new User(name, email);
            userRepository.save(user);
            notificationService.sendNotification(email, "Welcome to our platform!");
            System.out.println("User registered: " + name);
        }
    }
}