package com.filereadwrite;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Excel file reader using Apache POI.
 * Supports both XLS and XLSX formats.
 */
public class ExcelReader {

    private FormulaEvaluator evaluator;

    /**
     * Read an Excel file and return data as list of maps.
     * @param file Excel file to read
     * @return List of row data maps
     */
    public List<Map<String, Object>> readExcel(File file) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = createWorkbook(fis, file.getName())) {

            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);

            List<String> headers = extractHeaders(sheet);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowData = extractRowData(row, headers);
                if (!rowData.isEmpty()) {
                    results.add(rowData);
                }
            }
        }
        return results;
    }

    /**
     * Create appropriate workbook based on file extension.
     */
    private Workbook createWorkbook(FileInputStream fis, String fileName) throws IOException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (fileName.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        }
        throw new IllegalArgumentException("Unsupported Excel format: " + fileName);
    }

    /**
     * Extract headers from the first row.
     */
    private List<String> extractHeaders(Sheet sheet) {
        List<String> headers = new ArrayList<>();
        Row headerRow = sheet.getRow(0);

        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                headers.add(cell != null ? getCellValueAsString(cell) : "Column_" + i);
            }
        }
        return headers;
    }

    /**
     * Extract data from a single row.
     */
    private Map<String, Object> extractRowData(Row row, List<String> headers) {
        Map<String, Object> rowData = new LinkedHashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                Object value = getCellValue(cell);
                rowData.put(headers.get(i), value);
            }
        }
        return rowData;
    }

    /**
     * Get cell value with proper type handling.
     */
    private Object getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                double numVal = cell.getNumericCellValue();
                if (numVal == Math.floor(numVal) && !Double.isInfinite(numVal)) {
                    return (long) numVal;
                }
                return numVal;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return evaluateFormula(cell);
            case BLANK:
                return null;
            default:
                return cell.toString();
        }
    }

    /**
     * Evaluate formula cell.
     */
    private Object evaluateFormula(Cell cell) {
        try {
            CellValue value = evaluator.evaluate(cell);
            switch (value.getCellType()) {
                case NUMERIC:
                    return value.getNumberValue();
                case STRING:
                    return value.getStringValue();
                case BOOLEAN:
                    return value.getBooleanValue();
                default:
                    return null;
            }
        } catch (Exception e) {
            return cell.getCellFormula();
        }
    }

    /**
     * Get cell value as string.
     */
    private String getCellValueAsString(Cell cell) {
        Object value = getCellValue(cell);
        return value != null ? value.toString() : "";
    }

    /**
     * Write data to Excel file.
     */
    public void writeExcel(File file, List<Map<String, Object>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {

            Sheet sheet = workbook.createSheet("Data");

            if (!data.isEmpty()) {
                Row headerRow = sheet.createRow(0);
                Set<String> headers = data.get(0).keySet();
                int colIdx = 0;
                for (String header : headers) {
                    headerRow.createCell(colIdx++).setCellValue(header);
                }

                int rowIdx = 1;
                for (Map<String, Object> rowData : data) {
                    Row row = sheet.createRow(rowIdx++);
                    colIdx = 0;
                    for (String header : headers) {
                        Object value = rowData.get(header);
                        Cell cell = row.createCell(colIdx++);
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }

            workbook.write(fos);
        }
    }
}
