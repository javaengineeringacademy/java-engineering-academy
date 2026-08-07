package com.filereadwrite;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

/**
 * Common file utilities for validation, path operations, and encoding detection.
 */
public class FileUtils {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
        "xlsx", "xls", "csv", "json", "txt", "xml"
    );

    /**
     * Validate file exists and is readable.
     */
    public boolean validateFile(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Validate file exists and is writable.
     */
    public boolean validateFileWritable(File file) {
        return file.exists() && file.isFile() && file.canWrite();
    }

    /**
     * Get file extension without dot.
     */
    public String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return dotIndex > 0 ? name.substring(dotIndex + 1).toLowerCase() : "";
    }

    /**
     * Check if file type is supported.
     */
    public boolean isSupportedFileType(File file) {
        return SUPPORTED_EXTENSIONS.contains(getFileExtension(file));
    }

    /**
     * Get file size in human-readable format.
     */
    public String getReadableFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Detect file encoding using BOM detection.
     */
    public Charset detectEncoding(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            byte[] bom = new byte[3];
            if (is.read(bom) == 3) {
                if (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
                    return Charset.forName("UTF-8");
                }
                if (bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE) {
                    return Charset.forName("UTF-16LE");
                }
                if (bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF) {
                    return Charset.forName("UTF-16BE");
                }
            }
        }
        return Charset.defaultCharset();
    }

    /**
     * Create backup of file before modification.
     */
    public File createBackup(File file) throws IOException {
        Path source = file.toPath();
        String backupName = file.getName() + ".bak";
        Path backup = source.resolveSibling(backupName);
        Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
        return backup.toFile();
    }

    /**
     * Safely delete file with confirmation.
     */
    public boolean safeDelete(File file) {
        if (validateFile(file)) {
            return file.delete();
        }
        return false;
    }

    /**
     * Ensure parent directories exist.
     */
    public void ensureParentDirectory(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            Files.createDirectories(parent.toPath());
        }
    }

    /**
     * Get list of files with specific extension in directory.
     */
    public List<File> findFiles(File directory, String extension) {
        List<File> files = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] fileList = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith("." + extension.toLowerCase())
            );
            if (fileList != null) {
                files.addAll(Arrays.asList(fileList));
            }
        }
        return files;
    }

    /**
     * Calculate total size of files in list.
     */
    public long calculateTotalSize(List<File> files) {
        return files.stream().mapToLong(File::length).sum();
    }

    /**
     * Copy file to destination.
     */
    public void copyFile(File source, File destination) throws IOException {
        ensureParentDirectory(destination);
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
