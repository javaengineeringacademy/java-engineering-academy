package com.filereadwrite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;

/**
 * JSON file processor using Jackson library.
 * Supports object mapping and streaming for large files.
 */
public class JsonProcessor {

    private final ObjectMapper objectMapper;

    public JsonProcessor() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Read JSON file and return parsed object.
     * @param filePath Path to JSON file
     * @return Parsed JSON as Object, List, or Map
     */
    public Object readJson(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, Object.class);
    }

    /**
     * Read JSON file as a list of maps.
     */
    public List<Map<String, Object>> readJsonAsList(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Read JSON file and map to specific class.
     */
    public <T> T readJson(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, clazz);
    }

    /**
     * Write object to JSON file.
     */
    public void writeJson(String filePath, Object data) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, data);
    }

    /**
     * Write list of objects to JSON file.
     */
    public void writeJsonList(String filePath, List<?> data) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, data);
    }

    /**
     * Read JSON using streaming for large files.
     * Returns list of maps without loading entire file into memory.
     */
    public List<Map<String, Object>> readJsonStreaming(String filePath) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(filePath);
             JsonParser parser = objectMapper.getFactory().createParser(inputStream)) {

            JsonToken token;
            while ((token = parser.nextToken()) != null) {
                if (token == JsonToken.START_ARRAY) {
                    while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
                        if (token == JsonToken.START_OBJECT) {
                            Map<String, Object> obj = objectMapper.readValue(parser, Map.class);
                            results.add(obj);
                        }
                    }
                    break;
                }
            }
        }
        return results;
    }

    /**
     * Merge two JSON objects.
     */
    public Map<String, Object> mergeJson(Map<String, Object> base, Map<String, Object> override) {
        ObjectNode baseNode = objectMapper.valueToTree(base);
        ObjectNode overrideNode = objectMapper.valueToTree(override);

        baseNode.setAll(overrideNode);
        return objectMapper.convertValue(baseNode, Map.class);
    }

    /**
     * Pretty-print JSON string.
     */
    public String prettyPrint(String json) throws IOException {
        Object obj = objectMapper.readValue(json, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Validate JSON string.
     */
    public boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract a specific field from JSON file.
     */
    public Object extractField(String filePath, String fieldName) throws IOException {
        JsonNode node = objectMapper.readTree(new File(filePath));
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? objectMapper.convertValue(fieldNode, Object.class) : null;
    }
}
