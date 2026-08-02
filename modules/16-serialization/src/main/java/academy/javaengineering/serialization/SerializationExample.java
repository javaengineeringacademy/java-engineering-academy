package academy.javaengineering.serialization;

import java.io.*;
import java.util.*;

/**
 * Comprehensive examples of Java Serialization mechanisms.
 */
public class SerializationExample {

    // ==================== BASIC SERIALIZATION ====================
    
    /**
     * Basic serializable class.
     */
    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
    
    /**
     * Demonstrates basic serialization and deserialization.
     */
    public static Person serializePerson(Person person) throws IOException, ClassNotFoundException {
        // Serialize to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(person);
        }
        
        // Deserialize from byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Person) ois.readObject();
        }
    }

    // ==================== TRANSIENT FIELDS ====================
    
    /**
     * Class with transient fields that won't be serialized.
     */
    static class Employee implements Serializable {
        private static final long serialVersionUID = 2L;
        private String name;
        private double salary;
        private transient String temporaryToken;
        private transient List<String> cachedData;
        
        public Employee(String name, double salary, String token) {
            this.name = name;
            this.salary = salary;
            this.temporaryToken = token;
            this.cachedData = Arrays.asList("cached1", "cached2");
        }
        
        public String getTemporaryToken() { return temporaryToken; }
        public List<String> getCachedData() { return cachedData; }
        
        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + 
                   ", token=" + temporaryToken + ", cached=" + cachedData + "}";
        }
    }
    
    /**
     * Demonstrates transient field handling.
     */
    public static Employee demonstrateTransient(Employee employee) 
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(employee);
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Employee) ois.readObject();
        }
    }

    // ==================== CUSTOM SERIALIZATION ====================
    
    /**
     * Class with custom serialization logic.
     */
    static class BankAccount implements Serializable {
        private static final long serialVersionUID = 3L;
        private String accountNumber;
        private double balance;
        private transient String encryptedPin;
        
        public BankAccount(String accountNumber, double balance, String pin) {
            this.accountNumber = accountNumber;
            this.balance = balance;
            this.encryptedPin = encrypt(pin);
        }
        
        // Custom writeObject method
        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.defaultWriteObject(); // Write non-transient fields
            
            // Custom logic: encrypt and write pin
            oos.writeObject(encryptedPin);
        }
        
        // Custom readObject method
        private void readObject(ObjectInputStream ois) 
                throws IOException, ClassNotFoundException {
            ois.defaultReadObject(); // Read non-transient fields
            
            // Custom logic: read and decrypt pin
            encryptedPin = (String) ois.readObject();
        }
        
        private String encrypt(String data) {
            return "ENC_" + data;
        }
        
        public String getEncryptedPin() { return encryptedPin; }
        
        @Override
        public String toString() {
            return "BankAccount{number='" + accountNumber + "', balance=" + balance + 
                   ", pin=" + encryptedPin + "}";
        }
    }
    
    /**
     * Demonstrates custom serialization.
     */
    public static BankAccount demonstrateCustomSerialization(BankAccount account) 
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(account);
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (BankAccount) ois.readObject();
        }
    }

    // ==================== EXTERNALIZABLE ====================
    
    /**
     * Class implementing Externalizable for complete control.
     */
    static class Product implements Externalizable {
        private String id;
        private String name;
        private double price;
        private Map<String, String> attributes;
        
        public Product() {} // Required no-arg constructor
        
        public Product(String id, String name, double price, Map<String, String> attributes) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.attributes = attributes;
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(id);
            out.writeUTF(name);
            out.writeDouble(price);
            
            // Custom handling for Map
            out.writeInt(attributes.size());
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeUTF(entry.getValue());
            }
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException {
            id = in.readUTF();
            name = in.readUTF();
            price = in.readDouble();
            
            // Custom handling for Map
            int size = in.readInt();
            attributes = new HashMap<>();
            for (int i = 0; i < size; i++) {
                attributes.put(in.readUTF(), in.readUTF());
            }
        }
        
        @Override
        public String toString() {
            return "Product{id='" + id + "', name='" + name + "', price=" + price + 
                   ", attrs=" + attributes + "}";
        }
    }
    
    /**
     * Demonstrates Externalizable.
     */
    public static Product demonstrateExternalizable(Product product) 
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(product);
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Product) ois.readObject();
        }
    }

    // ==================== SERIALIZATION WITH INHERITANCE ====================
    
    static abstract class Animal implements Serializable {
        protected String name;
        
        public Animal(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    static class Dog extends Animal {
        private static final long serialVersionUID = 4L;
        private String breed;
        
        public Dog(String name, String breed) {
            super(name);
            this.breed = breed;
        }
        
        @Override
        public String toString() {
            return "Dog{name='" + name + "', breed='" + breed + "'}";
        }
    }
    
    static class Cat extends Animal {
        private static final long serialVersionUID = 5L;
        private boolean isIndoor;
        
        public Cat(String name, boolean isIndoor) {
            super(name);
            this.isIndoor = isIndoor;
        }
        
        @Override
        public String toString() {
            return "Cat{name='" + name + "', indoor=" + isIndoor + "}";
        }
    }
    
    /**
     * Demonstrates serialization with inheritance.
     */
    public static Animal demonstrateInheritance(Animal animal) 
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(animal);
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Animal) ois.readObject();
        }
    }

    // ==================== FILE-BASED SERIALIZATION ====================
    
    /**
     * Demonstrates file-based serialization.
     */
    public static void demonstrateFileSerialization(Object obj, String filename) 
            throws IOException, ClassNotFoundException {
        // Serialize to file
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(obj);
        }
        
        // Deserialize from file
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            Object restored = ois.readObject();
            System.out.println("Restored from file: " + restored);
        }
    }

    // ==================== MAIN DEMONSTRATION ====================
    
    public static void main(String[] args) {
        System.out.println("=== Java Serialization Examples ===\n");
        
        try {
            // Basic serialization
            System.out.println("--- Basic Serialization ---");
            Person person = new Person("John Doe", 30);
            Person restoredPerson = serializePerson(person);
            System.out.println("Original: " + person);
            System.out.println("Restored: " + restoredPerson);
            System.out.println("Same object? " + (person == restoredPerson));
            System.out.println();
            
            // Transient fields
            System.out.println("--- Transient Fields ---");
            Employee employee = new Employee("Alice", 75000, "token123");
            Employee restoredEmployee = demonstrateTransient(employee);
            System.out.println("Original: " + employee);
            System.out.println("Restored: " + restoredEmployee);
            System.out.println("Token preserved: " + 
                (restoredEmployee.getTemporaryToken() != null));
            System.out.println();
            
            // Custom serialization
            System.out.println("--- Custom Serialization ---");
            BankAccount account = new BankAccount("123456789", 10000.00, "1234");
            BankAccount restoredAccount = demonstrateCustomSerialization(account);
            System.out.println("Original: " + account);
            System.out.println("Restored: " + restoredAccount);
            System.out.println();
            
            // Externalizable
            System.out.println("--- Externalizable ---");
            Map<String, String> attrs = Map.of("color", "red", "size", "large");
            Product product = new Product("P001", "Widget", 29.99, attrs);
            Product restoredProduct = demonstrateExternalizable(product);
            System.out.println("Original: " + product);
            System.out.println("Restored: " + restoredProduct);
            System.out.println();
            
            // Inheritance
            System.out.println("--- Serialization with Inheritance ---");
            Dog dog = new Dog("Buddy", "Golden Retriever");
            Cat cat = new Cat("Whiskers", true);
            
            Animal restoredDog = demonstrateInheritance(dog);
            Animal restoredCat = demonstrateInheritance(cat);
            
            System.out.println("Original Dog: " + dog);
            System.out.println("Restored Dog: " + restoredDog);
            System.out.println("Restored Dog type: " + restoredDog.getClass().getSimpleName());
            System.out.println("Original Cat: " + cat);
            System.out.println("Restored Cat: " + restoredCat);
            System.out.println("Restored Cat type: " + restoredCat.getClass().getSimpleName());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
