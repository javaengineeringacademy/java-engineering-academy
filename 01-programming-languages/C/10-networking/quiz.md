# Networking Quiz

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
