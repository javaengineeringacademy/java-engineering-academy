/*
 * Networking — C Language
 * Solutions: Complete implementations for all exercises
 *
 * Compile with: gcc -Wall -Wextra -std=c99 -o solutions solutions.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <signal.h>

#define PORT 8080
#define BUFFER_SIZE 1024

static volatile int running = 1;

void handle_signal(int sig) {
    (void)sig;
    running = 0;
}

/* ============================================================
 * Problem 1: TCP Echo Server
 * ============================================================ */
void problem1_echo_server(void) {
    printf("=== Problem 1: TCP Echo Server ===\n");

    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) { perror("socket"); return; }

    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    struct sockaddr_in addr = {
        .sin_family = AF_INET,
        .sin_addr.s_addr = INADDR_ANY,
        .sin_port = htons(PORT)
    };

    if (bind(server_fd, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        perror("bind"); close(server_fd); return;
    }

    if (listen(server_fd, 5) < 0) {
        perror("listen"); close(server_fd); return;
    }

    printf("  Server listening on port %d\n", PORT);
    printf("  (Connect with: nc localhost %d)\n", PORT);

    signal(SIGINT, handle_signal);

    while (running) {
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &client_len);
        if (client_fd < 0) continue;

        printf("  Client connected from %s:%d\n",
               inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));

        char buffer[BUFFER_SIZE];
        ssize_t n;
        while ((n = recv(client_fd, buffer, sizeof(buffer) - 1, 0)) > 0) {
            buffer[n] = '\0';
            if (strncmp(buffer, "quit", 4) == 0) break;
            printf("  Received: %s", buffer);
            send(client_fd, buffer, n, 0);
        }

        printf("  Client disconnected\n");
        close(client_fd);
    }

    close(server_fd);
    printf("  Server shut down\n\n");
}

/* ============================================================
 * Problem 2: TCP Client
 * ============================================================ */
void problem2_tcp_client(void) {
    printf("=== Problem 2: TCP Client ===\n");

    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) { perror("socket"); return; }

    struct sockaddr_in server_addr = {
        .sin_family = AF_INET,
        .sin_port = htons(PORT)
    };
    inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr);

    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("connect"); close(sock); return;
    }

    printf("  Connected to server\n");

    const char *messages[] = {"Hello, server!\n", "This is a test.\n", "quit\n"};
    char buffer[BUFFER_SIZE];

    for (int i = 0; i < 3; i++) {
        send(sock, messages[i], strlen(messages[i]), 0);
        ssize_t n = recv(sock, buffer, sizeof(buffer) - 1, 0);
        if (n > 0) {
            buffer[n] = '\0';
            printf("  Echo: %s", buffer);
        }
    }

    close(sock);
    printf("  Client disconnected\n\n");
}

/* ============================================================
 * Problem 3: UDP Sender/Receiver
 * ============================================================ */
void problem3_udp(void) {
    printf("=== Problem 3: UDP Communication ===\n");

    /* Receiver */
    int recv_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (recv_fd < 0) { perror("socket"); return; }

    struct sockaddr_in recv_addr = {
        .sin_family = AF_INET,
        .sin_addr.s_addr = INADDR_ANY,
        .sin_port = htons(9090)
    };

    if (bind(recv_fd, (struct sockaddr *)&recv_addr, sizeof(recv_addr)) < 0) {
        perror("bind"); close(recv_fd); return;
    }

    printf("  UDP Receiver bound to port 9090\n");

    /* Sender */
    int send_fd = socket(AF_INET, SOCK_DGRAM, 0);
    struct sockaddr_in dest = {
        .sin_family = AF_INET,
        .sin_port = htons(9090)
    };
    inet_pton(AF_INET, "127.0.0.1", &dest.sin_addr);

    const char *messages[] = {"UDP message 1", "UDP message 2", "UDP message 3"};
    for (int i = 0; i < 3; i++) {
        sendto(send_fd, messages[i], strlen(messages[i]), 0,
               (struct sockaddr *)&dest, sizeof(dest));
        printf("  Sent: %s\n", messages[i]);
    }

    char buffer[BUFFER_SIZE];
    for (int i = 0; i < 3; i++) {
        ssize_t n = recvfrom(recv_fd, buffer, sizeof(buffer) - 1, 0, NULL, NULL);
        if (n > 0) {
            buffer[n] = '\0';
            printf("  Received: %s\n", buffer);
        }
    }

    close(send_fd);
    close(recv_fd);
    printf("\n");
}

/* ============================================================
 * Problem 4: Address Resolution
 * ============================================================ */
void resolve_hostname(const char *hostname) {
    struct addrinfo hints, *result, *rp;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;

    int status = getaddrinfo(hostname, NULL, &hints, &result);
    if (status != 0) {
        fprintf(stderr, "  getaddrinfo: %s\n", gai_strerror(status));
        return;
    }

    printf("  Resolving '%s':\n", hostname);
    for (rp = result; rp; rp = rp->ai_next) {
        char addr_str[INET6_ADDRSTRLEN];
        void *addr;

        if (rp->ai_family == AF_INET) {
            addr = &((struct sockaddr_in *)rp->ai_addr)->sin_addr;
        } else {
            addr = &((struct sockaddr_in6 *)rp->ai_addr)->sin6_addr;
        }

        inet_ntop(rp->ai_family, addr, addr_str, sizeof(addr_str));
        printf("    %s (family: %s)\n", addr_str,
               rp->ai_family == AF_INET ? "IPv4" : "IPv6");
    }

    freeaddrinfo(result);
}

void problem4_address_resolution(void) {
    printf("=== Problem 4: Address Resolution ===\n");
    resolve_hostname("localhost");
    resolve_hostname("127.0.0.1");
    resolve_hostname("::1");
    printf("\n");
}

/* ============================================================
 * Problem 5: Socket Options
 * ============================================================ */
void problem5_socket_options(void) {
    printf("=== Problem 5: Socket Options ===\n");

    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) { perror("socket"); return; }

    /* Query current buffer sizes */
    int sndbuf, rcvbuf;
    socklen_t optlen = sizeof(int);

    getsockopt(sock, SOL_SOCKET, SO_SNDBUF, &sndbuf, &optlen);
    getsockopt(sock, SOL_SOCKET, SO_RCVBUF, &rcvbuf, &optlen);
    printf("  Default SO_SNDBUF: %d bytes\n", sndbuf);
    printf("  Default SO_RCVBUF: %d bytes\n", rcvbuf);

    /* Set new buffer sizes */
    int new_sndbuf = 32768;
    int new_rcvbuf = 65536;
    setsockopt(sock, SOL_SOCKET, SO_SNDBUF, &new_sndbuf, sizeof(int));
    setsockopt(sock, SOL_SOCKET, SO_RCVBUF, &new_rcvbuf, sizeof(int));

    getsockopt(sock, SOL_SOCKET, SO_SNDBUF, &sndbuf, &optlen);
    getsockopt(sock, SOL_SOCKET, SO_RCVBUF, &rcvbuf, &optlen);
    printf("  After set - SO_SNDBUF: %d, SO_RCVBUF: %d\n", sndbuf, rcvbuf);

    /* Toggle TCP_NODELAY */
    int nodelay = 1;
    setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, &nodelay, sizeof(int));

    int current_nodelay;
    getsockopt(sock, IPPROTO_TCP, TCP_NODELAY, &current_nodelay, &optlen);
    printf("  TCP_NODELAY: %s\n", current_nodelay ? "enabled" : "disabled");

    nodelay = 0;
    setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, &nodelay, sizeof(int));
    getsockopt(sock, IPPROTO_TCP, TCP_NODELAY, &current_nodelay, &optlen);
    printf("  After disable: TCP_NODELAY: %s\n",
           current_nodelay ? "enabled" : "disabled");

    close(sock);
    printf("\n");
}

/* ============================================================
 * Main
 * ============================================================ */
int main(void) {
    printf("====================================\n");
    printf("  Networking — Solutions\n");
    printf("====================================\n\n");

    problem4_address_resolution();
    problem5_socket_options();
    problem3_udp();
    printf("  (Run echo server and client in separate terminals)\n");
    printf("  (Uncomment problem1 and problem2 to test)\n\n");

    /* Uncomment to run server/client (requires separate terminals) */
    /* problem1_echo_server(); */
    /* problem2_tcp_client(); */

    return 0;
}
