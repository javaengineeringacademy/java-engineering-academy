package academy.javaengineering.io.internals;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class IntroductionInternals {

    public static void main(String[] args) {
        System.out.println("=== I/O & NIO Introduction Internals ===\n");

        // 1. I/O Concept
        System.out.println("--- I/O Concept ---");
        System.out.println("I/O = Input/Output");
        System.out.println("Reading data from sources");
        System.out.println("Writing data to destinations");

        // 2. Stream-based I/O (Traditional)
        System.out.println("\n--- Stream-based I/O ---");
        System.out.println("Byte streams: InputStream, OutputStream");
        System.out.println("Character streams: Reader, Writer");
        System.out.println("Data flows sequentially");

        // 3. NIO Concept
        System.out.println("\n--- NIO (New I/O) ---");
        System.out.println("Buffer-oriented: data stored in buffers");
        System.out.println("Channel-based: bidirectional data transfer");
        System.out.println("Non-blocking I/O support");

        // 4. Key Differences
        System.out.println("\n--- I/O vs NIO ---");
        System.out.println("I/O: Stream-based, blocking");
        System.out.println("NIO: Buffer-based, non-blocking");
        System.out.println("NIO: Better for large files");
    }
}
