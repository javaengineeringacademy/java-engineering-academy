"""Socket client for chat application."""

import socket
import threading
import sys
from protocol import Message, MessageType


class ChatClient:
    """Chat client with socket connection."""
    
    def __init__(self, host: str = "localhost", port: int = 8080, username: str = ""):
        """Initialize client."""
        self.host = host
        self.port = port
        self.username = username
        self.socket = None
        self.running = False
        self.connected = False
    
    def connect(self) -> bool:
        """Connect to chat server."""
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.connect((self.host, self.port))
            
            # Send connect message
            connect_msg = Message.create_connect(self.username)
            self.socket.send(connect_msg.to_json().encode("utf-8"))
            
            self.running = True
            self.connected = True
            
            # Start receive thread
            receive_thread = threading.Thread(
                target=self.receive_loop,
                daemon=True
            )
            receive_thread.start()
            
            return True
            
        except ConnectionRefusedError:
            print(f"Connection refused: {self.host}:{self.port}")
            return False
        except Exception as e:
            print(f"Connection error: {e}")
            return False
    
    def receive_loop(self) -> None:
        """Continuously receive messages from server."""
        while self.running:
            try:
                data = self.socket.recv(4096).decode("utf-8")
                if not data:
                    break
                
                msg = Message.from_json(data)
                self.handle_message(msg)
                
            except (ConnectionResetError, BrokenPipeError):
                break
            except Exception as e:
                if self.running:
                    print(f"Receive error: {e}")
                break
        
        self.connected = False
        if self.running:
            print("\nDisconnected from server")
    
    def handle_message(self, message: Message) -> None:
        """Handle received message."""
        if message.type == MessageType.USER_LIST:
            users = message.metadata.get("users", [])
            print(f"\n--- Online Users ({len(users)}): {', '.join(users)} ---")
        
        elif message.type == MessageType.ERROR:
            print(f"\n[ERROR]: {message.content}")
        
        elif message.type == MessageType.MESSAGE:
            if message.sender != self.username:
                print(f"\n{message}")
        
        elif message.type == MessageType.PRIVATE:
            print(f"\n{message}")
        
        else:
            print(f"\n{message}")
    
    def send_message(self, content: str) -> None:
        """Send a message to the chat."""
        if not self.connected:
            print("Not connected to server")
            return
        
        msg = Message.create_message(self.username, content)
        try:
            self.socket.send(msg.to_json().encode("utf-8"))
        except (BrokenPipeError, OSError):
            print("Failed to send message")
    
    def send_private(self, recipient: str, content: str) -> None:
        """Send a private message."""
        if not self.connected:
            print("Not connected to server")
            return
        
        msg = Message.create_private(self.username, recipient, content)
        try:
            self.socket.send(msg.to_json().encode("utf-8"))
        except (BrokenPipeError, OSError):
            print("Failed to send private message")
    
    def disconnect(self) -> None:
        """Disconnect from server."""
        self.running = False
        
        if self.socket:
            try:
                disconnect_msg = Message.create_disconnect(self.username)
                self.socket.send(disconnect_msg.to_json().encode("utf-8"))
            except (BrokenPipeError, OSError):
                pass
            
            self.socket.close()
        
        self.connected = False
        print("Disconnected")
    
    def input_loop(self) -> None:
        """Handle user input."""
        print("\nCommands:")
        print("  /quit     - Exit chat")
        print("  /pm <user> <message> - Send private message")
        print("  /users    - Show online users")
        print("  Just type to send a message\n")
        
        while self.running and self.connected:
            try:
                user_input = input("").strip()
                
                if not user_input:
                    continue
                
                if user_input.lower() == "/quit":
                    self.disconnect()
                    break
                
                elif user_input.lower() == "/users":
                    print("Requesting user list...")
                    # Server will send user list automatically
                
                elif user_input.lower().startswith("/pm "):
                    parts = user_input[4:].split(" ", 1)
                    if len(parts) == 2:
                        recipient, content = parts
                        self.send_private(recipient, content)
                    else:
                        print("Usage: /pm <username> <message>")
                
                else:
                    self.send_message(user_input)
                    
            except KeyboardInterrupt:
                self.disconnect()
                break
