package academy.javaengineering.io.internals;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

public class NioChannelsInternals {

    public static void main(String[] args) {
        System.out.println("=== NIO Channels Internals ===\n");

        // 1. Channel Concept
        System.out.println("--- Channel Concept ---");
        System.out.println("Bidirectional data transfer");
        System.out.println("Connection to I/O source/destination");
        System.out.println("Works with buffers");

        // 2. Channel Types
        System.out.println("\n--- Channel Types ---");
        System.out.println("FileChannel: file I/O");
        System.out.println("SocketChannel: TCP client");
        System.out.println("ServerSocketChannel: TCP server");
        System.out.println("DatagramChannel: UDP");

        // 3. Non-blocking I/O
        System.out.println("\n--- Non-blocking I/O ---");
        System.out.println("Selector: monitor multiple channels");
        System.out.println("Thread can handle multiple connections");
        System.out.println("Better scalability for networking");
    }
}
