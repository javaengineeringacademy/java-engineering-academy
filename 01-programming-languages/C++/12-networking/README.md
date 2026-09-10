# Networking — C++

## Overview

Networking is the backbone of modern software. Every mobile app, web service, game, and distributed system relies on network communication. C++ networking encompasses low-level socket programming, kernel I/O multiplexing (epoll/kqueue), and high-level async frameworks like Boost.Asio. It enables building high-performance networked systems capable of handling millions of concurrent connections — from game servers and database drivers to HTTP reverse proxies and message brokers. C++ gives direct access to OS primitives for zero-copy I/O, non-blocking sockets, and io_uring, making it the language of choice for performance-critical network infrastructure. When you understand sockets, protocols, and async I/O, you can build reliable, high-performance networked systems that handle millions of connections instead of copying HTTP snippets.

## Learning Objectives

- Understand socket programming fundamentals (TCP/UDP) using POSIX APIs and Boost.Asio
- Implement non-blocking I/O with epoll (Linux) and kqueue (macOS/BSD)
- Design async network servers using the Reactor or Proactor pattern
- Apply connection pooling, backpressure, and reconnection with exponential backoff
- Recognize security implications (TLS, buffer overflows, input validation)
- Debug common networking issues (fd leaks, partial reads, DNS blocking)

## Prerequisites

- [Module 07: Concurrency](../07-concurrency/) — threads, mutexes, condition variables, futures
- [Module 08: Modern C++](../08-modern-cpp/) — move semantics, RAII, `std::optional`, lambdas, smart pointers

## History

| Era | Event | Impact |
|-----|-------|--------|
| 1983 | BSD 4.2 sockets API introduced | First portable socket interface (`socket()`, `bind()`, `listen()`, `accept()`). Became the universal network programming model. |
| 1993 | Solaris introduces `/dev/poll` | First scalable I/O multiplexing beyond `select()`. Required kernel event notification for high-fd-count servers. |
| 1999 | Linux epoll API (kernel 2.1) | O(1) event notification replacing O(n) `poll()`/`select()`. Enabled 100K+ connection servers on Linux. |
| 2000 | Boost.Asio (first release) | Portable async I/O library for C++ wrapping epoll/kqueue/IOCP under a unified API. |
| 2004 | kqueue/kevent in FreeBSD 4.1 | BSD equivalent of epoll, available on macOS/BSD. Event-driven I/O notification with richer event types. |
| 2014 | io_uring proposal (kernel 5.1) | Kernel-level async I/O with submission/completion queues. Bypasses syscall overhead for extreme throughput. |
| 2019 | C++20 coroutines (standardized) | Enables async I/O with sequential-looking code via `co_await`, replacing callback chains. |
| 2022 | QUIC protocol (RFC 9000) | UDP-based transport with built-in TLS 1.3, multiplexed streams, and 0-RTT connection. |

## Production Notes

- **File descriptors are limited**: Default ulimit is often 1024. For production servers, set `ulimit -n 100000+` and tune `fs.file-max` in `/proc/sys/fs/file-max`.
- **Ephemeral port range**: Linux has ~28K ephemeral ports. For high-connection-rate servers, expand with `sysctl net.ipv4.ip_local_port_range="1024 65535"`.
- **SO_REUSEADDR vs SO_REUSEPORT**: `SO_REUSEADDR` allows binding to addresses in `TIME_WAIT`. `SO_REUSEPORT` (Linux 3.9+) enables multiple processes to bind to the same port with kernel-level load balancing.
- **TCP_NODELAY**: Disable Nagle's algorithm (`setsockopt(..., TCP_NODELAY, ...)`) for latency-sensitive protocols. Default Nagle buffering adds up to 40ms delay per message.
- **Keep-alive tuning**: `TCP_KEEPIDLE`, `TCP_KEEPINTVL`, `TCP_KEEPCNT` control dead connection detection. Too aggressive wastes bandwidth; too lenient leaves zombie connections.
- **AddressSanitizer in CI**: Always compile network-facing code with `-fsanitize=address,undefined` to catch buffer overflows and use-after-free in packet parsing.

## Core Concepts

| Concept | Description | C++ Relevance |
|---------|-------------|---------------|
| Socket | Bidirectional communication endpoint (fd on Unix) | `int fd = socket(AF_INET, SOCK_STREAM, 0)` or `boost::asio::ip::tcp::socket` |
| TCP | Reliable, ordered byte stream with flow/congestion control | Three-way handshake, `SOCK_STREAM`, `TCP_NODELAY` |
| UDP | Connectionless datagram with no ordering guarantees | `SOCK_DGRAM`, `recvfrom()`/`sendto()`, multicast |
| Non-blocking I/O | `fcntl(fd, F_SETFL, O_NONBLOCK)` — returns `EAGAIN`/`EWOULDBLOCK` when no data available | Avoids blocking the event loop; required for epoll edge-triggered mode |
| epoll / kqueue | Kernel event notification for I/O readiness | `epoll_create1()`, `epoll_wait()` on Linux; `kqueue()`, `kevent()` on BSD/macOS |
| io_uring | Async I/O via shared kernel-user ring buffers | Zero-syscall I/O for extreme throughput; `io_uring_submit()` / `io_uring_wait_cqe()` |
| Reactor pattern | Event loop dispatches to registered handlers | Most epoll-based servers; Boost.Asio's `io_context` |
| Proactor pattern | OS performs I/O, notifies completion | Boost.Asio's async model; Windows IOCP |
| Connection pooling | Reuse TCP connections across requests | Reduces handshake overhead; `curl_multi_*` or custom pool |
| Backpressure | Slow down producers when consumer is overloaded | Prevents memory exhaustion under burst traffic |

## Internal Working

### Linux TCP Stack

```
Application → send() → TCP send buffer → NIC → wire
wire → NIC → TCP receive buffer → recv() → Application
```

1. **`send()` / `write()`**: Copies data from user buffer into kernel TCP send buffer. Returns immediately if buffer has space (non-blocking).
2. **TCP send buffer**: Kernel queues segments for transmission. Nagle's algorithm may batch small writes.
3. **NIC driver**: DMA transfers data from kernel buffer to NIC ring buffer. Offload checksumming/TSO to hardware.
4. **Wire**: Ethernet frame → IP packet → TCP segment.
5. **Receive path**: NIC → DMA to receive ring buffer → kernel TCP stack reorders/ACKs → `recv()` copies to user buffer.

### epoll Internal Mechanism

```
User process                      Kernel
─────────────                     ──────
epoll_create1()           →     Creates epoll instance (Red-Black tree + ready list)
epoll_ctl(ADD, fd, EV)   →     Inserts fd into RB tree, registers callback
                                   on socket ready → callback fires
                                   callback adds fd to ready list
epoll_wait()              →     Copies ready list to user space (O(ready events))
```

**Edge-triggered (EPOLLET)**: Only notifies when state *changes* (e.g., new data arrives). Requires reading until `EAGAIN` to avoid missing events. More efficient but harder to program.

**Level-triggered (default)**: Notifies whenever fd is ready. Simpler but may cause busy-looping with naive code.

### io_uring Flow

```
User space                          Kernel
──────────                          ──────
io_uring_setup()            →     Creates shared ring buffers (SQ/CQ)
io_uring_get_sqe()          →     Get submission queue entry
io_uring_prep_recv()        →     Prepare recv operation in SQE
io_uring_submit()           →     Write SQEs, notify kernel
                                   Kernel processes SQEs asynchronously
                                   Writes completions to CQ ring
io_uring_wait_cqe()         →     Read completion from CQ ring (zero-syscall)
```

io_uring eliminates per-I/O syscall overhead by batching submissions and completions in shared ring buffers. Achieves 2-3x throughput over epoll for high IOPS workloads.

### Non-Blocking I/O State Machine

```
             ┌──────────────┐
             │  WAITING     │  epoll_wait() / kqueue()
             └──────┬───────┘
                    │ event ready
             ┌──────▼───────┐
             │  READING     │  read() / recv()
             └──────┬───────┘
                    │ EAGAIN
             ┌──────▼───────┐
             │  BUFFERED    │  Accumulate partial message
             └──────┬───────┘
                    │ complete message
             ┌──────▼───────┐
             │  PROCESSING  │  Parse, handle, generate response
             └──────┬───────┘
                    │
             ┌──────▼───────┐
             │  WRITING     │  write() / send()
             └──────┬───────┘
                    │ EAGAIN or done
                    └──────→ back to WAITING
```

## Syntax

### POSIX Socket API

```cpp
// Create socket
int fd = socket(AF_INET, SOCK_STREAM, 0);  // TCP
int fd = socket(AF_INET, SOCK_DGRAM, 0);   // UDP

// Set options
int opt = 1;
setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, &opt, sizeof(opt));

// Bind and listen (server)
sockaddr_in addr{AF_INET, htons(port), INADDR_ANY};
bind(fd, (sockaddr*)&addr, sizeof(addr));
listen(fd, SOMAXCONN);  // backlog queue

// Accept (server)
sockaddr_in client;
socklen_t len = sizeof(client);
int client_fd = accept(fd, (sockaddr*)&client, &len);

// Connect (client)
sockaddr_in server{AF_INET, htons(port), {}};
inet_pton(AF_INET, "127.0.0.1", &server.sin_addr);
connect(fd, (sockaddr*)&server, sizeof(server));

// Non-blocking
fcntl(fd, F_SETFL, O_NONBLOCK);
```

### Boost.Asio Async Pattern

```cpp
#include <boost/asio.hpp>
using boost::asio::ip::tcp;

boost::asio::io_context io;
tcp::acceptor acceptor(io, {tcp::v4(), 8080});

// Async accept loop
acceptor.async_accept([&](auto ec, tcp::socket sock) {
    if (!ec) {
        // Handle connection
        auto buf = std::make_shared<std::array<char, 1024>>();
        async_read(sock, boost::asio::buffer(*buf),
            [buf, s = std::move(sock)](auto ec, auto n) {
                if (!ec) { /* process buf[0..n] */ }
            });
    }
    acceptor.async_accept(/* ... */);  // continue accepting
});

io.run();  // Event loop
```

### epoll Setup

```cpp
int epoll_fd = epoll_create1(0);

epoll_event ev{};
ev.events = EPOLLIN | EPOLLET;  // Edge-triggered read
ev.data.fd = server_fd;
epoll_ctl(epoll_fd, EPOLL_CTL_ADD, server_fd, &ev);

std::vector<epoll_event> events(128);
int n = epoll_wait(epoll_fd, events.data(), events.size(), -1);
for (int i = 0; i < n; ++i) {
    if (events[i].data.fd == server_fd) {
        // Accept new connection
    } else {
        // Read data from client
    }
}
```

## Examples

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

## Performance Considerations

| Technique | Description | Impact |
|-----------|-------------|--------|
| **Connection pooling** | Reuse TCP connections across requests | Eliminates per-request TCP handshake + TLS negotiation (30-100ms saved per request) |
| **Zero-copy I/O** | `sendfile()`, `splice()`, `MSG_ZEROCOPY` — data moves between kernel/NIC without user-space copy | 2-3x throughput for large file transfers; reduces CPU usage |
| **io_uring** | Shared ring buffers for async I/O — bypasses per-I/O syscall overhead | 2-3x throughput vs. epoll for high IOPS (100K+ ops/sec); lower latency |
| **TCP_NODELAY** | Disable Nagle's algorithm for latency-sensitive protocols | Eliminates up to 40ms batching delay per small message |
| **SO_REUSEPORT** | Kernel-level load balancing across multiple sockets | Enables multi-process scaling without accept serialization |
| **EPOLLET (edge-triggered)** | Notify only on state change, not continuously | Fewer epoll_wait wakeups; requires reading until EAGAIN |
| **Buffer management** | Pre-allocated buffer pools avoid per-connection malloc/free | Reduces heap fragmentation; 10-20% throughput improvement under load |
| **Protocol buffers (protobuf)** | Binary serialization vs. JSON | 5-10x smaller payloads, 2-5x faster serialization for structured data |
| **TCP_CORK** | Batch small writes into larger segments | Reduces packet count; use before sending, unset before final flush |
| **readv/writev (scatter-gather)** | Multiple buffers in a single syscall | Reduces syscall count; useful for header+body writes |

### Latency vs. Throughput Trade-offs

| Scenario | Best Approach | Why |
|----------|---------------|-----|
| Real-time gaming | UDP + custom reliability layer | No TCP head-of-line blocking; sub-millisecond latency |
| High-throughput file transfer | TCP + zero-copy + TCP_CORK | Maximize bandwidth utilization; minimize CPU overhead |
| High-concurrency HTTP | epoll + connection pooling + HTTP/2 | Multiplexed streams over single connection |
| Microservice RPC | gRPC (protobuf over HTTP/2) | Typed contracts, streaming, multiplexing |
| Ultra-low-latency | io_uring + kernel bypass (DPDK) | Eliminate kernel overhead entirely |

## Best Practices

1. **Always use RAII for sockets**: Wrap `socket()` in a class that calls `close()` in the destructor. Use move semantics to transfer ownership.
2. **Validate every packet size**: Never trust the client. Reject packets exceeding the maximum expected size before parsing.
3. **Use `snprintf`/`strncpy`**: Never `sprintf`/`strcpy` on network data. Always bound string operations.
4. **Handle partial reads/writes**: TCP is a byte stream. Loop until the complete message is received. Use a state machine for protocol parsing.
5. **Set timeouts on everything**: `connect()`, `read()`, `write()` can all block indefinitely. Use `SO_RCVTIMEO`/`SO_SNDTIMEO` or async timeouts.
6. **Edge-triggered epoll requires discipline**: When using `EPOLLET`, always read/write until `EAGAIN`. Missing this loses events silently.
7. **Cache DNS results**: Never call `getaddrinfo()` on the hot path. Cache results with TTL-based expiration.
8. **Monitor file descriptors**: Track `fd` count in production. Alert when approaching `ulimit -n`.
9. **Fuzz network parsers**: Use AFL/libFuzzer on all code that parses network input. Enable ASan/UBSan in CI.
10. **Use connection pooling**: Never create a new TCP connection per request in production. Pool connections with size limits.

## Common Mistakes

| Mistake | Symptom | Fix |
|---------|---------|-----|
| Using `sprintf()` on network input | Buffer overflow, segfault, RCE | Use `snprintf(buf, sizeof(buf), ...)` |
| Not handling partial reads | Protocol errors, hanging connections | Loop `read()` until complete message or `EAGAIN` |
| Blocking I/O on event loop | Entire server freezes during slow client I/O | Use non-blocking sockets + epoll/kqueue |
| Forgetting `close()` on error paths | File descriptor leak, eventual fd exhaustion | Use RAII wrappers; never return without cleanup |
| Using level-triggered + not reading all data | Missed events or busy-looping | Use edge-triggered + read until EAGAIN, or use level-triggered correctly |
| Hardcoding buffer sizes without validation | Crash on oversized packets | Validate packet size before copying into buffer |
| Not setting `TCP_NODELAY` on latency-sensitive protocols | 40ms Nagle delay per message | `setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, ...)` |
| Ignoring `EINTR` on signal interrupts | Spurious errors, connection drops | Retry `read()`/`write()` on `EINTR` |
| Creating one thread per connection | Thread limit hit at ~10K connections | Use event loop with epoll/kqueue or Boost.Asio async |
| No connection timeout | Zombie connections accumulate | Set `SO_KEEPALIVE` with tuned idle/interval/probes |

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

### Incident 4: TCP Accept Thundering Herd Under Spike Load
**Problem**: A WebSocket server experienced 5-second startup stalls when 50K clients connected simultaneously after a maintenance window. CPU usage spiked to 100% during the storm.

**Cause**: The server used a multi-process model with all workers calling `accept()` on the same listening socket. When the load spike hit, all 8 worker processes woke up simultaneously (thundering herd), each called `accept()`, and 7 of them got `EAGAIN` — wasting CPU cycles in a tight loop retrying `accept()`.

**Impact**: Server took 5 seconds to stabilize after each spike. 50K clients experienced connection timeouts. Some clients gave up and failed over to backup, causing cascading load.

**Detection**: `strace -p <pid>` showed thousands of `accept()` calls returning `EAGAIN`. Perf profiling revealed 60% CPU spent in kernel `tcp_accept()` contention.

**Solution**: Switched to `SO_REUSEPORT` (Linux 3.9+) — each worker gets its own socket with kernel-level load balancing, eliminating accept contention. Added `EPOLLEXCLUSIVE` flag to epoll for processes that can't use `SO_REUSEPORT`. Reduced workers from 8 to 4 (fewer processes, no contention).

**Prevention**: Use `SO_REUSEPORT` for multi-process servers. Add `EPOLLEXCLUSIVE` to prevent thundering herd. Benchmark under realistic spike conditions before deployment.

### Incident 5: TLS Certificate Expiry Causing Silent Failures
**Problem**: A payment processing service started silently dropping HTTPS connections at 2 AM on a Sunday. The service returned empty responses instead of errors, causing downstream services to cache stale data.

**Cause**: The TLS certificate expired at midnight. The OpenSSL version in use treated expired certificates as non-fatal for outgoing connections (it depends on `SSL_CTX_set_verify` mode). The server accepted expired client certs and returned empty responses instead of closing the connection cleanly. Monitoring didn't alert because the server was still "healthy" (accepting connections).

**Impact**: Payment processing silently failed for 6 hours. $200K in transactions were lost or duplicated when services retried. Customer trust damaged.

**Detection**: Downstream services reported empty response bodies. `openssl s_client -connect` showed expired certificate. Certificate monitoring had a bug — it checked the wrong certificate chain.

**Solution**: Set certificate expiration alerts at 30/14/7/1 days. Implemented strict TLS verification (`SSL_CTX_set_verify(SSL_VERIFY_PEER)`). Added health check that validates TLS handshake succeeds with valid cert. Fixed certificate monitoring to check the correct chain.

**Prevention**: Never rely on "it works" without verifying the TLS handshake. Automate certificate rotation (Let's Encrypt + certbot). Set alerts well before expiry. Test certificate expiry scenarios in staging.

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

## Cross-References

- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Thread pools, async patterns, mutexes
- **Performance** → [Module 11: Performance](../11-performance/) — Zero-copy, memory pools for buffers
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Error handling, RAII for resources
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Linking libcurl, Boost.Asio
- **Memory Management** → [Module 05: Memory](../05-memory-management/) — Buffer management, avoid leaks
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `std::optional` for results, lambdas for callbacks
- Thread pools for async I/O → [07-concurrency](../07-concurrency/) — Boost.Asio and custom async frameworks rely on thread pools
- RAII and move semantics → [08-modern-cpp](../08-modern-cpp/) — `std::unique_ptr<Socket>` enables safe socket ownership transfer
- `std::optional` for nullable returns → [08-modern-cpp](../08-modern-cpp/) — `std::optional<TcpMessage> receive()`
- Memory pools for buffer management → [05-memory-management](../05-memory-management/) — Pre-allocated buffer pools eliminate per-connection heap allocation
- Build system: linking Boost.ASIO, libcurl → [13-build-systems](../13-build-systems/) — CMake `find_package(Boost)`, pkg-config for libcurl
- Error handling patterns → [14-best-practices](../14-best-practices/) — `std::error_code` vs exceptions for network error propagation
- Compiler flags and sanitizers → [04-compilers](../04-compilers/) — `-fsanitize=address,undefined`, `-pthread`

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Buffer overflow from unchecked network input | AddressSanitizer + fuzzing (AFL/libFuzzer) | Run fuzzing on all network-facing parsers; enable ASan to catch overflows |
| Connection leak under load (fd exhaustion) | `lsof -p <pid> \| wc -l` + RAII audit | Monitor fd count; wrap all network handles in RAII classes |
| DNS resolution blocking event loop | `strace` + async DNS (c-ares) | Trace blocking calls on main thread; move DNS to background thread with caching |
| Partial read/write causing protocol parsing errors | Protocol state machine + loop until complete | Implement a state machine that accumulates data until a complete message is received |
| TLS handshake failure from expired certificate | `openssl s_client` + certificate monitoring | Check certificate expiry; implement certificate pinning with rotation plan |

## Code Review Checklist

- [ ] All incoming packet sizes validated before parsing
- [ ] `snprintf`/`strncpy` used instead of `sprintf`/`strcpy` on network data
- [ ] RAII used for all network resources (sockets, CURL handles)
- [ ] Timeouts set for all network operations (connect, read, write)
- [ ] Partial reads/writes handled with loops
- [ ] TLS/SSL used for all production network communication
- [ ] Connection pooling with size limits implemented
- [ ] Reconnection logic uses exponential backoff

## Architecture Considerations

Networking determines how systems communicate — from single TCP connections to millions of concurrent sockets. The I/O model (blocking, async, epoll/kqueue) defines scalability. Protocol choice (TCP, UDP, QUIC) determines reliability vs. latency trade-offs. Serialization format (protobuf, JSON, flatbuffers) impacts performance and interoperability. Architecture must define buffer management, connection lifecycle, and error recovery strategies.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Event loop with epoll/kqueue | High-concurrency server (10K+ connections) | Efficient scaling vs. complex callback-based programming |
| Connection pooling | Reusing TCP connections across requests | Avoids handshake overhead vs. pool size management complexity |
| RAII network wrappers | Automatic socket/CURL cleanup | Exception-safe but requires move-only semantics |

## Security Considerations | Risk | Impact | Mitigation |
|------|--------|------------|
| Buffer overflow from malformed network input | Remote code execution, critical vulnerability | Validate all input sizes; use `snprintf`; run AFL/libFuzzer on parsers |
| Connection leak causing fd exhaustion | Denial of service, server crash | RAII for all network handles; monitor fd count in production metrics |
| DNS spoofing redirecting traffic | Man-in-the-middle attack, data theft | Use DNSSEC; validate certificates; implement certificate pinning |

## Interview Questions

1. **What is the difference between TCP and UDP?**: TCP provides reliable, ordered byte streams with flow control and congestion control (three-way handshake). UDP provides fast, connectionless datagrams with no ordering or reliability. Use TCP for HTTP, SSH, databases; UDP for real-time gaming, DNS, video streaming.
2. **Explain the epoll/kqueue event-driven model**: `epoll` (Linux) and `kqueue` (macOS/BSD) provide I/O event notification — the kernel tells your application which sockets are ready for reading/writing. This enables handling thousands of connections with a single thread without blocking.
3. **Why must you handle partial reads/writes in network code?**: The network may deliver data in chunks — a `read()` of 1024 bytes may only return 100 bytes. You must loop until the complete message is received. This is especially important for TCP, which is a byte stream, not a message stream.
4. **What is connection pooling and why is it important?**: Connection pooling reuses TCP connections across multiple requests, avoiding the overhead of repeated TCP handshakes and TLS negotiations. It reduces latency and prevents file descriptor exhaustion under high load.
5. **How do you implement reconnection with exponential backoff?**: Start with a small delay (e.g., 100ms), double it on each failure (100ms, 200ms, 400ms, ...), cap at a maximum (e.g., 30s), and add jitter to prevent thundering herd. Reset the backoff on successful connection.
6. **What is the difference between edge-triggered and level-triggered epoll?**: Level-triggered notifies whenever a fd is ready (default). Edge-triggered notifies only on state *change*. Edge-triggered is more efficient but requires reading until `EAGAIN` to avoid losing events. Level-triggered is simpler but may cause busy-looping with naive code.
7. **How does io_uring differ from epoll?**: `io_uring` uses shared ring buffers between kernel and user space, eliminating per-I/O syscalls. Submit I/O operations via a submission queue, receive completions via a completion queue. Achieves 2-3x throughput over epoll for high IOPS workloads due to zero-syscall overhead.
8. **What is `SO_REUSEPORT` and when would you use it?**: `SO_REUSEPORT` allows multiple processes/threads to bind to the same port. The kernel distributes incoming connections across them via a hash-based load balancer. Use for multi-process servers to avoid accept thundering herd on the same listening socket.
9. **How do you handle `EINTR` in network code?**: `EINTR` means a signal interrupted the syscall. You must retry the operation. In a loop: if `read()`/`write()` returns `EINTR`, retry instead of treating it as an error. This prevents spurious connection drops on signal delivery.
10. **What is the problem with the thread-per-connection model?**: Each thread consumes ~8MB stack. At 10K connections, that's ~80GB of virtual memory. Thread context switches add overhead. Use event loops (epoll/kqueue) or async I/O (Boost.Asio, io_uring) instead.
11. **Explain Nagle's algorithm and when to disable it**: Nagle buffers small TCP writes to reduce packet count (combines small segments). This adds up to 40ms latency per message. Disable with `TCP_NODELAY` for latency-sensitive protocols (gaming, trading, SSH). Keep enabled for bulk transfers.
12. **How does Boost.Asio's Proactor pattern differ from the Reactor pattern?**: Reactor (epoll) notifies when an fd is *ready* — you call `read()`. Proactor (Boost.Asio, IOCP) completes the I/O and notifies you with the result. Proactor avoids non-blocking syscall complexity but requires OS support.
13. **What is zero-copy networking and when is it worth implementing?**: Zero-copy avoids copying data between user space and kernel. `sendfile()` transfers files directly from page cache to NIC. `MSG_ZEROCOPY` avoids copy on send. Worth it for large payloads (>4KB). Small messages benefit more from connection pooling than zero-copy.
14. **How would you design a chat server handling 100K concurrent connections?**: Single event loop with epoll/kqueue (edge-triggered), non-blocking sockets, per-connection state machines, connection pooling for outbound connections, backpressure via write buffer limits, optional io_uring on Linux 5.1+. Message broadcast via fan-out from epoll event handler.
15. **What are the security implications of network programming in C++?**: Buffer overflows from unchecked input (use `snprintf`). Missing TLS allows MITM attacks. DNS spoofing redirects traffic (use DNSSEC). Certificate validation must be strict. Never trust client-sent sizes. Fuzz all network-facing parsers. Enable ASan/UBSan in CI.
