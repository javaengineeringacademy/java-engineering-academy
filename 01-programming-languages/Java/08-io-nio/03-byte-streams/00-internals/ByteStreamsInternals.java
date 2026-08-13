package academy.javaengineering.io.internals;

import java.io.*;

public class ByteStreamsInternals {

    public static void main(String[] args) {
        System.out.println("=== Byte Streams Internals ===\n");

        // 1. InputStream
        System.out.println("--- InputStream ---");
        System.out.println(" abstract class for byte input");
        System.out.println("Methods: read(), available(), close()");
        System.out.println("Subclasses: FileInputStream, BufferedInputStream");

        // 2. OutputStream
        System.out.println("\n--- OutputStream ---");
        System.out.println(" abstract class for byte output");
        System.out.println("Methods: write(), flush(), close()");
        System.out.println("Subclasses: FileOutputStream, BufferedOutputStream");

        // 3. Byte Processing
        System.out.println("\n--- Byte Processing ---");
        System.out.println("Bytes: raw binary data");
        System.out.println("Use for: images, audio, serialized objects");
        System.out.println("Not suitable for text (encoding issues)");
    }
}
