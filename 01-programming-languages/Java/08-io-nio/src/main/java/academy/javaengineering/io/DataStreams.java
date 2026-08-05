package academy.javaengineering.io;

import java.io.*;
import java.util.*;

/**
 * Data Streams in Java IO - Demonstrates primitive type I/O operations.
 *
 * <p>This class provides comprehensive examples of DataInputStream,
 * DataOutputStream, and RandomAccessFile for reading and writing
 * primitive data types in binary format.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class DataStreams {

    private DataStreams() {
        // Utility class
    }

    // ==================== Data Stream Operations ====================

    /**
     * Writes primitive data types to a file.
     *
     * @param path the file path
     * @throws IOException if write fails
     */
    public static void writePrimitives(String path) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(path)))) {
            dos.writeInt(42);
            dos.writeLong(123456789L);
            dos.writeFloat(3.14f);
            dos.writeDouble(3.141592653589793);
            dos.writeBoolean(true);
            dos.writeByte(0x7F);
            dos.writeShort(1024);
            dos.writeChar('A');
            dos.writeUTF("Hello, Data Streams!");
        }
    }

    /**
     * Reads primitive data types from a file.
     *
     * @param path the file path
     * @throws IOException if read fails
     */
    public static void readPrimitives(String path) throws IOException {
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream(path)))) {
            System.out.printf("int: %d%n", dis.readInt());
            System.out.printf("long: %d%n", dis.readLong());
            System.out.printf("float: %.2f%n", dis.readFloat());
            System.out.printf("double: %.15f%n", dis.readDouble());
            System.out.printf("boolean: %b%n", dis.readBoolean());
            System.out.printf("byte: 0x%02X%n", dis.readByte());
            System.out.printf("short: %d%n", dis.readShort());
            System.out.printf("char: %c%n", dis.readChar());
            System.out.printf("String: %s%n", dis.readUTF());
        }
    }

    // ==================== RandomAccessFile Operations ====================

    /**
     * Demonstrates RandomAccessFile operations.
     *
     * @param path the file path
     * @throws IOException if operation fails
     */
    public static void randomAccessDemo(String path) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            // Write data at different positions
            raf.seek(0);
            raf.writeInt(100);

            raf.seek(10);
            raf.writeDouble(3.14);

            raf.seek(20);
            raf.writeUTF("Hello");

            // Read data back
            raf.seek(0);
            System.out.println("Position 0: " + raf.readInt());

            raf.seek(10);
            System.out.println("Position 10: " + raf.readDouble());

            raf.seek(20);
            System.out.println("Position 20: " + raf.readUTF());

            System.out.println("File pointer: " + raf.getFilePointer());
            System.out.println("File length: " + raf.length());
        }
    }

    // ==================== Structured Records ====================

    /**
     * Writes a student record.
     *
     * @param dos the data output stream
     * @param id student ID
     * @param name student name
     * @param gpa student GPA
     * @throws IOException if write fails
     */
    public static void writeStudentRecord(DataOutputStream dos,
            int id, String name, double gpa) throws IOException {
        dos.writeInt(id);
        dos.writeUTF(name);
        dos.writeDouble(gpa);
        dos.writeBoolean(gpa >= 3.0); // honors flag
    }

    /**
     * Reads a student record.
     *
     * @param dis the data input stream
     * @return array containing [id, name, gpa, honors]
     * @throws IOException if read fails
     */
    public static Object[] readStudentRecord(DataInputStream dis)
            throws IOException {
        int id = dis.readInt();
        String name = dis.readUTF();
        double gpa = dis.readDouble();
        boolean honors = dis.readBoolean();
        return new Object[]{id, name, gpa, honors};
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates data stream operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Data Streams Demo ===");

        String tempDir = System.getProperty("java.io.tmpdir");
        String primitivesFile = tempDir +
            File.separator + "primitives.bin";
        String randomFile = tempDir +
            File.separator + "random.dat";
        String studentsFile = tempDir +
            File.separator + "students.dat";

        try {
            // Write and read primitives
            System.out.println("Primitive types:");
            writePrimitives(primitivesFile);
            readPrimitives(primitivesFile);

            // Random access
            System.out.println("\nRandom access:");
            randomAccessDemo(randomFile);

            // Student records
            System.out.println("\nStudent records:");
            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream(studentsFile)))) {
                writeStudentRecord(dos, 1, "Alice", 3.8);
                writeStudentRecord(dos, 2, "Bob", 2.5);
                writeStudentRecord(dos, 3, "Charlie", 3.9);
            }

            try (DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(
                        new FileInputStream(studentsFile)))) {
                while (dis.available() > 0) {
                    Object[] record = readStudentRecord(dis);
                    System.out.printf("  ID: %d, Name: %-10s, " +
                        "GPA: %.1f, Honors: %s%n",
                        record[0], record[1], record[2], record[3]);
                }
            }

            // Cleanup
            new File(primitivesFile).delete();
            new File(randomFile).delete();
            new File(studentsFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
