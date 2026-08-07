package com.filereadwrite;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for file processing classes.
 */
public class FileProcessorTest {

    @TempDir
    Path tempDir;

    private FileProcessor fileProcessor;
    private ExcelReader excelReader;
    private CsvProcessor csvProcessor;
    private JsonProcessor jsonProcessor;
    private FileUtils fileUtils;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor();
        excelReader = new ExcelReader();
        csvProcessor = new CsvProcessor();
        jsonProcessor = new JsonProcessor();
        fileUtils = new FileUtils();
    }

    @Test
    @DisplayName("Test CSV processing")
    void testCsvProcessing() throws IOException {
        File csvFile = tempDir.resolve("test.csv").toFile();
        List<Map<String, String>> data = new ArrayList<>();
        data.add(Map.of("name", "Alice", "age", "30", "city", "NYC"));
        data.add(Map.of("name", "Bob", "age", "25", "city", "LA"));

        csvProcessor.writeCsv(csvFile.getAbsolutePath(), data);
        List<Map<String, String>> readData = csvProcessor.readCsv(csvFile.getAbsolutePath());

        assertEquals(2, readData.size());
        assertEquals("Alice", readData.get(0).get("name"));
        assertEquals("30", readData.get(0).get("age"));
    }

    @Test
    @DisplayName("Test JSON processing")
    void testJsonProcessing() throws IOException {
        File jsonFile = tempDir.resolve("test.json").toFile();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", "Test");
        data.put("value", 42);
        List<Map<String, Object>> list = Arrays.asList(data);

        jsonProcessor.writeJsonList(jsonFile.getAbsolutePath(), list);
        Object result = jsonProcessor.readJson(jsonFile.getAbsolutePath());

        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test
    @DisplayName("Test JSON validation")
    void testJsonValidation() {
        assertTrue(jsonProcessor.isValidJson("{\"key\": \"value\"}"));
        assertTrue(jsonProcessor.isValidJson("[1, 2, 3]"));
        assertFalse(jsonProcessor.isValidJson("{invalid}"));
        assertFalse(jsonProcessor.isValidJson(""));
    }

    @Test
    @DisplayName("Test file validation")
    void testFileValidation() {
        assertTrue(fileUtils.isSupportedFileType(new File("test.xlsx")));
        assertTrue(fileUtils.isSupportedFileType(new File("data.csv")));
        assertTrue(fileUtils.isSupportedFileType(new File("config.json")));
        assertFalse(fileUtils.isSupportedFileType(new File("image.png")));
    }

    @Test
    @DisplayName("Test file extension extraction")
    void testFileExtension() {
        assertEquals("csv", fileUtils.getFileExtension(new File("data.csv")));
        assertEquals("json", fileUtils.getFileExtension(new File("config.json")));
        assertEquals("xlsx", fileUtils.getFileExtension(new File("report.xlsx")));
        assertEquals("", fileUtils.getFileExtension(new File("noextension")));
    }

    @Test
    @DisplayName("Test readable file size")
    void testReadableFileSize() {
        assertEquals("0 B", fileUtils.getReadableFileSize(new File("")));
        File testFile = tempDir.resolve("small.txt").toFile();
        try {
            Files.writeString(testFile.toPath(), "Hello");
            String size = fileUtils.getReadableFileSize(testFile);
            assertTrue(size.contains("B"));
        } catch (IOException e) {
            fail("Failed to create test file");
        }
    }

    @Test
    @DisplayName("Test find files by extension")
    void testFindFiles() throws IOException {
        Files.writeString(tempDir.resolve("a.csv"), "data");
        Files.writeString(tempDir.resolve("b.csv"), "data");
        Files.writeString(tempDir.resolve("c.txt"), "data");

        List<File> csvFiles = fileUtils.findFiles(tempDir.toFile(), "csv");
        assertEquals(2, csvFiles.size());
    }

    @Test
    @DisplayName("Test CSV type inference")
    void testCsvTypeInference() {
        List<Map<String, String>> data = new ArrayList<>();
        data.add(Map.of("id", "123", "name", "Test", "active", "true", "price", "19.99"));
        Map<String, String> types = csvProcessor.inferTypes(data);

        assertEquals("integer", types.get("id"));
        assertEquals("string", types.get("name"));
        assertEquals("boolean", types.get("active"));
        assertEquals("double", types.get("price"));
    }

    @Test
    @DisplayName("Test CSV quoted fields")
    void testCsvQuotedFields() throws IOException {
        File csvFile = tempDir.resolve("quoted.csv").toFile();
        try (PrintWriter writer = new PrintWriter(csvFile)) {
            writer.println("name,description");
            writer.println("Test,\"A value, with comma\"");
        }

        List<Map<String, String>> data = csvProcessor.readCsv(csvFile.getAbsolutePath());
        assertEquals(1, data.size());
        assertEquals("A value, with comma", data.get(0).get("description"));
    }

    @Test
    @DisplayName("Test batch processing")
    void testBatchProcessing() throws IOException {
        File csvFile1 = tempDir.resolve("batch1.csv").toFile();
        File csvFile2 = tempDir.resolve("batch2.csv").toFile();

        List<Map<String, String>> data = List.of(Map.of("col", "val"));
        csvProcessor.writeCsv(csvFile1.getAbsolutePath(), data);
        csvProcessor.writeCsv(csvFile2.getAbsolutePath(), data);

        List<String> files = Arrays.asList(csvFile1.getAbsolutePath(), csvFile2.getAbsolutePath());
        List<FileProcessor.ProcessingResult> results = fileProcessor.processBatch(files);

        assertEquals(2, results.size());
    }
}
