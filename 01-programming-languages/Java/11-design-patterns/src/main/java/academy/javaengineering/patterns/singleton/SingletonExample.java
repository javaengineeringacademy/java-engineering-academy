package academy.javaengineering.patterns.singleton;

/**
 * Demonstrates all 5 flavors of the Singleton design pattern.
 *
 * <h3>Singleton Flavors:</h3>
 * <ol>
 *   <li>Eager Initialization</li>
 *   <li>Lazy Initialization</li>
 *   <li>Double-Checked Locking (Thread-Safe)</li>
 *   <li>Enum Singleton</li>
 *   <li>Bill Pugh Holder Pattern</li>
 * </ol>
 */
public class SingletonExample {

    // ========================================
    // Flavor 1: Eager Initialization
    // ========================================
    static class EagerSingleton {
        private static final EagerSingleton INSTANCE = new EagerSingleton();
        
        private EagerSingleton() {
            System.out.println("EagerSingleton created");
        }
        
        public static EagerSingleton getInstance() {
            return INSTANCE;
        }
        
        public void display() {
            System.out.println("EagerSingleton: Instance hash=" + hashCode());
        }
    }

    // ========================================
    // Flavor 2: Lazy Initialization (Not Thread-Safe)
    // ========================================
    static class LazySingleton {
        private static LazySingleton instance;
        
        private LazySingleton() {
            System.out.println("LazySingleton created");
        }
        
        public static LazySingleton getInstance() {
            if (instance == null) {
                instance = new LazySingleton();
            }
            return instance;
        }
        
        public void display() {
            System.out.println("LazySingleton: Instance hash=" + hashCode());
        }
    }

    // ========================================
    // Flavor 3: Double-Checked Locking (Thread-Safe)
    // ========================================
    static class DCLSingleton {
        private static volatile DCLSingleton instance;
        
        private DCLSingleton() {
            System.out.println("DCLSingleton created");
        }
        
        public static DCLSingleton getInstance() {
            if (instance == null) {
                synchronized (DCLSingleton.class) {
                    if (instance == null) {
                        instance = new DCLSingleton();
                    }
                }
            }
            return instance;
        }
        
        public void display() {
            System.out.println("DCLSingleton: Instance hash=" + hashCode());
        }
    }

    // ========================================
    // Flavor 4: Enum Singleton (Recommended by Joshua Bloch)
    // ========================================
    enum EnumSingleton {
        INSTANCE;
        
        private String data;
        
        EnumSingleton() {
            this.data = "Enum Singleton Data";
            System.out.println("EnumSingleton created");
        }
        
        public String getData() {
            return data;
        }
        
        public void display() {
            System.out.println("EnumSingleton: Instance hash=" + hashCode());
        }
    }

    // ========================================
    // Flavor 5: Bill Pugh Holder Pattern (Recommended)
    // ========================================
    static class BillPughSingleton {
        private BillPughSingleton() {
            System.out.println("BillPughSingleton created");
        }
        
        // Inner static class is not loaded until getInstance() is called
        private static class Holder {
            private static final BillPughSingleton INSTANCE = new BillPughSingleton();
        }
        
        public static BillPughSingleton getInstance() {
            return Holder.INSTANCE;
        }
        
        public void display() {
            System.out.println("BillPughSingleton: Instance hash=" + hashCode());
        }
    }

    // ========================================
    // Main Method - Demonstrate All Flavors
    // ========================================
    public static void main(String[] args) {
        System.out.println("=== Singleton Pattern - All 5 Flavors ===\n");
        
        // Flavor 1: Eager Initialization
        System.out.println("--- 1. Eager Initialization ---");
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        eager1.display();
        eager2.display();
        System.out.println("Same instance: " + (eager1 == eager2));
        
        // Flavor 2: Lazy Initialization
        System.out.println("\n--- 2. Lazy Initialization ---");
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        lazy1.display();
        lazy2.display();
        System.out.println("Same instance: " + (lazy1 == lazy2));
        
        // Flavor 3: Double-Checked Locking
        System.out.println("\n--- 3. Double-Checked Locking ---");
        DCLSingleton dcl1 = DCLSingleton.getInstance();
        DCLSingleton dcl2 = DCLSingleton.getInstance();
        dcl1.display();
        dcl2.display();
        System.out.println("Same instance: " + (dcl1 == dcl2));
        
        // Flavor 4: Enum Singleton
        System.out.println("\n--- 4. Enum Singleton ---");
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        enum1.display();
        enum2.display();
        System.out.println("Same instance: " + (enum1 == enum2));
        System.out.println("Data: " + enum1.getData());
        
        // Flavor 5: Bill Pugh Holder
        System.out.println("\n--- 5. Bill Pugh Holder Pattern ---");
        BillPughSingleton bill1 = BillPughSingleton.getInstance();
        BillPughSingleton bill2 = BillPughSingleton.getInstance();
        bill1.display();
        bill2.display();
        System.out.println("Same instance: " + (bill1 == bill2));
        
        // Comparison
        System.out.println("\n=== Comparison ===");
        System.out.println("Eager:     Creates at class load, thread-safe, no sync overhead");
        System.out.println("Lazy:      Creates on demand, NOT thread-safe");
        System.out.println("DCL:       Creates on demand, thread-safe, sync overhead");
        System.out.println("Enum:      Creates at class load, thread-safe, serialization safe");
        System.out.println("Bill Pugh: Creates on demand, thread-safe, no sync overhead (RECOMMENDED)");
    }
}
