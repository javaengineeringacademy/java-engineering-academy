package academy.javaengineering.linux;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * Demonstrates Linux file operations from Java.
 */
public class LinuxFileOperations {

    public void createFileWithPermissions(String path, String content, 
                                          String permissions) throws IOException {
        Path filePath = Path.of(path);
        Files.writeString(filePath, content);
        
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(permissions);
        Files.setPosixFilePermissions(filePath, perms);
    }

    public String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    public void changePermissions(String path, String permissions) throws IOException {
        Path filePath = Path.of(path);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(permissions);
        Files.setPosixFilePermissions(filePath, perms);
    }

    public boolean checkFileExists(String path) {
        return Files.exists(Path.of(path));
    }

    public long getFileSize(String path) throws IOException {
        return Files.size(Path.of(path));
    }
}
