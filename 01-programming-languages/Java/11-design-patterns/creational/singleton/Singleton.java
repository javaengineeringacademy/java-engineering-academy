package academy.javaengineering.patterns.creational;

public class Singleton {

    private static volatile Singleton instance;
    private String data;

    private Singleton(String data) {
        this.data = data;
    }

    // Double-checked locking
    public static Singleton getInstance(String data) {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton(data);
                }
            }
        }
        return instance;
    }

    // Static holder pattern
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton("default");
    }

    public static Singleton getHolderInstance() {
        return Holder.INSTANCE;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
