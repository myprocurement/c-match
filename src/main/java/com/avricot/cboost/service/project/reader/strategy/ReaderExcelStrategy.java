package com.avricot.cboost.service.project.reader.strategy;

/**
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.avricot.cboost.service.project.reader.FileReaderException;
import com.avricot.cboost.service.project.reader.ILineReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReaderExcelStrategy implements IReaderStrategy{


    @Override
    public void process(File file, ILineReader lineProcessor, final String charsetName, final Integer lineMaxToRead) {
        // Here we read the Excel file.
        InputStream inp;
        try {
            inp = new FileInputStream(file);
            // Get the XLS(X or M) file back
            Workbook wb = WorkbookFactory.create(inp);
            // Get the sheet at the index "indexSheet"
            Sheet sheet = wb.getSheetAt(0);
            // Row number
            double rowNumberInTheSheet = sheet.getPhysicalNumberOfRows();
            // We fill textContainer.
            Object result = null;
            for (int lineNumber = 0; lineNumber < rowNumberInTheSheet; lineNumber++) {
                Row row = sheet.getRow(lineNumber);
                String[] dataArray = new String[row.getPhysicalNumberOfCells()];
                if (row != null) {
                    for (int column = 0; column < row.getPhysicalNumberOfCells(); column++) {
                        Cell cellule = row.getCell(column);
                        Object value = ContenuCellule(cellule);
                        dataArray[column] = value.toString();
                    }
                    result = lineProcessor.processLine(lineNumber, dataArray, result);
                }
                if(lineMaxToRead != null && lineNumber>lineMaxToRead){
                    break;
                }
            }
            inp.close();
        } catch (IOException e) {
            throw new FileReaderException("IO exception during the Excel file reading process", e);
        } catch (InvalidFormatException e) {
            throw new FileReaderException("Invalid Format exception during the Excel file reading process", e);
        }

    }

    /**
     * Returns a cell content, depending on its type.
     */
    private Object ContenuCellule(Cell cell) {
        Object value = null;
        if (cell == null) {
            value = "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            value = cell.getBooleanCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            value = cell.getErrorCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            value = cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            value = cell.getNumericCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            value = cell.getRichStringCellValue();
        }
        return value;
    }






}