"""Socket server for chat application."""

import socket
import threading
from typing import Dict, List
from protocol import Message, MessageType


class ChatServer:
    """Multi-threaded chat server."""
    
    def __init__(self, host: str = "localhost", port: int = 8080):
        """Initialize server with host and port."""
        self.host = host
        self.port = port
        self.server_socket = None
        self.clients: Dict[socket.socket, str] = {}
        self.running = False
        self.lock = threading.Lock()
    
    def start(self) -> None:
        """Start the chat server."""
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen(5)
        self.running = True
        
        print(f"Server started on {self.host}:{self.port}")
        print("Waiting for connections...")
        
        while self.running:
            try:
                client_socket, address = self.server_socket.accept()
                print(f"New connection from {address}")
                
                thread = threading.Thread(
                    target=self.handle_client,
                    args=(client_socket, address),
                    daemon=True
                )
                thread.start()
                
            except OSError:
                break
    
    def handle_client(self, client_socket: socket.socket, address: tuple) -> None:
        """Handle individual client connection."""
        username = None
        
        try:
            # Wait for connect message
            data = client_socket.recv(4096).decode("utf-8")
            if not data:
                return
            
            msg = Message.from_json(data)
            if msg.type != MessageType.CONNECT:
                client_socket.close()
                return
            
            username = msg.sender
            
            # Register client
            with self.lock:
                self.clients[client_socket] = username
            
            # Broadcast connection
            self.broadcast(Message.create_connect(username), exclude=None)
            print(f"{username} connected from {address}")
            
            # Send user list
            self.send_user_list()
            
            # Handle messages
            self.client_loop(client_socket, username)
            
        except (ConnectionResetError, BrokenPipeError):
            pass
        except Exception as e:
            print(f"Error handling client {address}: {e}")
        finally:
            # Cleanup
            if client_socket in self.clients:
                with self.lock:
                    del self.clients[client_socket]
            
            client_socket.close()
            
            if username:
                self.broadcast(Message.create_disconnect(username), exclude=None)
                self.send_user_list()
                print(f"{username} disconnected")
    
    def client_loop(self, client_socket: socket.socket, username: str) -> None:
        """Process messages from a client."""
        while self.running:
            try:
                data = client_socket.recv(4096).decode("utf-8")
                if not data:
                    break
                
                msg = Message.from_json(data)
                
                if msg.type == MessageType.MESSAGE:
                    self.broadcast(msg, exclude=None)
                    print(f"[{username}]: {msg.content}")
                
                elif msg.type == MessageType.PRIVATE:
                    self.send_private(msg)
                
                elif msg.type == MessageType.PING:
                    self.send_to_client(client_socket, Message(
                        msg_type=MessageType.PONG,
                        content="pong"
                    ))
                
                elif msg.type == MessageType.DISCONNECT:
                    break
                    
            except (ConnectionResetError, BrokenPipeError):
                break
    
    def broadcast(self, message: Message, exclude: socket.socket = None) -> None:
        """Send message to all connected clients."""
        with self.lock:
            for client_socket in self.clients:
                if client_socket != exclude:
                    try:
                        client_socket.send(message.to_json().encode("utf-8"))
                    except (BrokenPipeError, OSError):
                        pass
    
    def send_private(self, message: Message) -> None:
        """Send private message to specific user."""
        with self.lock:
            for client_socket, username in self.clients.items():
                if username == message.recipient:
                    try:
                        client_socket.send(message.to_json().encode("utf-8"))
                    except (BrokenPipeError, OSError):
                        pass
                    return
        
        # Send error if user not found
        self.send_to_client_by_name(message.sender, Message.create_error(
            f"User '{message.recipient}' not found"
        ))
    
    def send_to_client(self, client_socket: socket.socket, message: Message) -> None:
        """Send message to specific client socket."""
        try:
            client_socket.send(message.to_json().encode("utf-8"))
        except (BrokenPipeError, OSError):
            pass
    
    def send_to_client_by_name(self, username: str, message: Message) -> None:
        """Send message to client by username."""
        with self.lock:
            for client_socket, name in self.clients.items():
                if name == username:
                    self.send_to_client(client_socket, message)
                    return
    
    def send_user_list(self) -> None:
        """Send updated user list to all clients."""
        with self.lock:
            users = list(self.clients.values())
        
        self.broadcast(Message.create_user_list(users), exclude=None)
    
    def stop(self) -> None:
        """Stop the server."""
        self.running = False
        
        # Close all client connections
        with self.lock:
            for client_socket in list(self.clients.keys()):
                try:
                    client_socket.close()
                except OSError:
                    pass
            self.clients.clear()
        
        if self.server_socket:
            self.server_socket.close()
        
        print("Server stopped")
