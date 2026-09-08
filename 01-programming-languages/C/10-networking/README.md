# Networking — C Language

## Why It Matters

When you're building web servers, databases, APIs, or any application that communicates across processes, machines, or networks, you need networking. Without it, there are no web servers, no databases, no internet. C's BSD sockets API is the universal foundation for every networked application, giving you maximum control and performance — close to the metal — along with maximum responsibility for managing connections, buffers, and protocols directly.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | TCP/UDP servers, low-level network programming | High-level frameworks (libuv, libevent) for apps |
| When NOT to use | Simple HTTP clients (use libcurl) | Don't reimplement TLS, DNS resolution |
| Alternatives | Go net, Rust tokio, libuv, Boost.Asio | Higher-level abstractions, different trade-offs |
| Production Examples | Nginx, HAProxy, Redis (TCP), memcached | Event-driven for high concurrency |
| Common Mistakes | Not handling partial sends, missing `SO_REUSEADDR`, not closing fds | Loop on send, set `SO_REUSEADDR`, close in all paths |

## What It Is

C networking operates through the BSD socket API:

| Concept | System Call | Purpose |
|---------|------------|---------|
| Create socket | `socket()` | Create communication endpoint |
| Bind address | `bind()` | Associate socket with address/port |
| Listen | `listen()` | Wait for incoming connections |
| Accept | `accept()` | Accept incoming connection |
| Connect | `connect()` | Initiate outgoing connection |
| Send data | `send()` / `write()` | Send data to peer |
| Receive data | `recv()` / `read()` | Receive data from peer |
| Close | `close()` | Close socket |

## Why It Exists

The BSD socket API was developed at Berkeley in the 1980s and became the universal network programming interface. It exists because:

- **Universality**: Same API works for TCP, UDP, Unix sockets, and more
- **Portability**: Available on Unix, Linux, macOS, Windows (with minor differences)
- **Performance**: Direct kernel access, no middleware overhead
- **Flexibility**: Supports blocking, non-blocking, and multiplexed I/O

### Architecture: Client-Server Model

```
Client                    Server
  │                         │
  │──── socket() ──────────→│
  │──── connect() ─────────→│ bind()
  │                         │ listen()
  │                         │ accept()
  │──── send(request) ─────→│
  │←─── recv(response) ─────│
  │──── close() ───────────→│ close()
```

## Expanded Code Examples

### TCP Server — Complete Example

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int create_server(int port) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) {
        perror("socket");
        return -1;
    }

    // Allow address reuse
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    if (bind(server_fd, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        perror("bind");
        close(server_fd);
        return -1;
    }

    if (listen(server_fd, 10) < 0) {
        perror("listen");
        close(server_fd);
        return -1;
    }

    printf("Server listening on port %d\n", port);
    return server_fd;
}

void handle_client(int client_fd) {
    char buffer[BUFFER_SIZE];
    ssize_t bytes_read;

    while ((bytes_read = recv(client_fd, buffer, sizeof(buffer) - 1, 0)) > 0) {
        buffer[bytes_read] = '\0';
        printf("Received: %s", buffer);

        // Echo back
        send(client_fd, buffer, bytes_read, 0);
    }

    if (bytes_read == 0) {
        printf("Client disconnected\n");
    } else {
        perror("recv");
    }

    close(client_fd);
}

int main(void) {
    int server_fd = create_server(PORT);
    if (server_fd < 0) return 1;

    while (1) {
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);

        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &client_len);
        if (client_fd < 0) {
            perror("accept");
            continue;
        }

        printf("Client connected from %s:%d\n",
               inet_ntoa(client_addr.sin_addr),
               ntohs(client_addr.sin_port));

        handle_client(client_fd);
    }

    close(server_fd);
    return 0;
}
```

### TCP Client

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

int connect_to_server(const char *host, int port) {
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("socket");
        return -1;
    }

    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);

    if (inet_pton(AF_INET, host, &addr.sin_addr) <= 0) {
        fprintf(stderr, "Invalid address: %s\n", host);
        close(sock);
        return -1;
    }

    if (connect(sock, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        perror("connect");
        close(sock);
        return -1;
    }

    return sock;
}

int main(void) {
    int sock = connect_to_server("127.0.0.1", 8080);
    if (sock < 0) return 1;

    const char *msg = "Hello, server!\n";
    send(sock, msg, strlen(msg), 0);

    char buffer[1024];
    ssize_t n = recv(sock, buffer, sizeof(buffer) - 1, 0);
    if (n > 0) {
        buffer[n] = '\0';
        printf("Server replied: %s", buffer);
    }

    close(sock);
    return 0;
}
```

### UDP Socket

```c
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

// UDP Server
int udp_server(int port) {
    int sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) return -1;

    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    if (bind(sock, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        close(sock);
        return -1;
    }

    char buffer[1024];
    struct sockaddr_in client_addr;
    socklen_t client_len = sizeof(client_addr);

    ssize_t n = recvfrom(sock, buffer, sizeof(buffer) - 1, 0,
                         (struct sockaddr *)&client_addr, &client_len);
    if (n > 0) {
        buffer[n] = '\0';
        printf("UDP received: %s\n", buffer);

        // Echo back
        sendto(sock, buffer, n, 0,
               (struct sockaddr *)&client_addr, client_len);
    }

    close(sock);
    return 0;
}
```

### Multiplexed I/O with select()

```c
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/select.h>

#define MAX_CLIENTS 64
#define BUFFER_SIZE 1024

void run_server(int port) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_addr.s_addr = INADDR_ANY,
        .sin_port = htons(port)
    };

    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 10);

    int clients[MAX_CLIENTS] = {0};
    int max_fd = server_fd;

    while (1) {
        fd_set readfds;
        FD_ZERO(&readfds);
        FD_SET(server_fd, &readfds);

        for (int i = 0; i < MAX_CLIENTS; i++) {
            if (clients[i] > 0) {
                FD_SET(clients[i], &readfds);
                if (clients[i] > max_fd) max_fd = clients[i];
            }
        }

        int activity = select(max_fd + 1, &readfds, NULL, NULL, NULL);
        if (activity < 0) continue;

        // New connection
        if (FD_ISSET(server_fd, &readfds)) {
            struct sockaddr_in client_addr;
            socklen_t len = sizeof(client_addr);
            int new_fd = accept(server_fd, (struct sockaddr *)&client_addr, &len);

            for (int i = 0; i < MAX_CLIENTS; i++) {
                if (clients[i] == 0) {
                    clients[i] = new_fd;
                    break;
                }
            }
        }

        // Handle existing clients
        for (int i = 0; i < MAX_CLIENTS; i++) {
            if (clients[i] > 0 && FD_ISSET(clients[i], &readfds)) {
                char buffer[BUFFER_SIZE];
                ssize_t n = recv(clients[i], buffer, sizeof(buffer) - 1, 0);
                if (n <= 0) {
                    close(clients[i]);
                    clients[i] = 0;
                } else {
                    buffer[n] = '\0';
                    send(clients[i], buffer, n, 0);
                }
            }
        }
    }
}
```

## Production Incidents

### Incident 1: Partial Send Causing Corrupted Data

**Problem**: Large messages are sometimes truncated or corrupted.

**Cause**: `send()` may not send all bytes in one call:

```c
send(sock, buffer, total_length, 0);  // May send fewer bytes
```

**Solution**: Loop until all bytes are sent:

```c
ssize_t send_all(int sock, const char *buf, size_t len) {
    size_t sent = 0;
    while (sent < len) {
        ssize_t n = send(sock, buf + sent, len - sent, 0);
        if (n <= 0) return -1;
        sent += n;
    }
    return sent;
}
```

### Incident 2: Connection Leak from Missing close()

**Problem**: Server runs out of file descriptors after handling many connections.

**Cause**: Connections are not closed on error paths:

```c
int client_fd = accept(server_fd, ...);
if (fork() == 0) {
    handle_client(client_fd);
    exit(0);
}
// parent doesn't close client_fd — fd leak
```

**Solution**: Close file descriptors in all code paths:

```c
int client_fd = accept(server_fd, ...);
if (client_fd < 0) continue;
pid_t pid = fork();
if (pid == 0) {
    close(server_fd);
    handle_client(client_fd);
    close(client_fd);
    exit(0);
}
close(client_fd);  // Parent closes client fd
```

### Incident 3: Socket File Descriptor Leak in select() Loop

**Problem**: A server using `select()` leaks file descriptors when clients disconnect abruptly.

```c
while (true) {
    fd_set read_fds;
    FD_ZERO(&read_fds);
    FD_SET(server_fd, &read_fds);
    for (int i = 0; i < max_clients; i++) {
        if (client_fds[i] >= 0) FD_SET(client_fds[i], &read_fds);
    }
    select(max_fd + 1, &read_fds, NULL, NULL, NULL);
    
    if (FD_ISSET(server_fd, &read_fds)) {
        int new_fd = accept(server_fd, NULL, NULL);
        add_client(new_fd);  // May fail if array is full
    }
    for (int i = 0; i < max_clients; i++) {
        if (client_fds[i] >= 0 && FD_ISSET(client_fds[i], &read_fds)) {
            char buf[1024];
            ssize_t n = recv(client_fds[i], buf, sizeof(buf), 0);
            if (n <= 0) {
                close(client_fds[i]);
                client_fds[i] = -1;
                // But new_fd from accept may have been leaked
            }
        }
    }
}
```

**Cause**: If `accept()` succeeds but `add_client()` fails, the new file descriptor is leaked.

**Impact**: File descriptor exhaustion, service failure.

**Solution**: Close the new fd immediately if add_client fails:

```c
if (FD_ISSET(server_fd, &read_fds)) {
    int new_fd = accept(server_fd, NULL, NULL);
    if (new_fd >= 0) {
        if (add_client(new_fd) < 0) {
            close(new_fd);  // Close immediately on failure
        }
    }
}
```

**Prevention**: Always close file descriptors on error paths; track max fd for select(); use epoll/kqueue for scalability.

---

### Incident 4: SIGPIPE Crash in Network Server

**Problem**: A server crashes when a client disconnects during a write operation.

```c
void handle_client(int client_fd) {
    char response[] = "HTTP/1.1 200 OK\r\nContent-Length: 13\r\n\r\nHello, World!";
    write(client_fd, response, sizeof(response) - 1);  // May generate SIGPIPE
}
```

**Cause**: Writing to a broken connection generates SIGPIPE, which terminates the process by default.

**Impact**: Server crash, service unavailability.

**Solution**: Ignore SIGPIPE or use MSG_NOSIGNAL:

```c
// Option 1: Ignore SIGPIPE globally
signal(SIGPIPE, SIG_IGN);

// Option 2: Use MSG_NOSIGNAL flag
send(client_fd, response, len, MSG_NOSIGNAL);

// Option 3: Handle SIGPIPE with sigaction
struct sigaction sa;
sa.sa_handler = SIG_IGN;
sigemptyset(&sa.sa_mask);
sa.sa_flags = 0;
sigaction(SIGPIPE, &sa, NULL);
```

**Prevention**: Always handle SIGPIPE; use MSG_NOSIGNAL; install signal handler before creating threads.

---

### Incident 5: Buffer Overflow in HTTP Header Parsing

**Problem**: A malformed HTTP request causes a buffer overflow in the header parser.

```c
void parse_headers(int client_fd) {
    char buffer[4096];
    ssize_t n = recv(client_fd, buffer, sizeof(buffer) - 1, 0);
    buffer[n] = '\0';
    
    char *line = strtok(buffer, "\r\n");
    while (line) {
        char *colon = strchr(line, ':');
        if (colon) {
            *colon = '\0';
            char *value = colon + 1;
            // No bounds checking on value
            strcpy(header_value, value);  // Overflow if value > buffer size
        }
        line = strtok(NULL, "\r\n");
    }
}
```

**Cause**: No bounds checking on header values; attacker can send oversized headers.

**Impact**: Stack buffer overflow, remote code execution.

**Solution**: Use bounded string functions and validate header size:

```c
void parse_headers(int client_fd) {
    char buffer[4096];
    ssize_t n = recv(client_fd, buffer, sizeof(buffer) - 1, 0);
    if (n <= 0) return;
    buffer[n] = '\0';
    
    char *line = strtok(buffer, "\r\n");
    while (line) {
        char *colon = strchr(line, ':');
        if (colon) {
            *colon = '\0';
            char *value = colon + 1;
            // Skip leading whitespace
            while (*value == ' ') value++;
            strncpy(header_value, value, sizeof(header_value) - 1);
            header_value[sizeof(header_value) - 1] = '\0';
        }
        line = strtok(NULL, "\r\n");
    }
}
```

**Prevention**: Always use bounded string functions; validate input size; enable stack protector (`-fstack-protector-strong`).

## Production Checklist

- [ ] Set `SO_REUSEADDR` on server sockets
- [ ] Handle partial sends/receives with loops
- [ ] Set socket timeouts to avoid blocking forever
- [ ] Validate all received data before processing
- [ ] Close sockets in all code paths (including error paths)
- [ ] Use `select()`/`poll()`/`epoll` for concurrent connections
- [ ] Handle `SIGPIPE` signal (broken pipe)
- [ ] Use non-blocking I/O for high-performance servers
- [ ] Validate address strings with `inet_pton`

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Creates basic TCP client/server | Uses socket, bind, listen, accept, connect |
| **Intermediate** | Handles multiple connections | Uses select/poll, handles errors |
| **Advanced** | Implements async I/O and protocols | Uses epoll/kqueue, implements HTTP parser |
| **Expert** | Designs high-performance network servers | Event-driven architecture, zero-copy I/O |

## Common Myths Debunked

1. **Myth**: TCP is always better than UDP
   **Truth**: UDP is better for real-time applications (gaming, video, DNS) where speed matters more than reliability.

2. **Myth**: One socket per connection
   **Truth**: `select()`/`poll()`/`epoll` allow handling thousands of connections on a single thread with non-blocking I/O.

3. **Myth**: `send()` always sends all data
   **Truth**: `send()` may send fewer bytes than requested. Always loop until all data is sent.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Socket | Communication endpoint | `socket()` returns file descriptor |
| TCP | Reliable, ordered stream | `SOCK_STREAM` |
| UDP | Unreliable, fast datagrams | `SOCK_DGRAM` |
| bind | Associate address to socket | Must set `SO_REUSEADDR` |
| listen | Wait for connections | Backlog parameter |
| accept | Accept incoming connection | Returns new fd for client |
| connect | Initiate connection | Blocks until connected |
| select | Multiplexed I/O | Wait for activity on multiple fds |

## Related Topics

- [Concurrency](../09-concurrency/README.md) — Multi-threaded servers
- [Security](../11-security/README.md) — Secure network communication (TLS, input validation)
- [Performance](../12-performance/README.md) — High-performance networking

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Partial send causing truncated messages | `strace -e sendto` | Trace syscalls to verify all bytes are sent; implement send loop checking return value |
| Connection leak (fd exhaustion) | `lsof -p PID` / `ls /proc/PID/fd` | Count open file descriptors; identify leaked sockets in error paths |
| `SIGPIPE` crashing server | `signal(SIGPIPE, SIG_IGN)` | Ignore or handle `SIGPIPE`; use `send` with `MSG_NOSIGNAL` flag on Linux |
| Blocking `accept`/`recv` freezing server | Set socket timeouts | Use `setsockopt(SO_RCVTIMEO, SO_SNDTIMEO)` to prevent indefinite blocking |
| DNS resolution hanging | Non-blocking `getaddrinfo` | Use `getaddrinfo` with `AI_ADDRCONFIG`; implement timeout around resolution |

## Code Review Checklist

- [ ] `SO_REUSEADDR` set on server sockets before `bind`
- [ ] Partial sends handled with loop (check `send` return, retry until all bytes sent)
- [ ] Socket timeouts set (`SO_RCVTIMEO`, `SO_SNDTIMEO`) to prevent indefinite blocking
- [ ] All received data validated before processing (bounds, format, content)
- [ ] File descriptors closed in all code paths (including error and fork paths)
- [ ] `SIGPIPE` handled (ignored or caught) to prevent server crash
- [ ] Address validation performed with `inet_pton` (not `inet_addr`)

## Architecture Considerations

C networking is built on the BSD socket API, which provides a universal abstraction for TCP, UDP, and Unix sockets. For high-concurrency servers, choose between thread-per-connection (simple), event-driven with `epoll`/`kqueue` (scalable), or hybrid models (thread pool + event loop). The choice depends on connection count, I/O patterns, and latency requirements.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Thread-per-connection | Simple servers, low connection count | Simple code but limited scalability (thread overhead) |
| Event-driven (epoll/kqueue) | High-concurrency servers (10K+ connections) | Single-threaded simplicity; scales to millions of connections |
| Thread pool + event loop | Mixed CPU/I/O workloads | Combines scalability with CPU parallelism |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Buffer overflow from untrusted network input | Remote code execution | Validate all input; use bounded `recv` with size limits; enable `-fstack-protector-strong` |
| Denial of service (connection flood) | Server exhaustion | Limit concurrent connections; set `SO_RCVBUF`/`SO_SNDBUF`; use `accept` throttling |
| Unencrypted data transmission | Data interception | Use TLS (OpenSSL, mbedTLS); never send credentials in plaintext |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `getaddrinfo` (reentrant DNS), improved error handling | Replace `gethostbyname` with `getaddrinfo` for thread safety and IPv6 support |
| C99 → C11 | Added `<threads.h>` for concurrent servers, `<stdatomic.h>` | Use C11 threads for portable multi-threaded servers |
| C11 → C23 | Improved `constexpr`, potential async I/O support | Adopt modern async frameworks (libuv, libevent) for high-performance servers |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| BSD sockets (`socket`, `bind`, `listen`) | POSIX (not C standard) | Universal on Unix; use Winsock on Windows |
| `getaddrinfo` (reentrant DNS) | POSIX | Standard on Unix; use `GetAddrInfoW` on Windows |
| `<threads.h>` for concurrent servers | C11 | Limited platform support; prefer pthreads on Unix |
| Non-blocking I/O (`fcntl O_NONBLOCK`) | POSIX | Standard on Unix; use `ioctlsocket` on Windows |

## Interview Questions

1. **Why must you set `SO_REUSEADDR` on server sockets?**: Without `SO_REUSEADDR`, a server cannot restart immediately after closing — the port remains in `TIME_WAIT` state (typically 60 seconds). `SO_REUSEADDR` allows binding to the port immediately, enabling fast server restarts.
2. **How do you handle partial sends in `send()`?**: `send()` may send fewer bytes than requested (buffer full, signal interruption). Loop until all bytes are sent: `while (sent < len) { ssize_t n = send(fd, buf+sent, len-sent, 0); if (n <= 0) return -1; sent += n; }`.
3. **What is the difference between TCP and UDP and when to use each?**: TCP provides reliable, ordered, byte-stream communication (HTTP, SMTP, databases). UDP provides fast, unreliable datagrams (DNS, gaming, video streaming). Use TCP when reliability matters; use UDP when speed matters and loss is acceptable.
4. **How does `epoll` differ from `select` and why is it preferred?**: `select` has a hard limit of 1024 file descriptors and O(n) scan on every call. `epoll` scales to millions of FDs, uses O(1) event notification, and avoids rebuilding fd_sets. Use `epoll` on Linux, `kqueue` on macOS/BSD.
5. **What causes `SIGPIPE` and how do you handle it?**: `SIGPIPE` is sent when writing to a socket that has been closed by the peer. The default action is to terminate the process. Handle it by ignoring the signal (`signal(SIGPIPE, SIG_IGN)`) and checking `send` return for `EPIPE` error.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Unix Network Programming (Stevens)](https://www.unixnetworkprogramming.org/)
- [Beej's Guide to Network Programming](https://beej.us/guide/bgnet/)

## Overview

The Networking module covers network programming in C using the BSD sockets API. This is the universal foundation for every networked application, giving you maximum control and performance for TCP/UDP servers, clients, and multiplexed I/O.

## Learning Objectives

- Create TCP servers and clients with BSD sockets
- Implement UDP socket communication
- Use multiplexed I/O with `select()` and `poll()`
- Handle partial sends and receives
- Implement proper connection management

## Prerequisites

- Completion of Module 09 (Concurrency)
- Understanding of file descriptors
- Basic understanding of IP addresses and ports

## History

- **1972** — ARPANET protocols developed
- **1978** — BSD sockets API introduced in 4.2BSD
- **1989** — ANSI C standardized socket functions
- **1993** — POSIX standardized networking APIs
- **2001** — POSIX.1-2001 standardized `poll()`
- **2017** — C17 bug fix release
- **2023** — C23 added improved type inference

## Production Notes

- **Where is it used?** Web servers, databases, APIs, microservices
- **Why is it useful?** Maximum control, performance, close to the metal
- **When should it be avoided?** Simple HTTP clients (use libcurl)
- **Alternative?** Go net, Rust tokio, libuv, Boost.Asio

## Core Concepts

### BSD Socket API

| Concept | System Call | Purpose |
|---------|------------|---------|
| Create socket | `socket()` | Create communication endpoint |
| Bind address | `bind()` | Associate socket with address/port |
| Listen | `listen()` | Wait for incoming connections |
| Accept | `accept()` | Accept incoming connection |
| Connect | `connect()` | Initiate outgoing connection |
| Send data | `send()` / `write()` | Send data to peer |
| Receive data | `recv()` / `read()` | Receive data from peer |
| Close | `close()` | Close socket |

### Socket Types

| Type | Protocol | Use Case |
|------|----------|----------|
| SOCK_STREAM | TCP | Reliable, ordered data |
| SOCK_DGRAM | UDP | Unreliable, fast data |
| SOCK_RAW | IP | Raw IP packets |

## Internal Working

### TCP Connection Lifecycle

```
Server:
socket() → bind() → listen() → accept() → recv()/send() → close()

Client:
socket() → connect() → send()/recv() → close()
```

### Three-Way Handshake

```
Client → Server: SYN
Server → Client: SYN-ACK
Client → Server: ACK
Connection established
```

## Syntax

```c
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>

// Create socket
int sockfd = socket(AF_INET, SOCK_STREAM, 0);

// Server setup
struct sockaddr_in server_addr;
server_addr.sin_family = AF_INET;
server_addr.sin_port = htons(8080);
server_addr.sin_addr.s_addr = INADDR_ANY;

bind(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr));
listen(sockfd, 10);

int client_fd = accept(sockfd, NULL, NULL);

// Client setup
struct sockaddr_in server_addr;
server_addr.sin_family = AF_INET;
server_addr.sin_port = htons(8080);
inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr);

connect(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr));

// Send/Receive
send(sockfd, buffer, len, 0);
recv(sockfd, buffer, sizeof(buffer), 0);

// Close
close(sockfd);
```

## Examples

### Easy Example: Simple TCP Server

```c
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

int main(void) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    
    struct sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(8080);
    addr.sin_addr.s_addr = INADDR_ANY;
    
    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 5);
    
    printf("Server listening on port 8080\n");
    
    int client_fd = accept(server_fd, NULL, NULL);
    char buffer[1024] = {0};
    recv(client_fd, buffer, sizeof(buffer), 0);
    printf("Received: %s\n", buffer);
    
    close(client_fd);
    close(server_fd);
    return 0;
}
```

### Medium Example: TCP Client-Server

```c
// server.c
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

int main(void) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
    
    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_port = htons(8080),
        .sin_addr.s_addr = INADDR_ANY
    };
    
    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 5);
    
    int client_fd = accept(server_fd, NULL, NULL);
    char buffer[1024];
    ssize_t n = recv(client_fd, buffer, sizeof(buffer) - 1, 0);
    buffer[n] = '\0';
    printf("Client says: %s\n", buffer);
    
    send(client_fd, "Hello from server!", 18, 0);
    
    close(client_fd);
    close(server_fd);
    return 0;
}

// client.c
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>

int main(void) {
    int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    
    struct sockaddr_in server_addr = {
        .sin_family = AF_INET,
        .sin_port = htons(8080)
    };
    inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr);
    
    connect(sock_fd, (struct sockaddr *)&server_addr, sizeof(server_addr));
    
    send(sock_fd, "Hello from client!", 18, 0);
    
    char buffer[1024] = {0};
    recv(sock_fd, buffer, sizeof(buffer), 0);
    printf("Server says: %s\n", buffer);
    
    close(sock_fd);
    return 0;
}
```

### Hard Example: Multiplexed I/O

```c
#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <sys/select.h>

int main(void) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
    
    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_port = htons(8080),
        .sin_addr.s_addr = INADDR_ANY
    };
    
    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 5);
    
    fd_set read_fds;
    int max_fd = server_fd;
    
    while (1) {
        FD_ZERO(&read_fds);
        FD_SET(server_fd, &read_fds);
        
        select(max_fd + 1, &read_fds, NULL, NULL, NULL);
        
        if (FD_ISSET(server_fd, &read_fds)) {
            int client_fd = accept(server_fd, NULL, NULL);
            FD_SET(client_fd, &read_fds);
            if (client_fd > max_fd) max_fd = client_fd;
        }
        
        for (int i = 0; i <= max_fd; i++) {
            if (i != server_fd && FD_ISSET(i, &read_fds)) {
                char buffer[1024];
                ssize_t n = recv(i, buffer, sizeof(buffer), 0);
                if (n <= 0) {
                    close(i);
                    FD_CLR(i, &read_fds);
                } else {
                    send(i, buffer, n, 0);
                }
            }
        }
    }
    return 0;
}
```

### Enterprise Example: HTTP Server

```c
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

void handle_client(int client_fd) {
    char buffer[4096];
    recv(client_fd, buffer, sizeof(buffer), 0);
    
    const char *response =
        "HTTP/1.1 200 OK\r\n"
        "Content-Type: text/html\r\n"
        "Content-Length: 13\r\n"
        "\r\n"
        "Hello, World!";
    
    send(client_fd, response, strlen(response), 0);
    close(client_fd);
}

int main(void) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
    
    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_port = htons(80),
        .sin_addr.s_addr = INADDR_ANY
    };
    
    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 128);
    
    while (1) {
        int client_fd = accept(server_fd, NULL, NULL);
        if (fork() == 0) {
            close(server_fd);
            handle_client(client_fd);
            _exit(0);
        }
        close(client_fd);
    }
    return 0;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Blocking I/O | Thread per connection | Use select/poll/epoll |
| Buffer copying | Kernel/user space | Use zero-copy (sendfile) |
| Connection setup | TCP handshake | Use connection pooling |
| Small writes | Nagle's algorithm | Use TCP_NODELAY |
| Large data | Multiple send calls | Use scatter-gather I/O |

## Best Practices

- Do:
  - Always check return values from socket functions
  - Set `SO_REUSEADDR` for servers
  - Handle partial sends and receives
  - Close sockets in all code paths
  - Use non-blocking I/O for high concurrency
  
- Don't:
  - Ignore errors from `send`/`recv`
  - Forget to close file descriptors
  - Block on I/O in event loops
  - Assume `send` sends all data
  - Use `gets` for network input

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Partial send | Corrupted data | Loop until all data sent |
| Missing `SO_REUSEADDR` | Address already in use | Set before `bind` |
| Connection leak | Resource exhaustion | Close in all paths |
| Blocking I/O in select | Starvation | Use non-blocking sockets |
| Not handling EINTR | Interrupted system calls | Retry on signal interruption |

## Interview Questions

### Q1: What is the difference between TCP and UDP?
**Answer:** TCP: reliable, ordered, connection-oriented. UDP: unreliable, unordered, connectionless. TCP for correctness; UDP for speed.

### Q2: What is the three-way handshake?
**Answer:** TCP connection establishment: SYN → SYN-ACK → ACK. Ensures both sides are ready.

### Q3: What is `SO_REUSEADDR`?
**Answer:** Allows binding to an address that's in TIME_WAIT state. Essential for servers to restart quickly.

### Q4: What is the difference between `select` and `poll`?
**Answer:** `select` uses fd_set (limited to 1024 fds). `poll` uses array of pollfd structs (no limit). Both have O(n) performance.

### Q5: What is the difference between `send` and `write`?
**Answer:** `send` has flags parameter (e.g., `MSG_NOSIGNAL`). `write` is simpler. Both work for sockets.

### Q6: What is the difference between `recv` and `read`?
**Answer:** `recv` has flags parameter (e.g., `MSG_PEEK`). `read` is simpler. Both work for sockets.

### Q7: What is the difference between blocking and non-blocking I/O?
**Answer:** Blocking: waits for data. Non-blocking: returns immediately with EAGAIN/EWOULDBLOCK if no data.

### Q8: What is the difference between `epoll` and `select`?
**Answer:** `epoll` (Linux) uses event-based notification. `select` polls all fds. `epoll` is O(1) for event retrieval.

### Q9: What is the difference between `listen` backlog and `accept` queue?
**Answer:** `listen` backlog: pending connections queue. `accept` queue: completed connections ready for `accept`.

### Q10: What is `connect` timeout?
**Answer:** Time limit for TCP handshake. Set with `setsockopt` `SO_SNDTIMEO`. Important for client responsiveness.

### Q11: What is the difference between `shutdown` and `close`?
**Answer:** `shutdown`: half-duplex (close read/write). `close`: full close. `shutdown` allows graceful close.

### Q12: What is `SIGPIPE`?
**Answer:** Signal sent when writing to a broken pipe. Default action is terminate. Ignore with `signal(SIGPIPE, SIG_IGN)`.

### Q13: What is the difference between `gethostbyname` and `getaddrinfo`?
**Answer:** `gethostbyname`: IPv4 only, not thread-safe. `getaddrinfo`: IPv4/IPv6, thread-safe, preferred.

### Q14: What is the difference between `htons` and `htonl`?
**Answer:** `htons`: host to network short (16-bit). `htonl`: host to network long (32-bit). For portability across byte orders.

### Q15: What is the difference between `inet_ntoa` and `inet_ntop`?
**Answer:** `inet_ntoa`: IPv4 only, returns static buffer (not thread-safe). `inet_ntop`: IPv4/IPv6, thread-safe, preferred.

## Cross-References

- **Previous Module:** [09 - Concurrency](../09-concurrency/)
- **Next Module:** [11 - Security](../11-security/)
- **Related:** [04 - File I/O](../04-file-io/) — File descriptors
- **Related:** [05 - Pointers Advanced](../05-pointers-advanced/) — Callbacks
- **External:** [Unix Network Programming](https://www.unixnetworkprogramming.org/)
- **External:** [Beej's Guide to Network Programming](https://beej.us/guide/bgnet/)
