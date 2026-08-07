package com.filereadwrite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Main orchestrator for file processing operations.
 * Detects file types and routes to appropriate processors.
 */
public class FileProcessor {

    private final ExcelReader excelReader;
    private final CsvProcessor csvProcessor;
    private final JsonProcessor jsonProcessor;
    private final FileUtils fileUtils;

    private Map<String, Integer> processingStats;

    public FileProcessor() {
        this.excelReader = new ExcelReader();
        this.csvProcessor = new CsvProcessor();
        this.jsonProcessor = new JsonProcessor();
        this.fileUtils = new FileUtils();
        this.processingStats = new HashMap<>();
    }

    /**
     * Process a file based on its extension.
     * @param filePath Path to the file
     * @return Processing result
     */
    public ProcessingResult processFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!fileUtils.validateFile(file)) {
            throw new IllegalArgumentException("Invalid file: " + filePath);
        }

        String extension = fileUtils.getFileExtension(file);
        ProcessingResult result;

        switch (extension.toLowerCase()) {
            case "xlsx":
            case "xls":
                result = processExcel(file);
                break;
            case "csv":
                result = processCsv(file);
                break;
            case "json":
                result = processJson(file);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported file type: " + extension);
        }

        updateStats(extension, true);
        return result;
    }

    /**
     * Process an Excel file.
     */
    private ProcessingResult processExcel(File file) throws IOException {
        List<Map<String, Object>> data = excelReader.readExcel(file);
        return new ProcessingResult("excel", file.getName(), data.size(), data);
    }

    /**
     * Process a CSV file.
     */
    private ProcessingResult processCsv(File file) throws IOException {
        List<Map<String, String>> data = csvProcessor.readCsv(file.getAbsolutePath());
        List<Map<String, Object>> converted = new ArrayList<>();
        for (Map<String, String> row : data) {
            converted.add(new HashMap<>(row));
        }
        return new ProcessingResult("csv", file.getName(), data.size(), converted);
    }

    /**
     * Process a JSON file.
     */
    private ProcessingResult processJson(File file) throws IOException {
        Object data = jsonProcessor.readJson(file.getAbsolutePath());
        int count = (data instanceof List) ? ((List<?>) data).size() : 1;
        List<Map<String, Object>> wrapped = new ArrayList<>();
        if (data instanceof List) {
            for (Object item : (List<?>) data) {
                if (item instanceof Map) {
                    wrapped.add((Map<String, Object>) item);
                }
            }
        }
        return new ProcessingResult("json", file.getName(), count, wrapped);
    }

    /**
     * Process multiple files in batch.
     */
    public List<ProcessingResult> processBatch(List<String> filePaths) throws IOException {
        List<ProcessingResult> results = new ArrayList<>();
        for (String path : filePaths) {
            try {
                results.add(processFile(path));
            } catch (Exception e) {
                System.err.println("Error processing " + path + ": " + e.getMessage());
            }
        }
        return results;
    }

    private void updateStats(String extension, boolean success) {
        processingStats.merge(extension, 1, Integer::sum);
    }

    public Map<String, Integer> getStats() {
        return new HashMap<>(processingStats);
    }

    /**
     * Inner class representing processing results.
     */
    public static class ProcessingResult {
        private final String fileType;
        private final String fileName;
        private final int recordCount;
        private final Object data;

        public ProcessingResult(String fileType, String fileName, int recordCount, Object data) {
            this.fileType = fileType;
            this.fileName = fileName;
            this.recordCount = recordCount;
            this.data = data;
        }

        public String getFileType() { return fileType; }
        public String getFileName() { return fileName; }
        public int getRecordCount() { return recordCount; }
        public Object getData() { return data; }

        @Override
        public String toString() {
            return String.format("Result{type=%s, file=%s, records=%d}", fileType, fileName, recordCount);
        }
    }
}
