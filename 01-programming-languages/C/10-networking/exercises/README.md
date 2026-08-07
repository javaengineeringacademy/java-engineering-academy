# Networking Exercises

## Exercise 1: Echo Server
Create a TCP server that echoes back messages.

```c
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

int main(void) {
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_port = htons(8080),
        .sin_addr.s_addr = INADDR_ANY
    };

    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 10);

    int client = accept(server_fd, NULL, NULL);
    char buffer[1024];
    int n;
    while ((n = recv(client, buffer, sizeof(buffer), 0)) > 0) {
        send(client, buffer, n, 0);
    }

    close(client);
    close(server_fd);
    return 0;
}
```

## Exercise 2: HTTP Server
Implement a basic HTTP server that serves static files.

## Exercise 3: Chat Application
Create a multi-client chat server.

## Exercise 4: UDP Client/Server
Implement UDP communication.

## Exercise 5: Non-blocking I/O
Use select() for multiplexed I/O.
