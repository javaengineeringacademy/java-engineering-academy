package academy.javaengineering.patterns.creational;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Comprehensive guide demonstrating all singleton variants and their
 * characteristics: thread safety, serialization, cloning protection,
 * and reflection attack resistance.
 */
public class SingletonPatterns {

    public static void main(String[] args) {
        System.out.println("=== Singleton Patterns Comprehensive Guide ===\n");

        demonstrateLazy();
        demonstrateThreadSafe();
        demonstrateBillPugh();
        demonstrateDCL();
        demonstrateSerializable();
        demonstrateCloning();
        demonstrateReflectionAttack();
    }

    private static void demonstrateLazy() {
        System.out.println("--- Lazy Initialization ---");
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        System.out.println("Same instance: " + (lazy1 == lazy2));
        System.out.println("Data: " + lazy1.getData());
        System.out.println();
    }

    private static void demonstrateThreadSafe() {
        System.out.println("--- Thread-Safe (Synchronized) ---");
        ThreadSafeSingleton ts1 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton ts2 = ThreadSafeSingleton.getInstance();
        System.out.println("Same instance: " + (ts1 == ts2));
        System.out.println("Data: " + ts1.getData());
        System.out.println();
    }

    private static void demonstrateBillPugh() {
        System.out.println("--- Bill Pugh (Static Holder) ---");
        Singleton bp1 = Singleton.getHolderInstance();
        Singleton bp2 = Singleton.getHolderInstance();
        System.out.println("Same instance: " + (bp1 == bp2));
        System.out.println("Data: " + bp1.getData());
        System.out.println();
    }

    private static void demonstrateDCL() {
        System.out.println("--- Double-Checked Locking ---");
        Singleton dcl1 = Singleton.getInstance("dcl-data");
        Singleton dcl2 = Singleton.getInstance("ignored");
        System.out.println("Same instance: " + (dcl1 == dcl2));
        System.out.println("Data: " + dcl1.getData());
        System.out.println();
    }

    private static void demonstrateSerializable() {
        System.out.println("--- Serializable Singleton ---");
        SerializableSingleton s1 = SerializableSingleton.getInstance();
        s1.setData("modified");

        try {
            // Serialize
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(s1);
            oos.close();

            // Deserialize
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            SerializableSingleton s2 = (SerializableSingleton) ois.readObject();
            ois.close();

            System.out.println("Same instance after deserialization: " + (s1 == s2));
            System.out.println("Data preserved: " + s2.getData());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demonstrateCloning() {
        System.out.println("--- Cloning-Protected Singleton ---");
        CloningSingleton c1 = CloningSingleton.getInstance();
        try {
            CloningSingleton c2 = (CloningSingleton) c1.clone();
            System.out.println("Clone succeeded (unexpected): " + (c1 == c2));
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone prevented: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demonstrateReflectionAttack() {
        System.out.println("--- Reflection Attack Resistance ---");
        Singleton original = Singleton.getInstance("original");

        try {
            Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Singleton reflected = constructor.newInstance("reflected");
            System.out.println("Reflection created new instance: " + (original != reflected));
            System.out.println("Note: To prevent this, add a check in the private constructor.");
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            System.out.println("Reflection blocked: " + e.getMessage());
        }
        System.out.println();
    }
}
