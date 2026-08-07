# Networking Quiz

## Questions

1. What is the difference between TCP and UDP?
2. What is a socket?
3. What does bind() do?
4. What is the difference between listen() and accept()?
5. What is a port number?
6. What is a socket timeout?
7. What is non-blocking I/O?
8. What is select() used for?
9. What is the difference between send() and write()?
10. How do you handle partial receives?
11. What is the three-way handshake in TCP?
12. What is a TIME_WAIT state?
13. What is the difference between `gethostbyname` and `getaddrinfo`?
14. What is a multicast socket?
15. What is SO_REUSEADDR and when is it needed?

## Answers

1. TCP: reliable, ordered; UDP: fast, unreliable
2. An endpoint for network communication
3. Associates an address with a socket
4. listen(): wait; accept(): establish connection
5. A number identifying a process on a host
6. Limiting how long a socket operation can take
7. Operations that return immediately if not ready
8. Multiplexing multiple sockets in one thread
9. Both send data; send() is socket-specific
10. Loop until all expected bytes received
11. SYN → SYN-ACK → ACK: client and server establish a connection with sequence numbers
12. A state after connection close where the socket waits to ensure delayed packets are discarded; prevents old duplicates from interfering with new connections
13. `gethostbyname` is IPv4-only and not thread-safe; `getaddrinfo` supports IPv4/IPv6, is thread-safe, and returns a linked list of results
14. A socket that can send/receive datagrams to/from a group of hosts simultaneously; uses a multicast group address
15. Allows reuse of a local address/port in TIME_WAIT state; needed for server restarts to bind immediately without waiting
