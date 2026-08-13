package academy.javaengineering.io.internals;

import java.io.*;
import java.nio.file.*;

public class FileOperationsInternals {

    public static void main(String[] args) {
        System.out.println("=== File Operations Internals ===\n");

        // 1. File Class
        System.out.println("--- File Class ---");
        System.out.println("Represents file/directory pathname");
        System.out.println("Platform-independent path handling");
        System.out.println("Methods: exists(), delete(), renameTo()");

        // 2. NIO Path
        System.out.println("\n--- NIO Path ---");
        System.out.println("Path.of() or Paths.get()");
        System.out.println("More powerful than File class");
        System.out.println("Supports symbolic links");

        // 3. Files Utility Class
        System.out.println("\n--- Files Utility ---");
        System.out.println("Files.copy(), Files.move(), Files.delete()");
        System.out.println("Files.readAllBytes(), Files.write()");
        System.out.println("Convenient file operations");
    }
}
