"""Entry point for chat application server and client."""

import argparse
import threading
import sys
from server import ChatServer
from client import ChatClient


def run_server(host: str, port: int) -> None:
    """Run the chat server."""
    server = ChatServer(host, port)
    
    try:
        # Start server in a thread so we can handle shutdown
        server_thread = threading.Thread(target=server.start, daemon=True)
        server_thread.start()
        
        print("Press Ctrl+C to stop the server")
        while True:
            input()
            
    except KeyboardInterrupt:
        print("\nShutting down server...")
        server.stop()


def run_client(host: str, port: int, username: str) -> None:
    """Run the chat client."""
    client = ChatClient(host, port, username)
    
    if not client.connect():
        print("Failed to connect to server")
        sys.exit(1)
    
    try:
        client.input_loop()
    except KeyboardInterrupt:
        pass
    finally:
        client.disconnect()


def parse_arguments():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(
        description="Chat Application - Socket-based messaging"
    )
    
    subparsers = parser.add_subparsers(dest="command", help="Command to run")
    
    # Server command
    server_parser = subparsers.add_parser("server", help="Run chat server")
    server_parser.add_argument(
        "--host", default="localhost",
        help="Host address (default: localhost)"
    )
    server_parser.add_argument(
        "--port", type=int, default=8080,
        help="Port number (default: 8080)"
    )
    
    # Client command
    client_parser = subparsers.add_parser("client", help="Run chat client")
    client_parser.add_argument(
        "--host", default="localhost",
        help="Server host (default: localhost)"
    )
    client_parser.add_argument(
        "--port", type=int, default=8080,
        help="Server port (default: 8080)"
    )
    client_parser.add_argument(
        "--name", required=True,
        help="Username for chat"
    )
    
    return parser.parse_args()


def main():
    """Main entry point."""
    args = parse_arguments()
    
    print("=" * 50)
    print("       CHAT APPLICATION")
    print("=" * 50)
    
    if args.command == "server":
        print(f"Starting server on {args.host}:{args.port}")
        run_server(args.host, args.port)
    
    elif args.command == "client":
        print(f"Connecting to {args.host}:{args.port} as {args.name}")
        run_client(args.host, args.port, args.name)
    
    else:
        print("Please specify a command: server or client")
        print("Example:")
        print("  python main.py server --port 8080")
        print("  python main.py client --host localhost --port 8080 --name Alice")


if __name__ == "__main__":
    main()
