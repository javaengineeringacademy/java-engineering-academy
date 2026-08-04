package academy.javaengineering.patterns.builder;

/**
 * Demonstrates the Builder design pattern for constructing complex objects.
 *
 * <p>The Builder pattern separates the construction of a complex object from its
 * representation. It allows step-by-step construction and produces different
 * representations using the same building process.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Fluent interface with method chaining</li>
 *   <li>Immutable object construction</li>
 *   <li>Separate builder class for complex object creation</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class BuilderExample {

    /**
     * HTTP request object with immutable fields built via Builder.
     */
    public static class HttpRequest {
        private final String url;
        private final String method;
        private final String body;
        private final int timeout;

        private HttpRequest(Builder builder) {
            this.url = builder.url;
            this.method = builder.method;
            this.body = builder.body;
            this.timeout = builder.timeout;
        }

        /**
         * Builder for constructing HttpRequest instances.
         */
        public static class Builder {
            private final String url;
            private String method = "GET";
            private String body = "";
            private int timeout = 30000;

            /**
             * Creates a Builder with the required URL.
             *
             * @param url the request URL
             */
            public Builder(String url) {
                this.url = url;
            }

            /**
             * Sets the HTTP method.
             *
             * @param method the HTTP method (GET, POST, etc.)
             * @return this builder
             */
            public Builder method(String method) {
                this.method = method;
                return this;
            }

            /**
             * Sets the request body.
             *
             * @param body the request body
             * @return this builder
             */
            public Builder body(String body) {
                this.body = body;
                return this;
            }

            /**
             * Sets the connection timeout in milliseconds.
             *
             * @param timeout the timeout value
             * @return this builder
             */
            public Builder timeout(int timeout) {
                this.timeout = timeout;
                return this;
            }

            /**
             * Builds and returns the HttpRequest.
             *
             * @return the constructed HttpRequest
             */
            public HttpRequest build() {
                return new HttpRequest(this);
            }
        }

        @Override
        public String toString() {
            return "HttpRequest{url='" + url + "', method='" + method + "'}";
        }
    }

    /**
     * Demonstrates builder pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
            .method("POST")
            .body("{\"key\": \"value\"}")
            .timeout(5000)
            .build();

        System.out.println(request);
    }
}
