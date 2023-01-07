package com.faisal.testapi.api.utils;

import com.faisal.testapi.api.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "upload";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Employee> excel(InputStream is) {
        Logger logger = LoggerFactory.getLogger(ExcelHelper.class);
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Employee> employee = new ArrayList<Employee>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Employee employees = new Employee();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            employees.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            employees.setEmail(currentCell.getStringCellValue());
                            break;
                        case 2:

                            Double phone = currentCell.getNumericCellValue();
                            logger.info("PHONE ================> "+ (long)Math.floor(phone + 0.5d));

                            long IntValue = (long)Math.floor(phone + 0.5d);
                            String phones =String.valueOf(IntValue);
                            employees.setPhone("0"+phones);
                            break;
                        case 3:
                            employees.setAddress(currentCell.getStringCellValue());
                            break;
                        case 4:
                            employees.setCreate_date(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
                Date date = new Date();
                employees.setCreate_date(date.toString());

                employee.add(employees);
            }
            workbook.close();
            return employee;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
