# Chat Application

A socket-based chat application demonstrating network programming, threading, and protocol design in Python.

## Features

- Real-time multi-client chat
- User registration and nicknames
- Private messaging
- Broadcast messaging
- Connection status indicators
- Custom message protocol

## Architecture

```
chat-app/
├── server.py    # Socket server with client management
├── client.py    # Socket client with UI
├── protocol.py  # Message protocol definition
├── main.py      # Entry point for server/client
└── README.md    # This file
```

## Learning Objectives

- Socket programming fundamentals
- Multi-threading for concurrent connections
- Protocol design and message parsing
- Network error handling
- Client-server architecture

## How to Run

```bash
# Start the server
python main.py server --port 8080

# Start a client (in another terminal)
python main.py client --host localhost --port 8080 --name "Username"
```
