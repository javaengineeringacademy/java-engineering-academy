package com.filereadwrite;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * CSV file processor with header detection and type inference.
 * Handles quoted fields and various delimiters.
 */
public class CsvProcessor {

    private char delimiter = ',';
    private boolean hasHeader = true;

    public CsvProcessor() {
    }

    public CsvProcessor(char delimiter, boolean hasHeader) {
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
    }

    /**
     * Read CSV file and return list of maps.
     * @param filePath Path to CSV file
     * @return List of row data maps
     */
    public List<Map<String, String>> readCsv(String filePath) throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.defaultCharset());

        if (lines.isEmpty()) {
            return results;
        }

        List<String> headers = parseLine(lines.get(0));
        int startRow = hasHeader ? 1 : 0;

        for (int i = startRow; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                Map<String, String> row = parseRow(line, headers, i);
                results.add(row);
            }
        }
        return results;
    }

    /**
     * Parse a CSV line into fields, handling quotes.
     */
    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == delimiter && !inQuotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString().trim());
        return fields;
    }

    /**
     * Parse a row into a map using headers.
     */
    private Map<String, String> parseRow(String line, List<String> headers, int rowNum) {
        Map<String, String> row = new LinkedHashMap<>();
        List<String> fields = parseLine(line);

        for (int i = 0; i < headers.size(); i++) {
            String value = i < fields.size() ? fields.get(i) : "";
            row.put(headers.get(i), value);
        }
        return row;
    }

    /**
     * Write data to CSV file.
     */
    public void writeCsv(String filePath, List<Map<String, String>> data) throws IOException {
        if (data.isEmpty()) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            Set<String> headers = data.get(0).keySet();
            writer.println(String.join(String.valueOf(delimiter), headers));

            for (Map<String, String> row : data) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add(escapeField(row.getOrDefault(header, "")));
                }
                writer.println(String.join(String.valueOf(delimiter), values));
            }
        }
    }

    /**
     * Escape CSV field if needed.
     */
    private String escapeField(String field) {
        if (field.contains(String.valueOf(delimiter)) || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Infer data types from CSV data.
     */
    public Map<String, String> inferTypes(List<Map<String, String>> data) {
        Map<String, String> types = new LinkedHashMap<>();
        if (data.isEmpty()) return types;

        for (String key : data.get(0).keySet()) {
            types.put(key, inferType(data, key));
        }
        return types;
    }

    /**
     * Infer type for a specific column.
     */
    private String inferType(List<Map<String, String>> data, String column) {
        boolean isInteger = true;
        boolean isDouble = true;
        boolean isBoolean = true;

        for (Map<String, String> row : data) {
            String value = row.get(column);
            if (value == null || value.isEmpty()) continue;

            if (isInteger) {
                try {
                    Long.parseLong(value);
                } catch (NumberFormatException e) {
                    isInteger = false;
                }
            }
            if (isDouble) {
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    isDouble = false;
                }
            }
            if (isBoolean) {
                isBoolean = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            }
        }

        if (isInteger) return "integer";
        if (isDouble) return "double";
        if (isBoolean) return "boolean";
        return "string";
    }
}
