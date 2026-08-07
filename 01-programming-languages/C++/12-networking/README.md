# Networking

## What it is
Writing code that communicates over networks (TCP, UDP, HTTP).

## Why it exists
To enable distributed systems, web services, and networked applications.

## When to use it
When building networked applications, APIs, or distributed systems.

## How it works

### TCP Sockets
```cpp
#include <sys/socket.h>
#include <netinet/in.h>

int server_fd = socket(AF_INET, SOCK_STREAM, 0);
struct sockaddr_in address;
address.sin_family = AF_INET;
address.sin_addr.s_addr = INADDR_ANY;
address.sin_port = htons(8080);

bind(server_fd, (struct sockaddr*)&address, sizeof(address));
listen(server_fd, 3);
```

### HTTP Client
```cpp
#include <curl/curl.h>

CURL* curl = curl_easy_init();
if (curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://example.com");
    CURLcode res = curl_easy_perform(curl);
    curl_easy_cleanup(curl);
}
```

### Async Networking
```cpp
#include <boost/asio.hpp>

boost::asio::io_context io_context;
boost::asio::ip::tcp::acceptor acceptor(io_context, 
    boost::asio::ip::tcp::endpoint(boost::asio::ip::tcp::v4(), 8080));
```

## Production Checklist
- [ ] Handle connection errors gracefully
- [ ] Use timeouts for network operations
- [ ] Implement reconnection logic
- [ ] Use SSL/TLS for security
- [ ] Validate all input data
- [ ] Log network operations

## Maturity Levels
- **Beginner**: Basic TCP/UDP sockets
- **Intermediate**: HTTP clients, async operations
- **Advanced**: High-performance servers, protocol implementation

## Common Myths
- ❌ "Networking is always slow"
- ❌ "HTTP is the only protocol needed"
- ❌ "Sockets are too low-level"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Socket | Network communication endpoint |
| TCP | Reliable, ordered data transfer |
| UDP | Fast, unreliable data transfer |
| HTTP | Application layer protocol |
| Async | Non-blocking operations |

## Related Topics
- [Concurrency](../07-concurrency/)
- [Performance](../11-performance/)
- [Best Practices](../14-best-practices/)