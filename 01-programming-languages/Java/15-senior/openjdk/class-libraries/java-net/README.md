# java.net — Networking APIs

The `java.net` module provides networking capabilities, from low-level sockets to high-level HTTP clients.

## URL and URI

### URL

```java
URL url = new URL("https://example.com/path?q=1#frag");
url.getProtocol();   // "https"
url.getHost();       // "example.com"
url.getPath();       // "/path"
url.getQuery();      // "q=1"
```

### URI (preferred over URL)

```java
URI uri = URI.create("https://example.com/path?q=1#frag");
uri.getScheme();     // "https"
uri.getHost();       // "example.com"
uri.resolve("/other"); // Resolves relative URI
```

## Socket Programming

### TCP Sockets

**Server:**
```java
ServerSocket server = new ServerSocket(8080);
Socket client = server.accept();
BufferedReader in = new BufferedReader(
    new InputStreamReader(client.getInputStream()));
PrintWriter out = new PrintWriter(client.getOutputStream(), true);
String line = in.readLine();
out.println("Echo: " + line);
```

**Client:**
```java
Socket socket = new Socket("localhost", 8080);
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
BufferedReader in = new BufferedReader(
    new InputStreamReader(socket.getInputStream()));
out.println("Hello server");
String response = in.readLine();
```

### UDP (DatagramSocket)

```java
DatagramSocket socket = new DatagramSocket(9999);
byte[] buffer = new byte[1024];
DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
socket.receive(packet);
String msg = new String(packet.getData(), 0, packet.getLength());
```

## HttpClient (Java 11+)

The modern HTTP client supports HTTP/2 and async operations:

```java
HttpClient client = HttpClient.newHttpClient();

// Synchronous
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Accept", "application/json")
    .GET()
    .build();

HttpResponse<String> response = client.send(request,
    HttpResponse.BodyHandlers.ofString());

System.out.println(response.statusCode());
System.out.println(response.body());
```

### Async Requests

```java
CompletableFuture<HttpResponse<String>> future = client.sendAsync(request,
    HttpResponse.BodyHandlers.ofString());

future.thenApply(HttpResponse::body)
      .thenAccept(System.out::println);
```

### POST with Body

```java
HttpRequest postRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyHandlers.ofString("{\"key\":\"value\"}"))
    .build();
```

## Network Interface

```java
NetworkInterface.getByName("eth0");
NetworkInterface.getByInetAddress(addr);
Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
```

## Proxy Configuration

```java
Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));
Socket socket = new Socket(proxy);
```

## Key Source Files

| Path | Contents |
|------|----------|
| `src/java.net/share/classes/java/net/` | URL, URI, Socket, etc. |
| `src/java.net.http/share/classes/java/net/http/` | HttpClient, HttpRequest, HttpResponse |
| `src/java.net/share/classes/sun/net/` | Internal networking impl |
