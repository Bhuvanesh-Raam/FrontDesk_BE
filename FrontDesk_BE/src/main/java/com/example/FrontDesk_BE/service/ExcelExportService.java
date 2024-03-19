package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.model.excelModel;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ExcelExportService {
    public Workbook exportToExcel(List<excelModel> data, String filePath){
        Workbook workbook=new XSSFWorkbook();
        try{
            Sheet sheet=workbook.createSheet("Data");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("TimesNewRoman");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            headerStyle.setFont(font);
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);


            Row headerRow=sheet.createRow(0);
            String headers[]={"IdCard-ID","Issue-Date","Receiver-Name","Issuer-Name","Return-Date","TempID-Name"};
            for(int i=0;i<headers.length;i++)
            {
                Cell cell=headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i,4750);
            }
            int rowNum=1;
            for(excelModel model: data){
                DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String issueDateString=model.getIssueDate().format(dateFormatter);
                String returnDateString=model.getReturnDate()!=null? model.getReturnDate().format(dateFormatter) : "Not returned!";
                Row row= sheet.createRow(rowNum++);
                Cell cell0=row.createCell(0);
                cell0.setCellValue(model.getId());
                cell0.setCellStyle(dataStyle);
                Cell cell1=row.createCell(1);
                cell1.setCellValue(issueDateString);
                cell1.setCellStyle(dataStyle);
                Cell cell2=row.createCell(2);
                cell2.setCellValue(model.getEmpName());
                cell2.setCellStyle(dataStyle);
                Cell cell3=row.createCell(3);
                cell3.setCellValue(model.getIdIssuer());
                cell3.setCellStyle(dataStyle);
                Cell cell4=row.createCell(4);
                cell4.setCellValue(returnDateString);
                cell4.setCellStyle(dataStyle);
                Cell cell5=row.createCell(5);
                cell5.setCellValue(model.getTempIdName());
                cell5.setCellStyle(dataStyle);

                /*row.createCell(1).setCellValue(issueDateString);
                row.createCell(2).setCellValue(model.getEmpName());
                row.createCell(3).setCellValue(model.getIdIssuer());
                row.createCell(4).setCellValue(returnDateString);
                row.createCell(5).setCellValue(model.getTempIdName());*/
            }
            FileOutputStream file=new FileOutputStream(filePath);
            workbook.write(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return workbook;
    }

}
