import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * CharsetDemo.java
 *
 * Demonstrates charset encoding and decoding operations.
 */
public class CharsetDemo {

    public static void main(String[] args) {
        System.out.println("=== Charset and Encoding Demo ===\n");

        // 1. Standard charsets
        standardCharsets();

        // 2. Charset encoding/decoding
        encodingDecoding();

        // 3. ByteBuffer and CharBuffer
        buffers();

        // 4. Reading files with specific charset
        fileCharsetExample();

        // 5. Custom charset operations
        customCharsetOperations();
    }

    private static void standardCharsets() {
        System.out.println("--- 1. Standard Charsets ---");

        // StandardCharsets constants (preferred)
        System.out.println("UTF-8: " + StandardCharsets.UTF_8);
        System.out.println("UTF-16: " + StandardCharsets.UTF_16);
        System.out.println("UTF-16BE: " + StandardCharsets.UTF_16BE);
        System.out.println("UTF-16LE: " + StandardCharsets.UTF_16LE);
        System.out.println("US-ASCII: " + StandardCharsets.US_ASCII);
        System.out.println("ISO-8859-1: " + StandardCharsets.ISO_8859_1);

        // Charset.forName (alternative)
        Charset utf8 = Charset.forName("UTF-8");
        System.out.println("UTF-8 by name: " + utf8);
        System.out.println("Display name: " + utf8.displayName());
        System.out.println("Canonical name: " + utf8.name());

        // Available charsets
        System.out.println("\nAvailable charsets:");
        Charset.availableCharsets().keySet().stream()
            .sorted()
            .limit(10)
            .forEach(name -> System.out.println("  " + name));

        System.out.println();
    }

    private static void encodingDecoding() {
        System.out.println("--- 2. Encoding/Decoding ---");

        String text = "Hello, World! 你好世界";

        // Encode string to bytes
        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] utf16Bytes = text.getBytes(StandardCharsets.UTF_16);
        byte[] asciiBytes = text.getBytes(StandardCharsets.US_ASCII);

        System.out.println("Original: " + text);
        System.out.println("UTF-8 bytes length: " + utf8Bytes.length);
        System.out.println("UTF-16 bytes length: " + utf16Bytes.length);
        System.out.println("ASCII bytes length: " + asciiBytes.length);

        // Decode bytes to string
        String utf8Decoded = new String(utf8Bytes, StandardCharsets.UTF_8);
        String utf16Decoded = new String(utf16Bytes, StandardCharsets.UTF_16);
        String asciiDecoded = new String(asciiBytes, StandardCharsets.US_ASCII);

        System.out.println("\nDecoded from UTF-8: " + utf8Decoded);
        System.out.println("Decoded from UTF-16: " + utf16Decoded);
        System.out.println("Decoded from ASCII: " + asciiDecoded);

        // Using Charset.encode and Charset.decode
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
        System.out.println("\nCharset.encode/decode: " + charBuffer.toString());

        System.out.println();
    }

    private static void buffers() {
        System.out.println("--- 3. ByteBuffer and CharBuffer ---");

        // CharBuffer
        CharBuffer charBuffer = CharBuffer.allocate(100);
        charBuffer.put("Hello");
        charBuffer.flip();
        System.out.println("CharBuffer: " + charBuffer);
        System.out.println("Position: " + charBuffer.position());
        System.out.println("Limit: " + charBuffer.limit());

        // ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.put("Hello".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        System.out.println("\nByteBuffer: " + byteBuffer);

        // Converting between buffers
        String text = "Test String";
        ByteBuffer encoded = StandardCharsets.UTF_8.encode(text);
        CharBuffer decoded = StandardCharsets.UTF_8.decode(encoded);

        System.out.println("\nEncoded position: " + encoded.position());
        System.out.println("Decoded: " + decoded.toString());

        System.out.println();
    }

    private static void fileCharsetExample() {
        System.out.println("--- 4. File Charset Example ---");

        // Reading a file with specific charset (pseudo-code)
        // In real code, you would use:
        // try (BufferedReader reader = new BufferedReader(
        //     new InputStreamReader(
        //         new FileInputStream("file.txt"),
        //         StandardCharsets.UTF_8))) {
        //     String line;
        //     while ((line = reader.readLine()) != null) {
        //         System.out.println(line);
        //     }
        // }

        System.out.println("File reading with charset example:");
        System.out.println("  Use InputStreamReader with specific charset");
        System.out.println("  Example: new InputStreamReader(stream, StandardCharsets.UTF_8)");

        System.out.println();
    }

    private static void customCharsetOperations() {
        System.out.println("--- 5. Custom Charset Operations ---");

        // Compare charsets
        Charset utf8 = StandardCharsets.UTF_8;
        Charset utf16 = StandardCharsets.UTF_16;

        System.out.println("UTF-8 == UTF-8: " + utf8.equals(utf8));
        System.out.println("UTF-8 == UTF-16: " + utf8.equals(utf16));

        // Check if charset is registered
        try {
            Charset charset = Charset.forName("UTF-8");
            System.out.println("UTF-8 registered: " + charset.isRegistered());
        } catch (UnsupportedCharsetException e) {
            System.out.println("Charset not found: " + e.getMessage());
        }

        // Encoding with replacement
        String text = "Hello";
        byte[] bytes = text.getBytes(StandardCharsets.US_ASCII);
        // Characters not in ASCII will be replaced with '?'

        System.out.println("\nEncoding with replacement:");
        System.out.println("Original: " + text);
        System.out.println("ASCII bytes: " + java.util.Arrays.toString(bytes));

        // CanEncode check
        System.out.println("\nCan encode 'A' in US-ASCII: " +
            StandardCharsets.US_ASCII.canEncode());
        System.out.println("Can encode '你' in US-ASCII: " +
            StandardCharsets.US_ASCII.canEncode());
        System.out.println("Can encode '你' in UTF-8: " +
            StandardCharsets.UTF_8.canEncode());

        System.out.println();
    }
}
