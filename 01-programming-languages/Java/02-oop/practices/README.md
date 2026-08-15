# OOP Exercises

Practice exercises covering object-oriented programming concepts: classes, inheritance, polymorphism, encapsulation, and abstraction.

---

## Classes (3 Exercises)

### Exercise 1: Bank Account

**Problem:** Create a `BankAccount` class with the following features:
- Private fields: accountNumber (String), balance (double), owner (String)
- Constructor to initialize all fields
- Methods: deposit(), withdraw(), getBalance(), getAccountDetails()
- Validate that deposits and withdrawals cannot be negative amounts
- Withdrawal cannot exceed balance

**Expected Behavior:**
```java
BankAccount account = new BankAccount("ACC001", 1000.0, "John Doe");
account.deposit(500.0);          // balance: 1500.0
account.withdraw(200.0);         // balance: 1300.0
account.withdraw(2000.0);        // Insufficient funds
account.deposit(-100.0);         // Invalid amount
System.out.println(account);     // BankAccount[ACC001, John Doe, $1300.00]
```

**Classes to Create:**
- `BankAccount` — main class with encapsulation and validation

**Solution Reference:** `oop/exercises/BankAccount.java`

---

### Exercise 2: Student Management

**Problem:** Create a `Student` class that tracks student information and grades. Include methods to add grades, calculate GPA, and determine honor status.

**Expected Behavior:**
```java
Student student = new Student("S001", "Alice Smith");
student.addGrade(3.8);   // A-
student.addGrade(4.0);   // A
student.addGrade(3.5);   // B+
System.out.println(student.getGPA());        // 3.77
System.out.println(student.getHonorStatus()); // "Dean's List"
System.out.println(student);                 // Student[S001, Alice Smith, GPA: 3.77]
```

**Classes to Create:**
- `Student` — student info with grade tracking and GPA calculation

**Solution Reference:** `oop/exercises/Student.java`

---

### Exercise 3: Rectangle Utilities

**Problem:** Create a `Rectangle` class with methods to calculate area, perimeter, and check if it's a square. Override toString(), equals(), and hashCode().

**Expected Behavior:**
```java
Rectangle r1 = new Rectangle(5.0, 3.0);
Rectangle r2 = new Rectangle(5.0, 3.0);
Rectangle r3 = new Rectangle(4.0, 4.0);

r1.getArea();          // 15.0
r1.getPerimeter();     // 16.0
r1.isSquare();         // false
r3.isSquare();         // true
r1.equals(r2);         // true
System.out.println(r1); // Rectangle[5.0 x 3.0]
```

**Classes to Create:**
- `Rectangle` — with overridden Object methods

**Solution Reference:** `oop/exercises/Rectangle.java`

---

## Inheritance (3 Exercises)

### Exercise 4: Animal Hierarchy

**Problem:** Create an `Animal` base class and three subclasses (`Dog`, `Cat`, `Bird`). Each should have unique behaviors while sharing common properties.

**Expected Behavior:**
```java
Animal dog = new Dog("Buddy", 3, "Golden Retriever");
Animal cat = new Cat("Whiskers", 2, true);
Animal bird = new Bird("Tweety", 1, 0.5);

dog.speak();     // "Buddy barks: Woof!"
cat.speak();     // "Whiskers meows: Meow!"
bird.speak();    // "Tweety chirps: Tweet!"

dog.move();      // "Buddy runs on 4 legs"
cat.move();      // "Whiskers prowls silently"
bird.move();     // "Tweety flies with wings"
```

**Classes to Create:**
- `Animal` — abstract base class
- `Dog` — subclass with bark and run
- `Cat` — subclass with meow and prowl
- `Bird` — subclass with chirp and fly

**Solution Reference:** `oop/exercises/inheritance/AnimalHierarchy.java`

---

### Exercise 5: Shape Hierarchy

**Problem:** Create a `Shape` abstract class with concrete subclasses for different shapes. Each shape should calculate its area and perimeter differently.

**Expected Behavior:**
```java
Shape circle = new Circle(5.0);
Shape rectangle = new Rectangle(4.0, 6.0);
Shape triangle = new Triangle(3.0, 4.0, 5.0);

circle.getArea();        // 78.54
circle.getPerimeter();   // 31.42
rectangle.getArea();     // 24.0
triangle.getArea();      // 6.0 (Heron's formula)

// Polymorphic collection
Shape[] shapes = {circle, rectangle, triangle};
for (Shape s : shapes) {
    System.out.println(s.getName() + ": " + s.getArea());
}
```

**Classes to Create:**
- `Shape` — abstract class with common methods
- `Circle` — circle implementation
- `Rectangle` — rectangle implementation
- `Triangle` — triangle implementation

**Solution Reference:** `oop/exercises/inheritance/ShapeHierarchy.java`

---

### Exercise 6: Employee Types

**Problem:** Create an `Employee` base class with subclasses for different employee types (`FullTimeEmployee`, `PartTimeEmployee`, `Contractor`). Each type has different salary calculation methods.

**Expected Behavior:**
```java
Employee fullTime = new FullTimeEmployee("E001", "John", 75000.0, 5000.0);
Employee partTime = new PartTimeEmployee("E002", "Jane", 25.0, 20.0);
Employee contractor = new Contractor("E003", "Bob", 100.0, 160.0);

fullTime.calculateSalary();   // 80000.0 (salary + bonus)
partTime.calculateSalary();   // 2000.0 (hourly * hours)
contractor.calculateSalary(); // 16000.0 (rate * hours, no benefits)
```

**Classes to Create:**
- `Employee` — base class
- `FullTimeEmployee` — salary + bonus
- `PartTimeEmployee` — hourly rate
- `Contractor` — contract rate

**Solution Reference:** `oop/exercises/inheritance/EmployeeTypes.java`

---

## Polymorphism (3 Exercises)

### Exercise 7: Payment Processing

**Problem:** Create a polymorphic payment system where different payment methods process transactions differently.

**Expected Behavior:**
```java
Payment creditCard = new CreditCardPayment("1234-5678-9012-3456", "12/25");
Payment paypal = new PayPalPayment("user@email.com");
Payment crypto = new CryptoWalletPayment("0xABC123...");

PaymentProcessor processor = new PaymentProcessor();
processor.processPayment(creditCard, 100.0);  // Processing credit card...
processor.processPayment(paypal, 50.0);      // Processing PayPal...
processor.processPayment(crypto, 200.0);     // Processing crypto...
```

**Classes to Create:**
- `Payment` — abstract payment class
- `CreditCardPayment` — credit card processing
- `PayPalPayment` — PayPal processing
- `CryptoWalletPayment` — crypto processing
- `PaymentProcessor` — processes any Payment type

**Solution Reference:** `oop/exercises/polymorphism/PaymentProcessing.java`

---

### Exercise 8: Notification System

**Problem:** Create a notification system that sends messages via different channels (Email, SMS, Push) using polymorphism.

**Expected Behavior:**
```java
Notification email = new EmailNotification("user@example.com");
Notification sms = new SMSNotification("+1234567890");
Notification push = new PushNotification("device_token_abc");

NotificationService service = new NotificationService();
service.send(email, "Welcome!");    // Sending email to user@example.com: Welcome!
service.send(sms, "Alert!");        // Sending SMS to +1234567890: Alert!
service.send(push, "Update!");      // Sending push to device_token_abc: Update!
```

**Classes to Create:**
- `Notification` — abstract notification class
- `EmailNotification` — email implementation
- `SMSNotification` — SMS implementation
- `PushNotification` — push notification implementation
- `NotificationService` — sends any Notification type

**Solution Reference:** `oop/exercises/polymorphism/NotificationSystem.java`

---

### Exercise 9: Shape Calculator (Polymorphic)

**Problem:** Create a calculator that works with different shape types using method overloading and polymorphism.

**Expected Behavior:**
```java
ShapeCalculator calc = new ShapeCalculator();

// Using overloaded methods
double area1 = calc.calculateArea(new Circle(5.0));           // 78.54
double area2 = calc.calculateArea(new Rectangle(4.0, 6.0));  // 24.0
double area3 = calc.calculateArea(new Triangle(3, 4, 5));    // 6.0

// Polymorphic collection
List<Shape> shapes = List.of(new Circle(5), new Rectangle(4,6), new Triangle(3,4,5));
double totalArea = calc.totalArea(shapes);                    // 108.54
```

**Classes to Create:**
- `ShapeCalculator` — calculator with overloaded methods
- `Circle`, `Rectangle`, `Triangle` — shape classes

**Solution Reference:** `oop/exercises/polymorphism/ShapeCalculator.java`

---

## Encapsulation (3 Exercises)

### Exercise 10: Input Validation

**Problem:** Create a `User` class with strict encapsulation. All fields should be private with validated setters. Include validation for age, email, and username.

**Expected Behavior:**
```java
User user = new User();
user.setAge(25);           // valid
user.setAge(-5);           // throws IllegalArgumentException
user.setAge(150);          // throws IllegalArgumentException
user.setEmail("a@b.com");  // valid
user.setEmail("invalid");  // throws IllegalArgumentException
user.setUsername("john_d"); // valid
user.setUsername("ab");     // throws IllegalArgumentException (too short)
```

**Classes to Create:**
- `User` — with validated private fields and getters/setters

**Solution Reference:** `oop/exercises/encapsulation/UserValidation.java`

---

### Exercise 11: Access Control

**Problem:** Create a `SecureVault` class that demonstrates different access levels. Use `private`, `protected`, and `public` modifiers appropriately.

**Expected Behavior:**
```java
SecureVault vault = new SecureVault("secret123", 1000000);
vault.getBalance();          // public - accessible
vault.authenticate("wrong"); // public - accessible
// vault.secretKey;          // private - not accessible from outside
// vault.internalLog;        // protected - accessible in subclass only
```

**Classes to Create:**
- `SecureVault` — with different access levels
- `VaultTester` — subclass to test protected access

**Solution Reference:** `oop/exercises/encapsulation/AccessControl.java`

---

### Exercise 12: Immutable Class

**Problem:** Create an immutable `Point3D` class that cannot be modified after creation. Include methods that appear to modify but actually return new instances.

**Expected Behavior:**
```java
Point3D p1 = new Point3D(1.0, 2.0, 3.0);
Point3D p2 = p1.translate(1.0, 1.0, 1.0);

p1.getX();  // 1.0 (unchanged)
p2.getX();  // 2.0 (new instance)
p1.equals(p2); // false

Point3D p3 = p1.scale(2.0);
p1.getX();  // 1.0 (unchanged)
p3.getX();  // 2.0 (new instance)
```

**Classes to Create:**
- `Point3D` — immutable 3D point with defensive copies

**Solution Reference:** `oop/exercises/encapsulation/ImmutablePoint.java`

---

## Abstraction (3 Exercises)

### Exercise 13: Vehicle System

**Problem:** Create an abstract `Vehicle` class that defines the contract for all vehicles. Concrete classes must implement specific behaviors.

**Expected Behavior:**
```java
Vehicle car = new Car("Toyota", "Camry", 2024, 4);
Vehicle truck = new Truck("Ford", "F-150", 2024, 1500.0);
Vehicle motorcycle = new Motorcycle("Harley", "Sportster", 2024, 600);

car.start();        // "Starting car engine..."
car.stop();         // "Stopping car engine..."
car.load(500);      // "Car cannot carry heavy loads"

truck.start();      // "Starting truck engine..."
truck.load(500);    // "Truck loaded with 500kg"

motorcycle.start(); // "Starting motorcycle engine..."
motorcycle.load(500); // "Motorcycle cannot carry heavy loads"
```

**Classes to Create:**
- `Vehicle` — abstract class with abstract methods
- `Car` — car implementation
- `Truck` — truck implementation
- `Motorcycle` — motorcycle implementation

**Solution Reference:** `oop/exercises/abstraction/VehicleSystem.java`

---

### Exercise 14: Payment Gateway

**Problem:** Create an abstract `PaymentGateway` class that defines the payment processing contract. Implement different gateways with their own processing logic.

**Expected Behavior:**
```java
PaymentGateway stripe = new StripeGateway("sk_test_abc");
PaymentGateway square = new SquareGateway("sq0csp_xyz");

stripe.authorize(100.0);      // Stripe: Authorizing $100.00...
stripe.capture("txn_123");    // Stripe: Capturing transaction txn_123

square.authorize(50.0);       // Square: Preparing authorization for $50.00
square.capture("txn_456");    // Square: Processing capture for txn_456
```

**Classes to Create:**
- `PaymentGateway` — abstract gateway class
- `StripeGateway` — Stripe implementation
- `SquareGateway` — Square implementation

**Solution Reference:** `oop/exercises/abstraction/PaymentGatewaySystem.java`

---

### Exercise 15: Notification Service

**Problem:** Create an abstract `NotificationService` with different notification strategies. Each strategy handles delivery differently.

**Expected Behavior:**
```java
NotificationStrategy email = new EmailStrategy("smtp.server.com");
NotificationStrategy sms = new SMSStrategy("sms.provider.com");
NotificationStrategy webhook = new WebhookStrategy("https://api.example.com/hooks");

NotificationManager manager = new NotificationManager();
manager.setStrategy(email);
manager.notify("user@test.com", "Your order shipped!");

manager.setStrategy(sms);
manager.notify("+1234567890", "Your code is 123456");

manager.setStrategy(webhook);
manager.notify("https://hooks.example.com", "{\"event\":\"created\"}");
```

**Classes to Create:**
- `NotificationStrategy` — abstract strategy class
- `EmailStrategy` — email delivery
- `SMSStrategy` — SMS delivery
- `WebhookStrategy` — webhook delivery
- `NotificationManager` — uses strategy pattern

**Solution Reference:** `oop/exercises/abstraction/NotificationServiceSystem.java`

---

## Solutions

All solutions are provided in the `solutions/` directory within each exercise package. To compile and run:

```bash
javac -d out oop/exercises/*.java
java -cp out oop.exercises.ExerciseClassName
```
