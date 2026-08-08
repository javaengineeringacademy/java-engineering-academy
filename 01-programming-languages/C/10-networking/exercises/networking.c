/*
 * Exercise: Networking in C
 * Difficulty: ★★★★★ (5/5)
 * Learning Objectives:
 *   - Understand BSD sockets API
 *   - Practice TCP client-server programming
 *   - Learn about UDP communication
 *   - Master address resolution and socket options
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

/* ============================================================
 * Problem 1: TCP Echo Server
 *
 * Write a TCP server that:
 * - Binds to a specified port
 * - Listens for connections
 * - Accepts a client and echoes back any message received
 * - Handles graceful shutdown with "quit" command
 * ============================================================ */
void problem1_echo_server(void) {
    /* TODO: Implement TCP echo server
     * 1. Create socket with socket()
     * 2. Set SO_REUSEADDR with setsockopt()
     * 3. Bind with bind()
     * 4. Listen with listen()
     * 5. Accept loop: accept(), recv(), send() back
     * 6. Close connections on "quit" or disconnect
     */
    printf("TODO: Problem 1 - TCP Echo Server\n\n");
}

/* ============================================================
 * Problem 2: TCP Client
 *
 * Write a TCP client that:
 * - Connects to the echo server
 * - Sends user input line by line
 * - Prints the echoed response
 * - Exits on "quit"
 * ============================================================ */
void problem2_tcp_client(void) {
    /* TODO: Implement TCP client
     * 1. Create socket
     * 2. Set up sockaddr_in for server
     * 3. Connect with connect()
     * 4. Read input, send, receive echo, print
     * 5. Close socket
     */
    printf("TODO: Problem 2 - TCP Client\n\n");
}

/* ============================================================
 * Problem 3: UDP Sender/Receiver
 *
 * Implement a UDP sender that sends datagrams to a receiver.
 * Implement a UDP receiver that prints received datagrams.
 * Use sendto() and recvfrom().
 * ============================================================ */
void problem3_udp(void) {
    /* TODO: Implement UDP sender and receiver
     * Sender:
     * 1. Create UDP socket
     * 2. sendto() messages to receiver address
     * Receiver:
     * 1. Create UDP socket and bind
     * 2. recvfrom() and print messages
     */
    printf("TODO: Problem 3 - UDP Communication\n\n");
}

/* ============================================================
 * Problem 4: Address Resolution
 *
 * Write a function that uses getaddrinfo() to resolve a
 * hostname and print all resolved addresses.
 * Handle both IPv4 and IPv6.
 * ============================================================ */
void resolve_hostname(const char *hostname) {
    /* TODO: Use getaddrinfo() to resolve hostname
     * 1. Set up hints (AF_UNSPEC, SOCK_STREAM)
     * 2. Call getaddrinfo()
     * 3. Walk the results linked list
     * 4. Print each address with inet_ntop()
     * 5. Free results with freeaddrinfo()
     */
    (void)hostname;
}

void problem4_address_resolution(void) {
    /* TODO: Test with "localhost", "google.com", "::1" */
    printf("TODO: Problem 4 - Address Resolution\n\n");
}

/* ============================================================
 * Problem 5: Socket Options
 *
 * Write code that:
 * - Queries and prints current socket buffer sizes
 * - Sets custom send/receive buffer sizes
 * - Enables/disables Nagle's algorithm (TCP_NODELAY)
 * ============================================================ */
void problem5_socket_options(void) {
    /* TODO: Demonstrate socket options
     * 1. Create a TCP socket
     * 2. Get SO_SNDBUF and SO_RCVBUF with getsockopt()
     * 3. Set new buffer sizes with setsockopt()
     * 4. Toggle TCP_NODELAY
     * 5. Print results
     */
    printf("TODO: Problem 5 - Socket Options\n\n");
}

int main(void) {
    printf("====================================\n");
    printf("  Networking — Exercises\n");
    printf("====================================\n\n");

    printf("These exercises require a network-capable system.\n");
    printf("Implement each function and test with multiple terminals.\n\n");

    problem1_echo_server();
    problem2_tcp_client();
    problem3_udp();
    problem4_address_resolution();
    problem5_socket_options();

    return 0;
}
