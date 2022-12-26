package com.sungam1004.register.utill;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ExcelManager {
    public static String filePath = "/Users/hongseungtaeg/Desktop/toyproject/toyProject7-register/file";
    public static String fileNm = LocalDateTime.now() +".xlsx";

    public File createExcelFile(List<String> names, List<List<LocalDateTime>> data) {

        // 빈 Workbook 생성
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 빈 Sheet를 생성
        XSSFSheet sheet = workbook.createSheet("출석부");

        int rownum = 0;
        for (int i = 0; i < names.size(); i++) {
            Row row = sheet.createRow(rownum++);
            int cellnum = 0;
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(names.get(i));

            for (LocalDateTime at : data.get(i)) {
                cell = row.createCell(cellnum++);
                if (at != null) cell.setCellValue(at.toString());
                //else System.out.print("");
            }
        }

        try {
            File file = new File(filePath, fileNm);
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
