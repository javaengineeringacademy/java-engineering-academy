package academy.javaengineering.patterns.singleton;

public class SingletonExample {
    private static volatile SingletonExample instance;
    private String data;

    private SingletonExample() {
        this.data = "Singleton Data";
    }

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

    public String getData() {
        return data;
    }

    public static void main(String[] args) {
        SingletonExample s1 = SingletonExample.getInstance();
        SingletonExample s2 = SingletonExample.getInstance();
        System.out.println("Same instance: " + (s1 == s2));
        System.out.println("Data: " + s1.getData());
    }
}
