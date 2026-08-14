package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Access Modifier Patterns ===\n");

        // WHY: Access modifiers control encapsulation and API surface
        // INTERNAL: JVM enforces access checks at link time
        // ENGINEERING: Least privilege principle - most restrictive that works

        Config config = new Config("secret-key", 8080);
        System.out.println("Port: " + config.getPort());      // public
        // System.out.println(config.key);  // private - compilation error

        // TRADE-OFF: public vs private vs package-private
        // public: accessible everywhere, breaks encapsulation
        // private: only within class, safest
        // package-private (default): within package, testing friendly
        // protected: subclasses + package
    }
}

class Config {
    private final String key;  // Private: only accessible within this class
    private int port;

    public Config(String key, int port) {
        this.key = key;
        this.port = port;
    }

    public int getPort() { return port; }  // Public: accessible everywhere

    // Package-private: accessible within same package only
    String getKey() { return key; }

    // Protected: accessible in subclasses + same package
    protected void setPort(int port) { this.port = port; }
}
