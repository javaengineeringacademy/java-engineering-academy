package academy.javaengineering.oop.`07-constructors`;

import java.util.Objects;

/**
 * Demonstrates default, parameterized, and copy constructors.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Default (no-arg) constructors</li>
 *   <li>Parameterized constructors with validation</li>
 *   <li>Copy constructors (deep vs shallow)</li>
 *   <li>Constructor chaining with {@code this()} and {@code super()}</li>
 *   <li>Constructor overloading</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ConstructorExample {

    /**
     * Configuration object demonstrating constructor overloading and chaining.
     */
    public static class AppConfig {
        private final String applicationName;
        private final String version;
        private final String environment;
        private final int maxThreads;
        private final boolean debugMode;

        /** Default constructor - sensible defaults for development. */
        public AppConfig() {
            this("MyApp", "1.0.0"); // Chains to 2-arg constructor
        }

        /** Constructor with name and version. */
        public AppConfig(String applicationName, String version) {
            this(applicationName, version, "development"); // Chains to 3-arg
        }

        /** Constructor with environment. */
        public AppConfig(String applicationName, String version, String environment) {
            this(applicationName, version, environment, 4);
        }

        /** Full constructor with all parameters. */
        public AppConfig(String applicationName, String version, String environment,
                         int maxThreads) {
            this(applicationName, version, environment, maxThreads, false);
        }

        /** Complete constructor - all fields initialized here. */
        public AppConfig(String applicationName, String version, String environment,
                         int maxThreads, boolean debugMode) {
            this.applicationName = Objects.requireNonNull(applicationName);
            this.version = Objects.requireNonNull(version);
            this.environment = Objects.requireNonNull(environment);
            if (maxThreads <= 0) throw new IllegalArgumentException("maxThreads must be positive");
            this.maxThreads = maxThreads;
            this.debugMode = debugMode;
        }

        // Getters only - immutable
        public String getApplicationName() { return applicationName; }
        public String getVersion() { return version; }
        public String getEnvironment() { return environment; }
        public int getMaxThreads() { return maxThreads; }
        public boolean isDebugMode() { return debugMode; }

        @Override
        public String toString() {
            return "AppConfig{name='%s', version='%s', env='%s', threads=%d, debug=%s}".formatted(
                    applicationName, version, environment, maxThreads, debugMode);
        }
    }

    /**
     * Mutable employee record demonstrating copy constructor patterns.
     */
    public static class Employee {
        private long id;
        private String name;
        private Address address;

        public Employee(long id, String name, Address address) {
            this.id = id;
            this.name = name;
            this.address = Objects.requireNonNull(address);
        }

        /** Shallow copy constructor - shares mutable references. */
        public Employee(Employee other) {
            this.id = other.id;
            this.name = other.name;
            this.address = other.address; // Shallow: same reference
        }

        /** Deep copy constructor - independent copies of mutable objects. */
        public Employee deepCopy() {
            return new Employee(id, name, address.copy());
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public Address getAddress() { return address; }
        public void setName(String name) { this.name = name; }

        @Override
        public String toString() {
            return "Employee{id=%d, name='%s', address=%s}".formatted(id, name, address);
        }
    }

    /**
     * Mutable address for demonstrating shallow vs deep copy.
     */
    public static class Address {
        private String street;
        private String city;

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }

        public String getStreet() { return street; }
        public String getCity() { return city; }
        public void setStreet(String street) { this.street = street; }

        /** Creates a deep copy of this address. */
        public Address copy() {
            return new Address(street, city);
        }

        @Override
        public String toString() {
            return "%s, %s".formatted(street, city);
        }
    }

    /**
     * Builder pattern using constructors internally.
     */
    public static class HttpRequest {
        private final String method;
        private final String url;
        private final java.util.Map<String, String> headers;
        private final String body;
        private final int timeoutMs;

        private HttpRequest(Builder builder) {
            this.method = builder.method;
            this.url = builder.url;
            this.headers = builder.headers;
            this.body = builder.body;
            this.timeoutMs = builder.timeoutMs;
        }

        public String getMethod() { return method; }
        public String getUrl() { return url; }
        public java.util.Map<String, String> getHeaders() { return headers; }
        public String getBody() { return body; }
        public int getTimeoutMs() { return timeoutMs; }

        @Override
        public String toString() {
            return "%s %s (timeout=%dms)".formatted(method, url, timeoutMs);
        }

        public static class Builder {
            private final String method;
            private final String url;
            private java.util.Map<String, String> headers = java.util.Map.of();
            private String body = "";
            private int timeoutMs = 30000;

            public Builder(String method, String url) {
                this.method = method;
                this.url = url;
            }

            public Builder headers(java.util.Map<String, String> headers) {
                this.headers = new java.util.HashMap<>(headers);
                return this;
            }

            public Builder body(String body) {
                this.body = body;
                return this;
            }

            public Builder timeout(int timeoutMs) {
                this.timeoutMs = timeoutMs;
                return this;
            }

            public HttpRequest build() {
                return new HttpRequest(this);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Constructors Demo ===\n");

        // Constructor chaining
        System.out.println("--- Constructor Chaining (AppConfig) ---");
        AppConfig defaultConfig = new AppConfig();
        AppConfig devConfig = new AppConfig("PaymentService", "2.1.0");
        AppConfig prodConfig = new AppConfig("PaymentService", "2.1.0", "production", 16, false);

        System.out.println("Default:  " + defaultConfig);
        System.out.println("Dev:      " + devConfig);
        System.out.println("Production: " + prodConfig);

        // Shallow vs deep copy
        System.out.println("\n--- Shallow vs Deep Copy ---");
        Address addr1 = new Address("123 Main St", "Springfield");
        Employee emp1 = new Employee(1, "Alice", addr1);

        Employee shallowCopy = new Employee(emp1);
        Employee deepCopy = emp1.deepCopy();

        System.out.println("Original:      " + emp1);
        System.out.println("Shallow copy:  " + shallowCopy);
        System.out.println("Deep copy:     " + deepCopy);

        // Modify original address
        emp1.getAddress().setStreet("456 Oak Ave");
        System.out.println("\nAfter modifying original address:");
        System.out.println("Original:      " + emp1);
        System.out.println("Shallow copy:  " + shallowCopy); // Shared reference!
        System.out.println("Deep copy:     " + deepCopy);    // Independent copy

        // Builder pattern
        System.out.println("\n--- Builder Pattern ---");
        HttpRequest get = new HttpRequest.Builder("GET", "https://api.example.com/users")
                .timeout(5000)
                .build();

        HttpRequest post = new HttpRequest.Builder("POST", "https://api.example.com/users")
                .headers(java.util.Map.of("Content-Type", "application/json"))
                .body("{\"name\": \"Alice\"}")
                .timeout(10000)
                .build();

        System.out.println("GET:  " + get);
        System.out.println("POST: " + post);
        System.out.println("POST body: " + post.getBody());
    }
}
