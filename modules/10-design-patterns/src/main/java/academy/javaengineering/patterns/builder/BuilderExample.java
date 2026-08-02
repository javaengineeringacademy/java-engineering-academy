package academy.javaengineering.patterns.builder;

public class BuilderExample {

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

        public static class Builder {
            private final String url;
            private String method = "GET";
            private String body = "";
            private int timeout = 30000;

            public Builder(String url) {
                this.url = url;
            }

            public Builder method(String method) {
                this.method = method;
                return this;
            }

            public Builder body(String body) {
                this.body = body;
                return this;
            }

            public Builder timeout(int timeout) {
                this.timeout = timeout;
                return this;
            }

            public HttpRequest build() {
                return new HttpRequest(this);
            }
        }

        @Override
        public String toString() {
            return "HttpRequest{url='" + url + "', method='" + method + "'}";
        }
    }

    public static void main(String[] args) {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
            .method("POST")
            .body("{\"key\": \"value\"}")
            .timeout(5000)
            .build();

        System.out.println(request);
    }
}
