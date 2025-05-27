package com.mycompany.statdata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataExport {
    Workbook workbook;

    public DataExport() {
        this.workbook = new XSSFWorkbook();
    }

    public void export(List<Map<String, Object>> data, String filePath, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);

        if (data.isEmpty()) {
            throw new IllegalArgumentException("Вы экспортируете пустой файл");
        }

        Row headerRow = sheet.createRow(0);
        Map<String, Object> firstEntry = data.get(0);
        
        int headerCellIndex = 0;
        if (firstEntry.containsKey("sample")) {
            headerRow.createCell(headerCellIndex++).setCellValue("sample");
        }

        for (String key : firstEntry.keySet()) {
            if (!key.equals("sample")) {
                Cell cell = headerRow.createCell(headerCellIndex++);
                cell.setCellValue(key);
            }
        }

        int rowIndex = 1;
        for (Map<String, Object> dataMap : data) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;

            if (dataMap.containsKey("sample")) {
                Cell cell = row.createCell(cellIndex++);
                Object sampleValue = dataMap.get("sample");
                if (sampleValue instanceof Number) {
                    cell.setCellValue(((Number) sampleValue).doubleValue());
                } else {
                    cell.setCellValue(sampleValue.toString());
                }
            }

            for (String key : firstEntry.keySet()) {
                if (!key.equals("sample")) {
                    Object value = dataMap.get(key);
                    Cell cell = row.createCell(cellIndex++);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        // autoSizeColumn(i)
        for (int i = 0; i < headerCellIndex; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void saveToFile_and_CloseWorkbook(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
