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
