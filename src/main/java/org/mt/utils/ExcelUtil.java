package org.mt.utils;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    DecimalFormat decimalFormat;

    public ExcelUtil() {
        decimalFormat = new DecimalFormat("0");
    }

    @SneakyThrows
    public ExcelUtil readFile(File file, String sheetName) {
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheet(sheetName);
        return this;
    }

    @SneakyThrows
    public void writeFile(File file) {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public ExcelUtil deleteGivenCells(int startRow, int endRow, int column) {
        for (int rownum = startRow - 1; rownum < endRow; rownum++) {
            XSSFRow xssfRow = sheet.getRow(rownum);
            xssfRow.removeCell(xssfRow.getCell(column-1));
        }
        return this;
    }

    public ExcelUtil deleteCellByColumn(int column) {
        int columnIndex = column - 1;
        for (Row row : sheet)
            if (row.getCell(columnIndex) != null)
                row.removeCell(row.getCell(columnIndex));
        return this;
    }

    public Object[][] getDataArray() {
        int rowCount = sheet.getLastRowNum() + 1;
        int colCount = sheet.getRow(0).getLastCellNum();

        Object[][] objectArray = new Object[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                XSSFCell cell = sheet.getRow(i).getCell(j);
                if (cell != null) {
                    Object object = getValueByCellType(sheet.getRow(i).getCell(j));
                    if (object instanceof Double)
                        objectArray[i][j] = decimalFormat.format(object);
                    else
                        objectArray[i][j] = object;
                }
            }
        }
        return objectArray;
    }

    public List<List<Object>> getDataList() {
        List<List<Object>> dataList = new ArrayList<>();
        for (Row row : sheet) {
            List<Object> rowList = new ArrayList<>();
            for (Cell cell : row) {
                Object object = getValueByCellType(cell);
                if (object instanceof Double) rowList.add(Integer.parseInt(decimalFormat.format(object)));
                else rowList.add(object);
            }
            if (!rowList.isEmpty()) dataList.add(rowList);
        }
        return dataList;
    }

    public List<List<Object>> getDataListWithGivenColumnBackgroundColor(int colorColumnNum) {
        List<List<Object>> dataList = new ArrayList<>();
        for (Row row : sheet) {
            List<Object> rowList = new ArrayList<>();
            for (int i = 0; i < row.getLastCellNum(); i++) {
                XSSFCell cell = (XSSFCell) row.getCell(i);
                if (i + 1 == colorColumnNum) rowList.add(getBackgroundColor(cell));
                else {
                    Object object = getValueByCellType(cell);
                    if (object instanceof Double) rowList.add(Integer.parseInt(decimalFormat.format(object)));
                    else rowList.add(object);
                }
            }
            if (!rowList.isEmpty()) dataList.add(rowList);
        }
        return dataList;
    }

    public Object[][] getDataFromSheet(int numRows, int numCols) {
        Object[][] objects = new Object[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                objects[i][j] = getValueByCellType(sheet.getRow(i).getCell(j));
            }
        }
        return objects;
    }

    public void addRow(Object[] objects) {
        XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < objects.length; i++) {
            setValueByCellType(newRow.createCell(i), objects[i]);
        }
    }

    public void addRow(List<Object> objects) {
        XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < objects.size(); i++) {
            setValueByCellType(newRow.createCell(i), objects.get(i));
        }
    }

    public void addRowWithGivenColumnBackgroundColor(List<Object> objects, int colorColumnNum) {
        XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < objects.size(); i++) {
            if (i + 1 == colorColumnNum) setBackgroundColor(newRow.createCell(i), objects.get(i));
            else setValueByCellType(newRow.createCell(i), objects.get(i));
        }
    }

    @Step("Get data row count")
    public int getDataRowCount(int headerRowCount) {
        return sheet.getLastRowNum() + 1 - headerRowCount;
    }

    private Object getValueByCellType(XSSFCell xssfCell) {
        switch (xssfCell.getCellType()) {
            case STRING:
                return xssfCell.getStringCellValue();
            case NUMERIC:
                return xssfCell.getNumericCellValue();
            case BLANK:
                return "";
            default:
                throw new Error(xssfCell.getCellType() + " does not supported!");
        }
    }

    private Object getValueByCellType(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BLANK:
                return "";
            default:
                throw new Error(cell.getCellType() + " does not supported!");
        }
    }

    private void setValueByCellType(XSSFCell xssfCell, Object value) {
        if (value instanceof String)
            xssfCell.setCellValue((String) value);
        else if (value instanceof Integer)
            xssfCell.setCellValue((Integer) value);
        else
            throw new Error("Value type does not supported! Value: " + value);
    }

    private String getBackgroundColor(XSSFCell cell) {
        try {
            byte[] rgbWithTint = cell.getCellStyle().getFillForegroundColorColor().getRGBWithTint();
            if (rgbWithTint == null) return null;
            return "#" + Hex.encodeHexString(rgbWithTint).toUpperCase();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    private void setBackgroundColor(XSSFCell xssfCell, Object value) {
        if (value instanceof String) {
            if (((String) value).equalsIgnoreCase("red")) {
                XSSFCellStyle style = workbook.createCellStyle();
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                xssfCell.setCellStyle(style);
            } else throw new Error(value + " color does not supported!");
        } else throw new Error(value + " does not supported!");
    }
}