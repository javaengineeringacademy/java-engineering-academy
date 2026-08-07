"""Message protocol for chat application."""

import json
from typing import Dict, Optional
from datetime import datetime
from enum import Enum


class MessageType(Enum):
    """Message type constants."""
    CONNECT = "CONNECT"
    DISCONNECT = "DISCONNECT"
    MESSAGE = "MESSAGE"
    PRIVATE = "PRIVATE"
    USER_LIST = "USER_LIST"
    ERROR = "ERROR"
    SYSTEM = "SYSTEM"
    PING = "PING"
    PONG = "PONG"


class Message:
    """Represents a chat message."""
    
    def __init__(
        self,
        msg_type: MessageType,
        sender: str = "",
        content: str = "",
        recipient: str = "",
        metadata: Optional[Dict] = None
    ):
        """Initialize message."""
        self.type = msg_type
        self.sender = sender
        self.content = content
        self.recipient = recipient
        self.timestamp = datetime.now().isoformat()
        self.metadata = metadata or {}
    
    def to_json(self) -> str:
        """Serialize message to JSON string."""
        data = {
            "type": self.type.value,
            "sender": self.sender,
            "content": self.content,
            "recipient": self.recipient,
            "timestamp": self.timestamp,
            "metadata": self.metadata
        }
        return json.dumps(data)
    
    @classmethod
    def from_json(cls, json_str: str) -> "Message":
        """Deserialize message from JSON string."""
        try:
            data = json.loads(json_str)
            return cls(
                msg_type=MessageType(data["type"]),
                sender=data.get("sender", ""),
                content=data.get("content", ""),
                recipient=data.get("recipient", ""),
                metadata=data.get("metadata", {})
            )
        except (json.JSONDecodeError, KeyError, ValueError) as e:
            return cls(
                msg_type=MessageType.ERROR,
                content=f"Invalid message: {e}"
            )
    
    @staticmethod
    def create_connect(username: str) -> "Message":
        """Create a connection message."""
        return Message(
            msg_type=MessageType.CONNECT,
            sender=username,
            content=f"{username} has joined the chat"
        )
    
    @staticmethod
    def create_disconnect(username: str) -> "Message":
        """Create a disconnection message."""
        return Message(
            msg_type=MessageType.DISCONNECT,
            sender=username,
            content=f"{username} has left the chat"
        )
    
    @staticmethod
    def create_message(sender: str, content: str) -> "Message":
        """Create a chat message."""
        return Message(
            msg_type=MessageType.MESSAGE,
            sender=sender,
            content=content
        )
    
    @staticmethod
    def create_private(sender: str, recipient: str, content: str) -> "Message":
        """Create a private message."""
        return Message(
            msg_type=MessageType.PRIVATE,
            sender=sender,
            recipient=recipient,
            content=content
        )
    
    @staticmethod
    def create_system(content: str) -> "Message":
        """Create a system message."""
        return Message(
            msg_type=MessageType.SYSTEM,
            content=content
        )
    
    @staticmethod
    def create_user_list(users: list) -> "Message":
        """Create a user list message."""
        return Message(
            msg_type=MessageType.USER_LIST,
            content=json.dumps(users),
            metadata={"users": users}
        )
    
    @staticmethod
    def create_error(content: str) -> "Message":
        """Create an error message."""
        return Message(
            msg_type=MessageType.ERROR,
            content=content
        )
    
    def __str__(self) -> str:
        """String representation."""
        if self.type == MessageType.MESSAGE:
            return f"[{self.sender}]: {self.content}"
        elif self.type == MessageType.PRIVATE:
            return f"[PM {self.sender} -> {self.recipient}]: {self.content}"
        elif self.type in (MessageType.CONNECT, MessageType.DISCONNECT, MessageType.SYSTEM):
            return f"*** {self.content} ***"
        return f"[{self.type.value}]: {self.content}"
