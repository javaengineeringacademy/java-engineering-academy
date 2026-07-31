package academy.javaengineering.oop.abstraction;

/**
 * AbstractionDemo - Demonstrates abstraction with abstract classes and interfaces.
 * 
 * <p><b>Abstraction</b> hides implementation details and shows only essential features.
 * Achieved through:
 * <ul>
 *   <li><b>Abstract classes</b>: Partial implementation, can have state</li>
 *   <li><b>Interfaces</b>: Pure contract, no state (Java 8+ default methods)</li>
 * </ul>
 * 
 * <p><b>Analogy:</b> A car's steering wheel is an abstraction - you steer without knowing
 * the internal mechanism. Different car brands implement steering differently.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class AbstractionDemo {

    private AbstractionDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Abstraction Demo ===\n");

        // Abstract class - cannot instantiate directly
        System.out.println("--- Abstract Class ---");
        Vehicle car = new Car("Toyota", "Camry", 2024);
        Vehicle bike = new Motorcycle("Honda", "CBR600", 2023);

        car.start();
        car.stop();
        car.displayInfo();

        bike.start();
        bike.stop();
        bike.displayInfo();

        // Interface
        System.out.println("\n--- Interfaces ---");
        Drawable circle = new Circle2D(5.0);
        Drawable rect = new Rectangle2D(4.0, 6.0);
        
        circle.draw();
        rect.draw();
        System.out.println("Circle area: " + ((Resizable) circle).getArea());
        System.out.println("Rectangle area: " + ((Resizable) rect).getArea());

        // Multiple interfaces
        System.out.println("\n--- Multiple Interface Implementation ---");
        SmartPhone phone = new SmartPhone("iPhone", 15);
        phone.call("555-1234");
        phone.takePhoto();
        phone.playMusic();
        phone.connectToInternet();
        phone.sendData("Hello!");

        // Polymorphism with abstraction
        System.out.println("\n--- Polymorphic Abstraction ---");
        PaymentProcessor creditCard = new CreditCardProcessor();
        PaymentProcessor paypal = new PayPalProcessor();
        
        processPayment(creditCard, 99.99);
        processPayment(paypal, 49.99);

        // Abstract class vs Interface comparison
        System.out.println("\n--- Abstract Class vs Interface ---");
        System.out.println("Abstract class: CAN have constructors, state, non-abstract methods");
        System.out.println("Interface: CANNOT have constructors (pre-Java 21), fields are constant");
        System.out.println("Abstract class: single inheritance");
        System.out.println("Interface: multiple inheritance of type");
    }

    static void processPayment(PaymentProcessor processor, double amount) {
        System.out.println("Processing $" + amount + " via " + processor.getProvider());
        processor.processPayment(amount);
    }
}