package academy.javaengineering.patterns.decorator;

/**
 * Demonstrates the Decorator design pattern with stackable decorators.
 *
 * <h3>Decorator Flavors:</h3>
 * <ol>
 *   <li>Simple Decorator</li>
 *   <li>Stackable Decorators (Coffee Shop)</li>
 *   <li>I/O Stream-style Decorator</li>
 * </ol>
 */
public class DecoratorExample {

    // ========================================
    // Component Interface
    // ========================================
    public interface Coffee {
        double getCost();
        String getDescription();
    }

    // ========================================
    // Base Component
    // ========================================
    public static class SimpleCoffee implements Coffee {
        @Override
        public double getCost() { return 5.00; }
        
        @Override
        public String getDescription() { return "Simple coffee"; }
    }

    public static class Espresso implements Coffee {
        @Override
        public double getCost() { return 7.00; }
        
        @Override
        public String getDescription() { return "Espresso"; }
    }

    // ========================================
    // Abstract Decorator
    // ========================================
    public abstract static class CoffeeDecorator implements Coffee {
        protected final Coffee coffee;
        
        protected CoffeeDecorator(Coffee coffee) {
            this.coffee = coffee;
        }
        
        @Override
        public double getCost() { return coffee.getCost(); }
        
        @Override
        public String getDescription() { return coffee.getDescription(); }
    }

    // ========================================
    // Concrete Decorators
    // ========================================
    public static class MilkDecorator extends CoffeeDecorator {
        public MilkDecorator(Coffee coffee) { super(coffee); }
        
        @Override
        public double getCost() { return super.getCost() + 1.50; }
        
        @Override
        public String getDescription() { return super.getDescription() + ", milk"; }
    }

    public static class SugarDecorator extends CoffeeDecorator {
        public SugarDecorator(Coffee coffee) { super(coffee); }
        
        @Override
        public double getCost() { return super.getCost() + 0.75; }
        
        @Override
        public String getDescription() { return super.getDescription() + ", sugar"; }
    }

    public static class WhipCreamDecorator extends CoffeeDecorator {
        public WhipCreamDecorator(Coffee coffee) { super(coffee); }
        
        @Override
        public double getCost() { return super.getCost() + 1.00; }
        
        @Override
        public String getDescription() { return super.getDescription() + ", whip cream"; }
    }

    public static class MochaDecorator extends CoffeeDecorator {
        public MochaDecorator(Coffee coffee) { super(coffee); }
        
        @Override
        public double getCost() { return super.getCost() + 2.00; }
        
        @Override
        public String getDescription() { return super.getDescription() + ", mocha"; }
    }

    public static class CaramelDecorator extends CoffeeDecorator {
        public CaramelDecorator(Coffee coffee) { super(coffee); }
        
        @Override
        public double getCost() { return super.getCost() + 1.25; }
        
        @Override
        public String getDescription() { return super.getDescription() + ", caramel"; }
    }

    // ========================================
    // I/O Stream-style Decorator Example
    // ========================================
    public interface DataSource {
        void writeData(String data);
        String readData();
    }

    public static class FileDataSource implements DataSource {
        private String data;
        
        public FileDataSource(String data) {
            this.data = data;
        }
        
        @Override
        public void writeData(String data) {
            this.data = data;
            System.out.println("FileDataSource: Writing - " + data);
        }
        
        @Override
        public String readData() {
            System.out.println("FileDataSource: Reading - " + data);
            return data;
        }
    }

    public static class DataSourceDecorator implements DataSource {
        protected DataSource wrappee;
        
        public DataSourceDecorator(DataSource source) {
            this.wrappee = source;
        }
        
        @Override
        public void writeData(String data) { wrappee.writeData(data); }
        
        @Override
        public String readData() { return wrappee.readData(); }
    }

    public static class EncryptionDecorator extends DataSourceDecorator {
        public EncryptionDecorator(DataSource source) { super(source); }
        
        @Override
        public void writeData(String data) {
            String encrypted = encrypt(data);
            System.out.println("EncryptionDecorator: Encrypting data");
            super.writeData(encrypted);
        }
        
        @Override
        public String readData() {
            String data = super.readData();
            String decrypted = decrypt(data);
            System.out.println("EncryptionDecorator: Decrypting data");
            return decrypted;
        }
        
        private String encrypt(String data) {
            return "ENC(" + data + ")";
        }
        
        private String decrypt(String data) {
            return data.replaceAll("ENC\\((.*)\\)", "$1");
        }
    }

    public static class CompressionDecorator extends DataSourceDecorator {
        public CompressionDecorator(DataSource source) { super(source); }
        
        @Override
        public void writeData(String data) {
            String compressed = compress(data);
            System.out.println("CompressionDecorator: Compressing data");
            super.writeData(compressed);
        }
        
        @Override
        public String readData() {
            String data = super.readData();
            String decompressed = decompress(data);
            System.out.println("CompressionDecorator: Decompressing data");
            return decompressed;
        }
        
        private String compress(String data) {
            return "ZIP(" + data + ")";
        }
        
        private String decompress(String data) {
            return data.replaceAll("ZIP\\((.*)\\)", "$1");
        }
    }

    // ========================================
    // Main Method
    // ========================================
    public static void main(String[] args) {
        System.out.println("=== Decorator Pattern - Stackable Decorators ===\n");
        
        // Coffee Shop Example
        System.out.println("--- Coffee Shop Example ---");
        
        Coffee coffee1 = new SimpleCoffee();
        System.out.println(coffee1.getDescription() + " = $" + coffee1.getCost());
        
        Coffee coffee2 = new MilkDecorator(new SimpleCoffee());
        System.out.println(coffee2.getDescription() + " = $" + coffee2.getCost());
        
        Coffee coffee3 = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        System.out.println(coffee3.getDescription() + " = $" + coffee3.getCost());
        
        Coffee coffee4 = new WhipCreamDecorator(new SugarDecorator(new MilkDecorator(new Espresso())));
        System.out.println(coffee4.getDescription() + " = $" + coffee4.getCost());
        
        Coffee coffee5 = new MochaDecorator(new CaramelDecorator(new WhipCreamDecorator(new Espresso())));
        System.out.println(coffee5.getDescription() + " = $" + coffee5.getCost());
        
        // I/O Stream Example
        System.out.println("\n--- I/O Stream-style Decorator ---");
        
        DataSource source = new FileDataSource("Hello World");
        source.writeData("Hello World");
        System.out.println();
        
        DataSource encrypted = new EncryptionDecorator(new FileDataSource("Secret Data"));
        encrypted.writeData("Secret Data");
        encrypted.readData();
        System.out.println();
        
        DataSource compressed = new CompressionDecorator(new FileDataSource("Large Data"));
        compressed.writeData("Large Data");
        compressed.readData();
        System.out.println();
        
        DataSource both = new EncryptionDecorator(new CompressionDecorator(new FileDataSource("Secure Large Data")));
        both.writeData("Secure Large Data");
        both.readData();
        
        System.out.println("\n=== Summary ===");
        System.out.println("Decorators add behavior dynamically without modifying original class");
        System.out.println("Multiple decorators can be stacked for combined behavior");
        System.out.println("Same interface allows transparent decoration");
    }
}
