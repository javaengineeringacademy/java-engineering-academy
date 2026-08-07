# Python Socket Reference

## What is socket?

The socket module provides access to the BSD socket interface. It's used for network communication between processes, both locally and over the network.

## Why does socket matter?

Understanding socket helps you:
- Build network applications
- Implement client-server architecture
- Create custom protocols
- Handle low-level network communication

---

## 1. Basic TCP Client

```python
import socket

# Create socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to server
client_socket.connect(('localhost', 8080))

# Send data
client_socket.send(b'Hello, Server!')

# Receive data
data = client_socket.recv(1024)
print(data.decode())

# Close connection
client_socket.close()
```

---

## 2. Basic TCP Server

```python
import socket

# Create socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind to address
server_socket.bind(('localhost', 8080))

# Listen for connections
server_socket.listen(5)

while True:
    # Accept connection
    client_socket, address = server_socket.accept()
    print(f"Connection from {address}")
    
    # Receive data
    data = client_socket.recv(1024)
    print(f"Received: {data.decode()}")
    
    # Send response
    client_socket.send(b'Hello, Client!')
    
    # Close connection
    client_socket.close()
```

---

## 3. UDP Client

```python
import socket

# Create socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Send data
client_socket.sendto(b'Hello, Server!', ('localhost', 8080))

# Receive data
data, address = client_socket.recvfrom(1024)
print(f"From {address}: {data.decode()}")

# Close socket
client_socket.close()
```

---

## 4. UDP Server

```python
import socket

# Create socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Bind to address
server_socket.bind(('localhost', 8080))

while True:
    # Receive data
    data, address = server_socket.recvfrom(1024)
    print(f"From {address}: {data.decode()}")
    
    # Send response
    server_socket.sendto(b'Hello, Client!', address)
```

---

## 5. Non-blocking Sockets

```python
import socket

# Create socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Set non-blocking
client_socket.setblocking(False)

# Connect (may raise BlockingIOError)
try:
    client_socket.connect(('localhost', 8080))
except BlockingIOError:
    pass

# Send/receive may also raise BlockingIOError
```

---

## 6. Socket Options

```python
import socket

# Create socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Set socket options
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

# Bind and listen
server_socket.bind(('localhost', 8080))
server_socket.listen(5)
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **socket** | Create socket | `socket.socket(AF_INET, SOCK_STREAM)` |
| **bind** | Bind to address | `sock.bind(('host', port))` |
| **listen** | Listen for connections | `sock.listen(backlog)` |
| **accept** | Accept connection | `sock.accept()` |
| **connect** | Connect to server | `sock.connect(('host', port))` |
| **send** | Send data | `sock.send(data)` |
| **recv** | Receive data | `sock.recv(bufsize)` |
| **sendto** | Send UDP data | `sock.sendto(data, address)` |
| **recvfrom** | Receive UDP data | `sock.recvfrom(bufsize)` |
| **close** | Close socket | `sock.close()` |

---

## Common Mistakes

### 1. Not Closing Sockets

```python
# WRONG
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('localhost', 8080))
client_socket.send(b'Hello')
# Socket not closed

# RIGHT
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    client_socket.connect(('localhost', 8080))
    client_socket.send(b'Hello')
finally:
    client_socket.close()
```

### 2. Not Handling Errors

```python
# WRONG
client_socket.connect(('localhost', 8080))

# RIGHT
try:
    client_socket.connect(('localhost', 8080))
except ConnectionRefusedError:
    print("Connection refused")
except socket.gaierror:
    print("Invalid address")
```

### 3. Not Using SO_REUSEADDR

```python
# WRONG (may get "Address already in use")
server_socket.bind(('localhost', 8080))

# RIGHT
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_socket.bind(('localhost', 8080))
```

---

## Production Notes

1. **Use context managers** - `with socket.socket() as s:`
2. **Handle errors properly** - Connection refused, timeout, etc.
3. **Use SO_REUSEADDR** - For server sockets
4. **Close sockets properly** - Use finally or context manager
5. **Use select/poll for multiple connections** - Don't block
6. **Use socket timeout** - Prevent hanging
7. **Use SSL/TLS** - For secure communication
8. **Consider asyncio** - For high-concurrency applications
9. **Use struct for binary data** - Pack/unpack data
10. **Document your protocol** - Communication format

---

## Further Reading

- Python documentation on socket module
- socket programming HOWTO
- Python documentation on ssl module
