# Finding java.net Classes in the Source

The `java.net` module contains networking APIs. Source lives under `src/java.net/share/classes/`.

## Key Packages and Locations

### java.net (Legacy APIs)

| Class | Path |
|-------|------|
| `URL` | `src/java.net/share/classes/java/net/URL.java` |
| `URI` | `src/java.net/share/classes/java/net/URI.java` |
| `URLConnection` | `src/java.net/share/classes/java/net/URLConnection.java` |
| `HttpURLConnection` | `src/java.net/share/classes/java/net/HttpURLConnection.java` |
| `Socket` | `src/java.net/share/classes/java/net/Socket.java` |
| `ServerSocket` | `src/java.net/share/classes/java/net/ServerSocket.java` |
| `DatagramSocket` | `src/java.net/share/classes/java/net/DatagramSocket.java` |
| `InetAddress` | `src/java.net/share/classes/java/net/InetAddress.java` |
| `NetworkInterface` | `src/java.net/share/classes/java/net/NetworkInterface.java` |
| `Proxy` | `src/java.net/share/classes/java/net/Proxy.java` |
| `CookieHandler` | `src/java.net/share/classes/java/net/CookieHandler.java` |

### java.net.http (Java 11+)

| Class | Path |
|-------|------|
| `HttpClient` | `src/java.net.http/share/classes/java/net/http/HttpClient.java` |
| `HttpRequest` | `src/java.net.http/share/classes/java/net/http/HttpRequest.java` |
| `HttpResponse` | `src/java.net.http/share/classes/java/net/http/HttpResponse.java` |
| `WebSocket` | `src/java.net.http/share/classes/java/net/http/WebSocket.java` |

### sun.net (Internal Implementation)

| Package | Path |
|---------|------|
| `sun.net.www.protocol.http` | `src/java.net.http/share/classes/sun/net/www/protocol/http/` |
| `sun.net.www.http.HttpClient` | Internal HTTP client impl |
| `sun.net.dns` | DNS resolver internals |
| `sun.net.spi.nameservice` | Name service provider interface |

## HTTP Client Internals

The Java 11+ HTTP client is implemented in:

```
src/java.net.http/share/classes/java/net/http/
├── HttpClient.java          # Public API
├── HttpRequest.java         # Request builder
├── HttpResponse.java        # Response handling
├── WebSocket.java           # WebSocket API
├── internal/
│   ├── HttpClientImpl.java  # HttpClient implementation
│   ├── HttpRequestBuilder.java
│   ├── HttpResponseImpl.java
│   └── h2/
│       ├── Http2Connection.java   # HTTP/2 impl
│       └── Http2ClientImpl.java
└── ...
```

## Finding Socket Implementations

```bash
# Find socket native methods
rg "Java_java_net_Socket" src/java.net/ --include="*.cpp"

# Find native socket impl
rg "socketImpl" src/java.net/ --include="*.cpp"
```

The native socket code lives in:
- `src/java.base/unix/native/libnet/` (Linux/macOS)
- `src/java.base/windows/native/libnet/` (Windows)
