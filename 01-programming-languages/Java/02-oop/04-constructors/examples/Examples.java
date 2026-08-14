package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Constructor Patterns ===\n");

        // WHY: Constructors enforce invariant — object is always valid after creation
        // INTERNAL: Compiler generates <init> method, handles super() call, field initialization
        // ENGINEERING: Use telescoping constructor or builder for many parameters

        Config defaultConfig = new Config();
        Config customConfig = new Config("production", 8080, true);
        Config copyConfig = new Config(customConfig);

        System.out.println("Default: " + defaultConfig);
        System.out.println("Custom: " + copyConfig);

        // TRADE-OFF: Constructor overloading vs static factory methods
        // Overloading: IDE-friendly, clear intent. Static factories: named, can return subtypes
        Database db = Database.connect("localhost", 5432);
        Database pool = Database.fromConfig(customConfig);
        System.out.println("DB: " + db);
        System.out.println("Pool: " + pool);
    }
}

class Config {
    private String env;
    private int port;
    private boolean debug;

    public Config() { this("dev", 3000, false); }

    public Config(String env, int port, boolean debug) {
        this.env = env;
        this.port = port;
        this.debug = debug;
    }

    public Config(Config other) {
        this.env = other.env;
        this.port = other.port;
        this.debug = other.debug;
    }

    @Override
    public String toString() { return env + ":" + port + (debug ? "*" : ""); }
}

class Database {
    private String host;
    private int port;

    private Database(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Database connect(String host, int port) {
        return new Database(host, port);
    }

    public static Database fromConfig(Config config) {
        return new Database("localhost", config.port);
    }

    @Override
    public String toString() { return host + ":" + port; }
}
