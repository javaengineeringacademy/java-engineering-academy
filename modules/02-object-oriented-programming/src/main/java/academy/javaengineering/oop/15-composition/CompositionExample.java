package academy.javaengineering.oop.`15-composition`;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates composition over inheritance - favoring object composition.
 *
 * <p>Composition is a "has-a" relationship where objects contain other objects
 * rather than inheriting from them. This provides greater flexibility, easier
 * testing, and avoids the fragile base class problem.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Composition ("has-a") vs Inheritance ("is-a")</li>
 *   <li>Delegation pattern</li>
 *   <li>Strategy pattern via composition</li>
 *   <li>Immutable composed objects</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CompositionExample {

    // ==================== INHERITANCE (for comparison) ====================

    /** Inheritance approach - rigid, fragile base class problem. */
    @SuppressWarnings("unused")
    public static abstract class BaseNotification {
        protected String recipient;

        public void setRecipient(String recipient) { this.recipient = recipient; }
        public abstract void send(String message);
    }

    // ==================== COMPOSITION (preferred) ====================

    /** Composable behavior: message formatting strategy. */
    public interface MessageFormatter {
        String format(String message);
    }

    public static class PlainTextFormatter implements MessageFormatter {
        @Override
        public String format(String message) { return message; }
    }

    public static class HtmlFormatter implements MessageFormatter {
        @Override
        public String format(String message) {
            return "<html><body><p>%s</p></body></html>".formatted(message);
        }
    }

    /** Composable behavior: delivery strategy. */
    public interface DeliveryStrategy {
        String deliver(String recipient, String formattedMessage);
    }

    public static class EmailDelivery implements DeliveryStrategy {
        @Override
        public String deliver(String recipient, String formattedMessage) {
            return "[EMAIL] To: %s | Body: %s".formatted(recipient, formattedMessage);
        }
    }

    public static class SmsDelivery implements DeliveryStrategy {
        @Override
        public String deliver(String recipient, String formattedMessage) {
            String truncated = formattedMessage.length() > 160
                    ? formattedMessage.substring(0, 157) + "..."
                    : formattedMessage;
            return "[SMS] To: %s | Text: %s".formatted(recipient, truncated);
        }
    }

    /**
     * Notification service using composition - flexible and testable.
     * Has-a formatter, has-a delivery strategy.
     */
    public static class NotificationService {
        private final MessageFormatter formatter;
        private final DeliveryStrategy delivery;
        private final List<String> deliveryLog = new ArrayList<>();

        public NotificationService(MessageFormatter formatter, DeliveryStrategy delivery) {
            this.formatter = formatter;
            this.delivery = delivery;
        }

        public String send(String recipient, String message) {
            String formatted = formatter.format(message);
            String result = delivery.deliver(recipient, formatted);
            deliveryLog.add(result);
            return result;
        }

        public List<String> getDeliveryLog() { return List.copyOf(deliveryLog); }
    }

    // ==================== COMPOSITION: Engine components ====================

    /** Engine component - composed into a car. */
    public static class Engine {
        private final String type;
        private final int horsepower;
        private boolean running;

        public Engine(String type, int horsepower) {
            this.type = type;
            this.horsepower = horsepower;
            this.running = false;
        }

        public void start() { running = true; }
        public void stop() { running = false; }
        public boolean isRunning() { return running; }
        public String getType() { return type; }
        public int getHorsepower() { return horsepower; }

        @Override
        public String toString() {
            return "Engine{type='%s', hp=%d, running=%s}".formatted(type, horsepower, running);
        }
    }

    /** Transmission component - composed into a car. */
    public static class Transmission {
        private final String type;
        private int currentGear;

        public Transmission(String type) {
            this.type = type;
            this.currentGear = 0; // Neutral
        }

        public void shiftUp() { currentGear = Math.min(currentGear + 1, 6); }
        public void shiftDown() { currentGear = Math.max(currentGear - 1, 0); }
        public int getCurrentGear() { return currentGear; }
        public String getType() { return type; }

        @Override
        public String toString() {
            return "Transmission{type='%s', gear=%d}".formatted(type, currentGear);
        }
    }

    /** Navigation system component - composed into a car. */
    public static class NavigationSystem {
        private String currentLocation;
        private String destination;

        public void setCurrentLocation(String location) { this.currentLocation = location; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getCurrentLocation() { return currentLocation; }
        public String getDestination() { return destination; }

        public String getDirections() {
            if (currentLocation == null || destination == null) {
                return "Set location and destination first";
            }
            return "Navigate from %s to %s".formatted(currentLocation, destination);
        }
    }

    /**
     * Car built via composition - has-a Engine, has-a Transmission, has-a Navigation.
     * Flexible: swap components at runtime.
     */
    public static class Car {
        private final String make;
        private final String model;
        private final Engine engine;
        private final Transmission transmission;
        private final NavigationSystem navigation;

        public Car(String make, String model, Engine engine,
                   Transmission transmission, NavigationSystem navigation) {
            this.make = make;
            this.model = model;
            this.engine = engine;
            this.transmission = transmission;
            this.navigation = navigation;
        }

        public void start() { engine.start(); }
        public void stop() { engine.stop(); }
        public boolean isRunning() { return engine.isRunning(); }
        public void shiftUp() { transmission.shiftUp(); }
        public void shiftDown() { transmission.shiftDown(); }
        public int getCurrentGear() { return transmission.getCurrentGear(); }

        public String getNavigationDirections() { return navigation.getDirections(); }

        @Override
        public String toString() {
            return "%s %s [%s | %s]".formatted(make, model, engine.getType(), transmission.getType());
        }
    }

    // ==================== COMPOSITION: Report builder ====================

    /** Composable report section. */
    public interface ReportSection {
        String render();
    }

    public static class HeaderSection implements ReportSection {
        private final String title;

        public HeaderSection(String title) { this.title = title; }

        @Override
        public String render() { return "=== %s ===".formatted(title); }
    }

    public static class TableSection implements ReportSection {
        private final String[] headers;
        private final List<String[]> rows;

        public TableSection(String[] headers, List<String[]> rows) {
            this.headers = headers;
            this.rows = rows;
        }

        @Override
        public String render() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.join(" | ", headers)).append("\n");
            sb.append("-".repeat(40)).append("\n");
            for (String[] row : rows) {
                sb.append(String.join(" | ", row)).append("\n");
            }
            return sb.toString();
        }
    }

    /** Report built by composing sections. */
    public static class Report {
        private final List<ReportSection> sections = new ArrayList<>();

        public void addSection(ReportSection section) { sections.add(section); }

        public String generate() {
            StringBuilder sb = new StringBuilder();
            for (ReportSection section : sections) {
                sb.append(section.render()).append("\n\n");
            }
            return sb.toString().trim();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Composition over Inheritance Demo ===\n");

        // Composition: Notification service
        System.out.println("--- Composition: Notification Service ---");
        NotificationService emailHtml = new NotificationService(new HtmlFormatter(), new EmailDelivery());
        NotificationService smsPlain = new NotificationService(new PlainTextFormatter(), new SmsDelivery());

        System.out.println(emailHtml.send("alice@example.com", "Your order is ready!"));
        System.out.println(smsPlain.send("+15551234567", "Your OTP is 4829"));

        // Composition: Car components
        System.out.println("\n--- Composition: Car Components ---");
        Engine v6 = new Engine("V6", 300);
        Transmission auto = new Transmission("Automatic");
        NavigationSystem nav = new NavigationSystem();

        Car car = new Car("Toyota", "Camry", v6, auto, nav);
        System.out.println("Car: " + car);
        System.out.println("Engine: " + v6);
        System.out.println("Transmission: " + auto);

        car.start();
        car.shiftUp();
        car.shiftUp();
        System.out.println("Running: " + car.isRunning() + ", Gear: " + car.getCurrentGear());

        nav.setCurrentLocation("Home");
        nav.setDestination("Office");
        System.out.println("Navigation: " + car.getNavigationDirections());

        // Composition: Report builder
        System.out.println("\n--- Composition: Report Builder ---");
        Report report = new Report();
        report.addSection(new HeaderSection("Quarterly Sales Report"));
        report.addSection(new TableSection(
                new String[]{"Product", "Units", "Revenue"},
                List.of(
                        new String[]{"Widget A", "150", "$4,500"},
                        new String[]{"Widget B", "89", "$2,670"},
                        new String[]{"Service C", "45", "$9,000"}
                )
        ));
        report.addSection(new HeaderSection("Summary"));
        report.addSection(new TableSection(
                new String[]{"Metric", "Value"},
                List.of(
                        new String[]{"Total Units", "284"},
                        new String[]{"Total Revenue", "$16,170"}
                )
        ));

        System.out.println(report.generate());
    }
}
