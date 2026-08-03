package academy.javaengineering.patterns.singleton;

/**
 * Demonstrates the Singleton design pattern ensuring only one instance exists.
 *
 * <p>The Singleton pattern restricts instantiation of a class to one instance.
 * This example uses double-checked locking for thread-safe lazy initialization.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Double-checked locking with volatile</li>
 *   <li>Thread-safe lazy initialization</li>
 *   <li>Private constructor preventing external instantiation</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class SingletonExample {
    private static volatile SingletonExample instance;
    private String data;

    private SingletonExample() {
        this.data = "Singleton Data";
    }

    /**
     * Returns the singleton instance, creating it if necessary.
     *
     * @return the singleton instance
     */
    public static SingletonExample getInstance() {
        if (instance == null) {
            synchronized (SingletonExample.class) {
                if (instance == null) {
                    instance = new SingletonExample();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the data held by this singleton instance.
     *
     * @return the data string
     */
    public String getData() {
        return data;
    }

    /**
     * Demonstrates singleton usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SingletonExample s1 = SingletonExample.getInstance();
        SingletonExample s2 = SingletonExample.getInstance();
        System.out.println("Same instance: " + (s1 == s2));
        System.out.println("Data: " + s1.getData());
    }
}
