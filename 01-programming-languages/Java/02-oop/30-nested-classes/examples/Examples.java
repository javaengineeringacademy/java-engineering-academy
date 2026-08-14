package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Nested Static Class Patterns ===\n");

        // WHY: Static nested classes don't need enclosing instance, more encapsulated
        // INTERNAL: No synthetic accessors, no outer reference stored
        // ENGINEERING: Prefer static nested over non-static inner when possible

        // Static nested class
        Map.Entry<String, Integer> entry = new Map<>("age", 30);
        System.out.println("Entry: " + entry);

        // Builder pattern with static nested class
        Server server = new Server.Builder()
            .host("localhost")
            .port(8080)
            .timeout(30)
            .build();
        System.out.println("Server: " + server);

        // TRADE-OFF: Static nested vs inner class
        // Static nested: no outer reference, slightly faster, preferred
        // Inner class: access to outer instance, useful for callbacks
    }
}

class Map<K, V> {
    static class Entry<K, V> {
        final K key;
        final V value;
        Entry(K key, V value) { this.key = key; this.value = value; }
        @Override public String toString() { return key + "=" + value; }
    }
}

class Server {
    private final String host;
    private final int port;
    private final int timeout;

    private Server(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.timeout = builder.timeout;
    }

    public static class Builder {
        private String host;
        private int port;
        private int timeout;

        public Builder host(String host) { this.host = host; return this; }
        public Builder port(int port) { this.port = port; return this; }
        public Builder timeout(int timeout) { this.timeout = timeout; return this; }
        public Server build() { return new Server(this); }
    }

    @Override
    public String toString() {
        return host + ":" + port + " (timeout=" + timeout + "s)";
    }
}
