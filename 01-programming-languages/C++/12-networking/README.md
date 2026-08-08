# Networking — C++

## Why It Matters

Networking is the backbone of modern software. Every mobile app, web service, game, and distributed system relies on network communication. When you understand sockets, protocols, and async I/O, you can build reliable, high-performance networked systems that handle millions of connections instead of copying HTTP snippets.

## What It Is

C++ networking covers socket programming, TCP/UDP protocols, async I/O with epoll/kqueue, and libraries like Boost.Asio for building networked applications that handle partial reads, connection resets, and timeouts gracefully.

## Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Protocol | TCP vs UDP vs QUIC | TCP for reliability, UDP for speed, QUIC for modern web | Don't default to UDP without understanding reliability needs |
| I/O model | Blocking vs async vs epoll/kqueue | Async for high concurrency, blocking for simple clients | Blocking I/O in high-concurrency servers |
| Library | Raw sockets vs Boost.Asio vs libcurl | Asio for async, raw sockets for learning/custom protocols | Raw sockets for production HTTP (use libcurl instead) |
| Serialization | JSON vs protobuf vs flatbuffers | Protobuf for performance, JSON for human readability | JSON in hot paths with millions of messages |
| Security | TLS/SSL vs plain | Always TLS in production; plain only for localhost testing | Plain text over public networks |
| Buffering | Fixed vs dynamic buffers | Fixed for predictable message sizes, dynamic for variable | Dynamic allocation in tight packet loops |

## Expanded Code Examples

### TCP Client and Server

```cpp
// --- Server ---
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <cstring>
#include <iostream>

int start_server(int port) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) {
        perror("socket failed");
        return -1;
    }

    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    sockaddr_in address{};
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(port);

    if (bind(server_fd, (sockaddr*)&address, sizeof(address)) < 0) {
        perror("bind failed");
        close(server_fd);
        return -1;
    }

    if (listen(server_fd, 10) < 0) {
        perror("listen failed");
        close(server_fd);
        return -1;
    }

    std::cout << "Server listening on port " << port << "\n";

    while (true) {
        sockaddr_in client_addr{};
        socklen_t client_len = sizeof(client_addr);
        int client_fd = accept(server_fd, (sockaddr*)&client_addr, &client_len);
        if (client_fd < 0) {
            perror("accept failed");
            continue;
        }

        char buffer[1024] = {};
        ssize_t bytes_read = read(client_fd, buffer, sizeof(buffer) - 1);
        if (bytes_read > 0) {
            std::cout << "Received: " << buffer << "\n";
            const char* response = "Hello from server!";
            write(client_fd, response, strlen(response));
        }
        close(client_fd);
    }

    close(server_fd);
    return 0;
}

// --- Client ---
int send_message(const char* host, int port, const char* message) {
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) return -1;

    sockaddr_in server_addr{};
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    inet_pton(AF_INET, host, &server_addr.sin_addr);

    if (connect(sock, (sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        close(sock);
        return -1;
    }

    write(sock, message, strlen(message));

    char buffer[1024] = {};
    ssize_t bytes = read(sock, buffer, sizeof(buffer) - 1);
    if (bytes > 0) {
        std::cout << "Server response: " << buffer << "\n";
    }

    close(sock);
    return 0;
}
```

### Non-Blocking I/O with epoll (Linux)

```cpp
#include <sys/epoll.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <fcntl.h>
#include <vector>
#include <iostream>

void set_nonblocking(int fd) {
    int flags = fcntl(fd, F_GETFL, 0);
    fcntl(fd, F_SETFL, flags | O_NONBLOCK);
}

void run_epoll_server(int port) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    set_nonblocking(server_fd);

    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);
    bind(server_fd, (sockaddr*)&addr, sizeof(addr));
    listen(server_fd, 128);

    int epoll_fd = epoll_create1(0);

    epoll_event ev{};
    ev.events = EPOLLIN;
    ev.data.fd = server_fd;
    epoll_ctl(epoll_fd, EPOLL_CTL_ADD, server_fd, &ev);

    std::vector<epoll_event> events(128);

    while (true) {
        int n = epoll_wait(epoll_fd, events.data(), events.size(), -1);
        for (int i = 0; i < n; ++i) {
            if (events[i].data.fd == server_fd) {
                sockaddr_in client_addr{};
                socklen_t len = sizeof(client_addr);
                int client_fd = accept(server_fd, (sockaddr*)&client_addr, &len);
                set_nonblocking(client_fd);

                epoll_event cev{};
                cev.events = EPOLLIN | EPOLLET;  // Edge-triggered
                cev.data.fd = client_fd;
                epoll_ctl(epoll_fd, EPOLL_CTL_ADD, client_fd, &cev);
            } else {
                int client_fd = events[i].data.fd;
                char buf[1024];
                ssize_t nread = read(client_fd, buf, sizeof(buf) - 1);
                if (nread <= 0) {
                    close(client_fd);
                } else {
                    buf[nread] = '\0';
                    write(client_fd, "OK", 2);
                }
            }
        }
    }
}
```

### HTTP Client with libcurl

```cpp
#include <curl/curl.h>
#include <string>
#include <iostream>

static size_t write_callback(char* ptr, size_t size, size_t nmemb, void* userdata) {
    auto* response = static_cast<std::string*>(userdata);
    response->append(ptr, size * nmemb);
    return size * nmemb;
}

struct HttpResponse {
    long status_code = 0;
    std::string body;
    std::string error;
};

HttpResponse http_get(const std::string& url) {
    HttpResponse result;

    CURL* curl = curl_easy_init();
    if (!curl) {
        result.error = "Failed to init CURL";
        return result;
    }

    curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &result.body);
    curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30L);
    curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 10L);
    curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);

    CURLcode res = curl_easy_perform(curl);
    if (res != CURLE_OK) {
        result.error = curl_easy_strerror(res);
    } else {
        curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &result.status_code);
    }

    curl_easy_cleanup(curl);
    return result;
}

// Usage
void fetch_data() {
    auto response = http_get("https://api.example.com/data");
    if (!response.error.empty()) {
        std::cerr << "Error: " << response.error << "\n";
    } else {
        std::cout << "Status: " << response.status_code << "\n";
        std::cout << "Body: " << response.body << "\n";
    }
}
```

### Boost.Asio Async TCP

```cpp
#include <boost/asio.hpp>
#include <iostream>
#include <memory>

using boost::asio::ip::tcp;

class TcpSession : public std::enable_shared_from_this<TcpSession> {
    tcp::socket socket_;
    char data_[1024];

public:
    explicit TcpSession(tcp::socket socket) : socket_(std::move(socket)) {}

    void start() {
        do_read();
    }

private:
    void do_read() {
        auto self = shared_from_this();
        socket_.async_read_some(boost::asio::buffer(data_, sizeof(data_)),
            [this, self](boost::system::error_code ec, std::size_t length) {
                if (!ec) {
                    do_write(length);
                }
            });
    }

    void do_write(std::size_t length) {
        auto self = shared_from_this();
        boost::asio::async_write(socket_, boost::asio::buffer(data_, length),
            [this, self](boost::system::error_code ec, std::size_t /*length*/) {
                if (!ec) {
                    do_read();
                }
            });
    }
};

class TcpServer {
    tcp::acceptor acceptor_;

public:
    TcpServer(boost::asio::io_context& io_context, short port)
        : acceptor_(io_context, tcp::endpoint(tcp::v4(), port)) {
        do_accept();
    }

private:
    void do_accept() {
        acceptor_.async_accept(
            [this](boost::system::error_code ec, tcp::socket socket) {
                if (!ec) {
                    std::make_shared<TcpSession>(std::move(socket))->start();
                }
                do_accept();
            });
    }
};
```

### UDP Socket Example

```cpp
#include <sys/socket.h>
#include <netinet/in.h>
#include <cstring>
#include <iostream>

void udp_server(int port) {
    int sock = socket(AF_INET, SOCK_DGRAM, 0);

    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    bind(sock, (sockaddr*)&addr, sizeof(addr));

    char buffer[1024];
    sockaddr_in client_addr{};
    socklen_t client_len = sizeof(client_addr);

    while (true) {
        ssize_t n = recvfrom(sock, buffer, sizeof(buffer) - 1, 0,
                            (sockaddr*)&client_addr, &client_len);
        if (n > 0) {
            buffer[n] = '\0';
            std::cout << "UDP received: " << buffer << "\n";

            const char* reply = "ACK";
            sendto(sock, reply, strlen(reply), 0,
                   (sockaddr*)&client_addr, client_len);
        }
    }
}
```

## Production Incidents

### Incident 1: Buffer Overflow in Network Parser
**Problem**: A network service crashed with a segfault when receiving malformed packets from external clients.

**Cause**: The packet parser used `sprintf()` to format incoming data into a fixed 256-byte buffer without checking the incoming packet size. A malicious client sent a 2KB packet, overflowing the buffer and overwriting the return address on the stack.

**Impact**: Remote code execution vulnerability. CVSS score 9.8. Emergency patch deployed within 4 hours. 3 customers reported suspicious activity on their accounts.

**Detection**: AddressSanitizer caught the buffer overflow in a fuzzing session. Security audit triggered by an unrelated vulnerability report.

**Solution**: Replaced `sprintf` with `snprintf` with bounds checking. Added packet size validation before parsing. Implemented input validation at the network boundary: reject any packet exceeding maximum expected size.

**Prevention**: Never use `sprintf`, `strcpy`, or `strcat` on network data. Always validate packet sizes. Run AFL/libFuzzer on all network-facing code. Enable `-fsanitize=address,undefined` in CI.

### Incident 2: Connection Leak Under Load
**Problem**: A REST API server ran out of file descriptors after handling 10K requests, even though each request completed successfully.

**Cause**: The HTTP client library opened a new TCP connection for each request but didn't always close it. When a request timed out, the error handler returned early without calling `curl_easy_cleanup()`. The socket remained in `TIME_WAIT` state for 60 seconds, accumulating until the fd limit was hit.

**Impact**: Server crashed every ~5 minutes under sustained load. 100% downtime for high-traffic periods. Auto-restart helped briefly but the leak was fast enough to crash again.

**Detection**: `lsof -p <pid> | wc -l` showed file descriptor count climbing continuously. `netstat` showed thousands of connections in `TIME_WAIT`.

**Solution**: Wrapped the CURL handle in a RAII class that calls `curl_easy_cleanup()` in its destructor. Added a connection pool with max-connections limit. Set `CURLOPT_MAXCONNECTS` and `CURLOPT_FORBID_REUSE` to prevent connection exhaustion.

**Prevention**: Always use RAII for network resources. Monitor fd count in production metrics. Set connection pool limits. Add connection-count assertions in debug builds.

### Incident 3: DNS Resolution Blocking Event Loop
**Problem**: A game server's main event loop froze for 5 seconds whenever a DNS lookup failed, causing all 500 connected players to experience lag spikes.

**Cause**: DNS resolution (`getaddrinfo()`) was called synchronously on the main thread. When the DNS server was unreachable, the system's default timeout was 5 seconds. During this time, the event loop couldn't process any player input or game state updates.

**Impact**: 500 players experienced 5-second freezes every few minutes. Player complaints spiked. Churn increased 15% in the week after deployment.

**Detection**: Player-reported lag correlated with DNS timeout patterns in network logs. `strace` on the game server showed `getaddrinfo()` blocking the main thread.

**Solution**: Moved DNS resolution to a background thread. Implemented DNS caching with TTL-based expiration (cache results for 5 minutes). Added a fallback DNS server. Set `getaddrinfo()` timeout to 2 seconds with `AI_NUMERICSERV` flag.

**Prevention**: Never call blocking I/O on the main event loop. Use async DNS resolution (c-ares library). Cache DNS results. Set aggressive timeouts for all network operations.

## Production Checklist

- [ ] Validate all incoming packet sizes before parsing
- [ ] Use `snprintf`/`strncpy` instead of `sprintf`/`strcpy` on network data
- [ ] Implement connection pooling with size limits
- [ ] Use RAII for all network resources (sockets, CURL handles)
- [ ] Set timeouts for all network operations (connect, read, write)
- [ ] Handle partial reads/writes in all network code
- [ ] Use TLS/SSL for all production network communication
- [ ] Implement reconnection logic with exponential backoff
- [ ] Monitor fd count, connection count, and latency in production
- [ ] Never block the main event loop with DNS or I/O
- [ ] Use edge-triggered epoll/kqueue for high-concurrency servers
- [ ] Fuzz all network-facing parsers

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | TCP/UDP sockets, basic HTTP client with libcurl |
| **Intermediate** | Non-blocking I/O, epoll/kqueue, connection pooling, async patterns |
| **Advanced** | Custom protocols, zero-copy networking, io_uring, QUIC implementation |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Networking is always slow" | Local loopback can do millions of messages/sec. Network overhead is in latency, not throughput. |
| "HTTP is the only protocol needed" | TCP/UDP/QUIC/WebSocket/gRPC all have specific use cases. HTTP adds overhead for real-time communication. |
| "Sockets are too low-level" | Sockets are the foundation. Understanding them makes you better with any higher-level library. |
| "Blocking I/O is always bad" | Blocking I/O is fine for simple clients and low-concurrency servers. Complexity has a cost. |
| "One thread per connection scales" | Thread-per-connection hits OS limits at ~10K connections. Use async I/O or event loops for scale. |

## One-Minute Revision Table

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Socket | Network communication endpoint | File descriptor wrapping network address |
| TCP | Reliable, ordered byte stream | Three-way handshake, flow control |
| UDP | Fast, connectionless datagram | No ordering, no reliability guarantees |
| epoll/kqueue | I/O event notification | Edge-triggered for high performance |
| Async I/O | Non-blocking network operations | Event-driven, no thread-per-connection |
| TLS/SSL | Encrypted network communication | Always use in production |
| Connection pool | Reuse TCP connections | Avoids handshake overhead |
| Backoff | Retry with increasing delay | Prevents thundering herd |
| Partial read | Network may deliver partial data | Always loop until complete message received |

## Cross-Linked Related Topics

- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Thread pools, async patterns, mutexes
- **Performance** → [Module 11: Performance](../11-performance/) — Zero-copy, memory pools for buffers
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Error handling, RAII for resources
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Linking libcurl, Boost.Asio
- **Memory Management** → [Module 05: Memory](../05-memory-management/) — Buffer management, avoid leaks
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `std::optional` for results, lambdas for callbacks
