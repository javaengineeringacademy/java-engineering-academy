# HTTP/3 and QUIC - Examples

## Basic HTTP/2 Client (Java Built-in)

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Http2ClientExample {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP Version: " + response.version());
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
    }
}
```

## Async HTTP/2 Client

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AsyncHttp2Client {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/ip"))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }
}
```

## Concurrent Requests with HTTP/2

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConcurrentHttp2Example {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        List<String> urls = List.of(
            "https://httpbin.org/get",
            "https://httpbin.org/ip",
            "https://httpbin.org/user-agent"
        );

        List<CompletableFuture<String>> futures = urls.stream()
            .map(url -> client.sendAsync(
                HttpRequest.newBuilder().uri(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofString()
            ).thenApply(HttpResponse::body))
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        futures.forEach(f -> System.out.println(f.join()));
    }
}
```

## Server-Sent Events with HTTP/2

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SSEClientExample {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/events"))
                .header("Accept", "text/event-stream")
                .GET()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofLines())
                .body()
                .limit(10)
                .forEach(line -> {
                    if (line.startsWith("data:")) {
                        System.out.println("Event: " + line.substring(5).trim());
                    }
                });
    }
}
```
