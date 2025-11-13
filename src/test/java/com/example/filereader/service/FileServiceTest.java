package com.example.filereader.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {
    @Autowired
    private FileService fileService;

    @TempDir
    File dir;

    @Test
    void findNMin() throws IOException {
        int[] numbers = {5649, 3574, 8940, 1, 9495, 253, 7574, 4, 6575};
        File testFile = createTestExcelFile("test.xlsx", numbers);
        int result = fileService.findNMinNumber(testFile.getAbsolutePath(), 3);

        assertEquals(253, result);
    }

    @Test
    void findNMinWithDuplicates() throws IOException {
        int[] numbers = {300, 1367, 25, 25, 35, 2};
        File testFile = createTestExcelFile("test.xlsx", numbers);
        int result = fileService.findNMinNumber(testFile.getAbsolutePath(), 2);

        assertEquals(25, result);
    }

    @Test
    void findNMinWithNegativeNumbers() throws IOException {
        int[] numbers = {-300, 1367, -25, 25, -35, 2};
        File testFile = createTestExcelFile("test.xlsx", numbers);
        int result = fileService.findNMinNumber(testFile.getAbsolutePath(), 2);

        assertEquals(-35, result);
    }

    @Test
    void findNMinInEmptyFile() throws IOException {
        int[] numbers = {};
        File testFile = createTestExcelFile("test.xlsx", numbers);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber(testFile.getAbsolutePath(), 1));

        assertEquals("File doesn`t contain integers", exception.getMessage());

    }

    @Test
    void findNMinWhereNIsGreaterThanNumbers() throws IOException {
        int[] numbers = {1, 2, 3};
        File testFile = createTestExcelFile("test.xlsx", numbers);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber(testFile.getAbsolutePath(), 7));

        assertEquals("N cannot be greater than the number of elements", exception.getMessage());
    }

    @Test
    void findNMinWithNullN() throws IOException {
        int[] numbers = {1, 2, 3};
        File testFile = createTestExcelFile("test.xlsx", numbers);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber(testFile.getAbsolutePath(), 0));

        assertEquals("N should be positive", exception.getMessage());
    }

    @Test
    void findNMinWithNegativeN() throws IOException {
        int[] numbers = {1, 2, 3};
        File testFile = createTestExcelFile("test.xlsx", numbers);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber(testFile.getAbsolutePath(), -5));

        assertEquals("N should be positive", exception.getMessage());
    }

    @Test
    void findNMinWithNullPath() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber(null, 1));

        assertEquals("File path cannot be empty", exception.getMessage());
    }

    @Test
    void findNMinWithEmptyPath() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber("", 1));

        assertEquals("File path cannot be empty", exception.getMessage());
    }

    @Test
    void findNMinWithWrongFileFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber("test.txt", 1));

        assertEquals("This format is not supported", exception.getMessage());
    }

    @Test
    void findNMinWithNonExistingFile() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> fileService.findNMinNumber("none.xlsx", 1));

        assertEquals("File doesn`t exist", exception.getMessage());
    }


    private File createTestExcelFile(String fileName, int[] numbers) throws IOException {
        File file = new File(dir, fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Numbers");

            int rowNum = 0;
            int columnNum = 0;
            Row row = sheet.createRow(rowNum++);

            for (int num : numbers) {
                if (columnNum >= 10) {
                    row = sheet.createRow(rowNum++);
                    columnNum = 0;
                }
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue(num);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }

        return file;
    }
}