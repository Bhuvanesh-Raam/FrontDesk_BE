package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.model.excelModel;

import com.example.FrontDesk_BE.model.visitorExcelModel;
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
            String headers[]={"IdCard-ID","Issue-Date","Receiver-Name","Issuer-Name","Return-Date","In-Time","Out-Time","TempID-Name"};
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
                DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("hh:mm:ss a");
                String issueDateString=model.getIssueDate().format(dateFormatter);
                String returnDateString=model.getReturnDate()!=null? model.getReturnDate().format(dateFormatter) : "Not returned!";
                String inTimeString=model.getInTime().format(timeFormatter);
                String outTimeString=model.getOutTime()!=null? model.getOutTime().format(timeFormatter) : "Not Returned!";
                Row row= sheet.createRow(rowNum++);
                Cell cell0=row.createCell(0);
                cell0.setCellValue(model.getDisplayId());
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
                cell5.setCellValue(inTimeString);
                cell5.setCellStyle(dataStyle);
                Cell cell6=row.createCell(6);
                cell6.setCellValue(outTimeString);
                cell6.setCellStyle(dataStyle);
                Cell cell7=row.createCell(7);
                cell7.setCellValue(model.getTempIdName());
                cell7.setCellStyle(dataStyle);

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

    

     public Workbook exportVisitorToExcel(List<visitorExcelModel> data, String filePath){
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
             String headers[]={"Visitor ID", "Issue Date", "Visitor Name", "Visitor Type","Phone No.", "Issuer Name","Employee Visited",   "Temp ID Issued", "Clockout Date","InTime", "OutTime"};
             for(int i=0;i<headers.length;i++)
             {
                 Cell cell=headerRow.createCell(i);
                 cell.setCellValue(headers[i]);
                 cell.setCellStyle(headerStyle);
                 sheet.setColumnWidth(i,4750);
             }
             int rowNum=1;
             for (visitorExcelModel model : data) {
                 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                 DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
                 String issueDateString = model.getIssueDate().format(dateFormatter);
                 String clockoutDateString = model.getClockoutDate() != null ? model.getClockoutDate().format(dateFormatter) : "Not returned!";
                 String inTimeString = model.getInTime().format(timeFormatter);
                 String outTimeString = model.getOutTime() != null ? model.getOutTime().format(timeFormatter) : "Not Returned!";

                 Row row= sheet.createRow(rowNum++);
                 Cell cell0 = row.createCell(0);
                 cell0.setCellValue(model.getDisplayId());
                 cell0.setCellStyle(dataStyle);
                 Cell cell1 = row.createCell(1);
                 cell1.setCellValue(issueDateString);
                 cell1.setCellStyle(dataStyle);
                 Cell cell2 = row.createCell(2);
                 cell2.setCellValue(model.getVisitorName());
                 cell2.setCellStyle(dataStyle);
                 Cell cell3 = row.createCell(3);
                 cell3.setCellValue(model.getVisitorType());
                 cell3.setCellStyle(dataStyle);
                 Cell cell4 = row.createCell(4);
                 cell4.setCellValue(model.getContactNo());
                 cell4.setCellStyle(dataStyle);
                 Cell cell5 = row.createCell(5);
                 cell5.setCellValue(model.getIdIssuer());
                 cell5.setCellStyle(dataStyle);
                 Cell cell6 = row.createCell(6);
                 cell6.setCellValue(model.getEmployeeVisited());
                 cell6.setCellStyle(dataStyle);
                 Cell cell7 = row.createCell(7);
                 cell7.setCellValue(model.getTempIdIssued());
                 cell7.setCellStyle(dataStyle);
                 Cell cell8 = row.createCell(8);
                 cell8.setCellValue(clockoutDateString);
                 cell8.setCellStyle(dataStyle);
                 Cell cell9 = row.createCell(9);
                 cell9.setCellValue(inTimeString);
                 cell9.setCellStyle(dataStyle);

                 Cell cell10 = row.createCell(10);
                 cell10.setCellValue(outTimeString);
                 cell10.setCellStyle(dataStyle);
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
