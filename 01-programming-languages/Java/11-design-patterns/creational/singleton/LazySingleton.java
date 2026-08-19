package academy.javaengineering.patterns.creational;

/**
 * Lazy initialization singleton — delays instance creation until first use.
 * Not thread-safe by design; prefer ThreadSafeSingleton for concurrent contexts.
 */
public class LazySingleton {

    private static LazySingleton instance;
    private String data;

    private LazySingleton() {
        this.data = "Lazy initialized";
    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
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
        return "LazySingleton{data='" + data + "'}";
    }
}
