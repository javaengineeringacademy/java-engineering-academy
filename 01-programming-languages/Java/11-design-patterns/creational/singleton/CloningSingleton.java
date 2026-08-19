package academy.javaengineering.patterns.creational;

/**
 * Singleton that prevents cloning via Cloneable interface.
 * Without override, Object.clone() would produce a second instance.
 * The clone method throws CloneNotSupportedException to enforce singleton.
 */
public class CloningSingleton implements Cloneable {

    private static volatile CloningSingleton instance;
    private String data;

    private CloningSingleton() {
        this.data = "Cloning-safe initialized";
    }

    public static CloningSingleton getInstance() {
        if (instance == null) {
            synchronized (CloningSingleton.class) {
                if (instance == null) {
                    instance = new CloningSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Prevents cloning by throwing CloneNotSupportedException.
     * This ensures the singleton invariant is not violated via clone().
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of singleton is not allowed");
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CloningSingleton{data='" + data + "'}";
    }
}
