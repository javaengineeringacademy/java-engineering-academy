package academy.javaengineering.patterns.creational;

/**
 * Thread-safe singleton using synchronized method.
 * Guarantees single instance under concurrent access at the cost of
 * synchronized overhead on every call. For better performance consider
 * DCL (in Singleton.java) or the Bill Pugh holder pattern.
 */
public class ThreadSafeSingleton {

    private static volatile ThreadSafeSingleton instance;
    private String data;

    private ThreadSafeSingleton() {
        this.data = "ThreadSafe initialized";
    }

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ThreadSafeSingleton{data='" + data + "'}";
    }
}
