package com.sungam1004.register.utill;


import com.sungam1004.register.dto.StatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class ExcelManager {

    @Value("${file.path}")
    public String filePath;

    public String createExcelFile(StatisticsDto statistics) {
        List<List<LocalDateTime>> data = statistics.getAttendance();
        List<String> names = statistics.getNames();

        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ".xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("출석부");

        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        for (int i = 0; i < StatisticsDto.date.size(); i++) {
            row.createCell(i + 1).setCellValue(StatisticsDto.date.get(i));
        }

        for (int i = 0; i < names.size(); i++) {
            row = sheet.createRow(rowNum++);
            int cellNum = 0;
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(names.get(i));

            for (LocalDateTime at : data.get(i)) {
                cell = row.createCell(cellNum++);
                if (at != null) {
                    if (at.getHour() == 0 && at.getMinute() == 0 && at.getSecond() == 0)
                        cell.setCellValue("관리자에 의한 출석");
                    else cell.setCellValue(at.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        }


        CellStyle cellStyle_Table_Center = workbook.createCellStyle();
        cellStyle_Table_Center.setBorderTop(BorderStyle.DOUBLE); //테두리 위쪽
        cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);

        row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
        cell.setCellValue("합계");
        cell.setCellStyle(cellStyle_Table_Center);
        for (int i = 1; i <= StatisticsDto.date.size(); i++) {
            String colAlphabet = getColAlphabet(i);
            String formula = "COUNTA(" + colAlphabet + "2:" + colAlphabet + (names.size() + 1) + ")";
            cell = row.createCell(i);
            cell.setCellFormula(formula);
            cell.setCellStyle(cellStyle_Table_Center);
        }

        try {
            if (new File(filePath).mkdir()) log.info("디렉터리가 생성되었습니다.");
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
            out.close();
            log.info("엑셀 파일이 저장되었습니다. fileFullPath={}", filePath + "/" + fileName);
        } catch (IOException e) {
            log.error("엑셀 저장에 실패했습니다.");
            e.printStackTrace();
        }
        return fileName;
    }

    private String getColAlphabet(int index) {
        if (index >= 0 && index < 26) return String.valueOf((char) (index + 65));
        else if (index < 52) return "A" + ((char) (index + 65 - 26));
        else if (index < 78) return "B" + ((char) (index + 65 - 26 * 2));
        else if (index < 104) return "C" + ((char) (index + 65 - 26 * 3));
        else return "";
    }
}
