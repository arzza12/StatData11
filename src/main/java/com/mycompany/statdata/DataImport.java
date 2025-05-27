package com.mycompany.statdata;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataImport {
   
    public static Map<String, double[]> makeHashMapFromFile() {
        Map<String, double[]> resultMap = new HashMap<>();

        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String inputFileName = selectedFile.getAbsolutePath();

            try (FileInputStream fis = new FileInputStream(inputFileName); Workbook workbook = new XSSFWorkbook(fis)) {

                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                int sheetCount = workbook.getNumberOfSheets();
                String[] sheetNames = new String[sheetCount];
                for (int i = 0; i < sheetCount; i++) {
                    sheetNames[i] = workbook.getSheetName(i);
                }

                String selectedSheetName = (String) JOptionPane.showInputDialog(
                        null, "Выберите лист:", "Выбор листа", JOptionPane.PLAIN_MESSAGE,
                        null, sheetNames, sheetNames[0]);

                if (selectedSheetName == null) {
                    JOptionPane.showMessageDialog(null,
                            "Выбор отменён. Программа завершит выполнение.",
                            "Отмена",
                            JOptionPane.INFORMATION_MESSAGE);
                    return resultMap;
                }

                Sheet selectedSheet = workbook.getSheet(selectedSheetName);

                if (selectedSheet.getPhysicalNumberOfRows() == 0) {
                    JOptionPane.showMessageDialog(null, "Выбранный лист пуст.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return resultMap;
                }

                int columnCount = selectedSheet.getRow(0).getPhysicalNumberOfCells();
                int rowCount = selectedSheet.getPhysicalNumberOfRows();

                for (int k = 0; k < columnCount; k++) {
                    int validDataCount = 0;

                    for (int j = 1; j < rowCount; j++) {
                        Cell cell = selectedSheet.getRow(j).getCell(k);
                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            validDataCount++;
                        }
                    }

                    double[] selection = new double[validDataCount];
                    int index = 0;

                    for (int i = 1; i < rowCount; i++) {
                        Cell cell = selectedSheet.getRow(i).getCell(k);
                        if (cell == null || cell.getCellType() == CellType.BLANK) {
                            continue;  // Пропуск пустой ячейки
                        }

                        try {
                            double value;
                            if (cell.getCellType() == CellType.FORMULA) {
                                value = evaluator.evaluate(cell).getNumberValue();
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                value = cell.getNumericCellValue();
                            } else {
                                continue;  // Пропуск ячеек не содержащих числа/формул
                            }
                            selection[index++] = value;

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка при обработке данных в ячейке: (Строкарока " + (i + 1) + ", Колонка " + (k + 1) + ")" + e.getMessage(), "Ошибка данных", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    resultMap.put(selectedSheet.getRow(0).getCell(k).toString(), selection);
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Файл не найден: " + e.getMessage(), "Ошибка файла", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Файл не может быть открыт как Excel. Пожалуйста, выберите корректный Excel файл.", "Ошибка формата", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Неизвестная ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

        return resultMap;
    }
}
