# Networking — C Language

## What it is
Networking provides functions for communication between processes over networks.

## Why it exists
To enable distributed systems, client-server applications, and internet communication.

## When to use it
When you need network communication, web servers, or distributed applications.

## How it works

### Sockets

```c
#include <sys/socket.h>

int sockfd = socket(AF_INET, SOCK_STREAM, 0);
```

### Server

```c
struct sockaddr_in addr;
addr.sin_family = AF_INET;
addr.sin_port = htons(8080);
addr.sin_addr.s_addr = INADDR_ANY;

bind(sockfd, (struct sockaddr *)&addr, sizeof(addr));
listen(sockfd, 10);
int client = accept(sockfd, NULL, NULL);
```

### Client

```c
struct sockaddr_in addr;
addr.sin_family = AF_INET;
addr.sin_port = htons(8080);
inet_pton(AF_INET, "127.0.0.1", &addr.sin_addr);

connect(sockfd, (struct sockaddr *)&addr, sizeof(addr));
```

### Data Transfer

```c
send(sockfd, buffer, length, 0);
recv(sockfd, buffer, length, 0);
```

### UDP

```c
int sockfd = socket(AF_INET, SOCK_DGRAM, 0);
sendto(sockfd, buffer, length, 0, (struct sockaddr *)&addr, len);
recvfrom(sockfd, buffer, length, 0, (struct sockaddr *)&addr, &len);
```

## Production Checklist

- [ ] Handle connection errors
- [ ] Set socket timeouts
- [ ] Use non-blocking I/O when needed
- [ ] Validate all received data
- [ ] Handle partial sends/receives

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Creates basic TCP client/server |
| Intermediate | Handles multiple connections |
| Advanced | Implements async I/O and protocols |

## Common Myths

1. **Myth**: TCP is always better than UDP
   **Truth**: UDP is better for real-time applications where speed matters

2. **Myth**: One socket per connection
   **Truth**: Multiplexing (select/poll) allows many connections on fewer sockets

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Socket | Communication endpoint |
| TCP | Reliable, ordered stream |
| UDP | Unreliable, fast datagrams |
| bind | Associate address to socket |
| listen | Wait for connections |
| accept | Accept incoming connection |
| connect | Initiate connection |
| send/recv | Data transfer |

## Related Topics

- [Security](../11-security/README.md)
- [Concurrency](../09-concurrency/README.md)
