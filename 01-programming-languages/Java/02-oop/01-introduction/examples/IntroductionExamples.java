package academy.javaengineering.oop.examples;

/**
 * OOP Introduction Examples - Why OOP exists and its benefits.
 * 
 * WHY OOP WAS INTRODUCED:
 * - Managing complexity in large systems
 * - Code reuse through inheritance
 * - Better modeling of real-world entities
 * - Encapsulation protects data integrity
 * 
 * TRADE-OFFS:
 * - Pros: Organized, reusable, maintainable
 * - Cons: More verbose, steeper learning curve
 */
public class IntroductionExamples {

    public static void main(String[] args) {
        System.out.println("=== OOP Introduction Examples ===\n");

        // Example 1: Procedural vs OOP
        example1_ProceduralVsOOP();

        // Example 2: Real-world Modeling
        example2_RealWorldModeling();

        // Example 3: Encapsulation
        example3_Encapsulation();
    }

    /**
     * WHY: OOP organizes code around objects, not functions.
     * 
     * ENGINEERING DECISION: Use OOP for complex systems with many entities.
     */
    private static void example1_ProceduralVsOOP() {
        System.out.println("--- Example 1: Procedural vs OOP ---");

        // Procedural approach (bad for complex systems)
        String[] names = {"Alice", "Bob", "Charlie"};
        int[] ages = {25, 30, 35};

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + " is " + ages[i] + " years old");
        }

        // OOP approach (better for complex systems)
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 35);

        p1.displayInfo();
        p2.displayInfo();
        p3.displayInfo();
    }

    /**
     * WHY: OOP models real-world entities as objects.
     * 
     * ENGINEERING DECISION: Identify nouns in requirements → Classes.
     */
    private static void example2_RealWorldModeling() {
        System.out.println("\n--- Example 2: Real-world Modeling ---");

        Car car = new Car("Toyota", "Camry", 2024);
        car.displayInfo();
        car.accelerate(60);
        car.brake(20);
        car.displayInfo();
    }

    /**
     * WHY: Encapsulation protects internal state.
     * 
     * ENGINEERING DECISION: Always use private fields with public getters/setters.
     */
    private static void example3_Encapsulation() {
        System.out.println("\n--- Example 3: Encapsulation ---");

        BankAccount account = new BankAccount("123456", 1000);
        System.out.println("Initial balance: " + account.getBalance());

        account.deposit(500);
        System.out.println("After deposit: " + account.getBalance());

        account.withdraw(200);
        System.out.println("After withdrawal: " + account.getBalance());
    }

    // Supporting classes
    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void displayInfo() {
            System.out.println(name + " is " + age + " years old");
        }
    }

    static class Car {
        private String make;
        private String model;
        private int year;
        private int speed;

        public Car(String make, String model, int year) {
            this.make = make;
            this.model = model;
            this.year = year;
            this.speed = 0;
        }

        public void accelerate(int amount) {
            speed += amount;
            System.out.println("Accelerated to " + speed + " km/h");
        }

        public void brake(int amount) {
            speed = Math.max(0, speed - amount);
            System.out.println("Slowed to " + speed + " km/h");
        }

        public void displayInfo() {
            System.out.println(year + " " + make + " " + model + " @ " + speed + " km/h");
        }
    }

    static class BankAccount {
        private String accountNumber;
        private double balance;

        public BankAccount(String accountNumber, double initialBalance) {
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            }
        }

        public void withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
            }
        }
    }
}
