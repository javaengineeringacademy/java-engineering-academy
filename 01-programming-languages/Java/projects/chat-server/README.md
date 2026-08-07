# Chat Server Project

## Overview
A multi-threaded TCP chat server that supports multiple clients, chat rooms, and private messaging. This project demonstrates networking, concurrency, and design patterns in Java.

## Features
- Multiple concurrent clients
- Chat rooms (create, join, leave)
- Private messaging
- User authentication
- Message history
- Broadcasting

## Architecture
- ChatServer: Accepts connections, manages clients
- ClientHandler: Handles individual client communication
- Message: Represents chat messages with metadata
- ChatRoom: Manages room state and participants

## Learning Objectives
- TCP socket programming
- Thread management
- Producer-consumer pattern
- Design patterns (Observer, Factory)

## How to Run
```bash
javac src/*.java
java -cp src ChatServer
```

## Production Notes
- In production, use NIO for better scalability
- Add SSL/TLS for secure communication
- Use a message queue for persistence
