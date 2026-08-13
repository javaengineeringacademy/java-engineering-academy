package academy.javaengineering.io.internals;

import java.io.*;

public class CharacterStreamsInternals {

    public static void main(String[] args) {
        System.out.println("=== Character Streams Internals ===\n");

        // 1. Reader
        System.out.println("--- Reader ---");
        System.out.println(" abstract class for character input");
        System.out.println("Methods: read(), close()");
        System.out.println("Subclasses: FileReader, BufferedReader");

        // 2. Writer
        System.out.println("\n--- Writer ---");
        System.out.println(" abstract class for character output");
        System.out.println("Methods: write(), append(), flush(), close()");
        System.out.println("Subclasses: FileWriter, BufferedWriter");

        // 3. Character Encoding
        System.out.println("\n--- Character Encoding ---");
        System.out.println("Characters mapped to bytes via encoding");
        System.out.println("UTF-8: variable-length encoding");
        System.out.println("Platform default encoding may vary");
    }
}
