package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.VisitorDto;
import com.example.FrontDesk_BE.model.visitorExcelModel;
import com.example.FrontDesk_BE.service.ExcelExportService;
import com.example.FrontDesk_BE.service.VisitorService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
/* @Validated */
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/visitorAccess/")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("list")
    public Page<VisitorDto> getVisitorDtoList(
            @RequestParam(value = "searchParam", required = false) String searchParam,
            @RequestParam(value = "clockedOutStatus", required = false) Boolean clockedOutStatus,
            @PageableDefault(sort = {"clockedOutStatus", "issueDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Boolean clockedOutStatusParam=clockedOutStatus!=null?Boolean.valueOf(clockedOutStatus):null;
        System.out.println("searchParam: " + searchParam + " clockedOutStatus: " + clockedOutStatusParam);
        if (searchParam != null && !searchParam.isEmpty() && clockedOutStatusParam != null) {
            return visitorService.filterByIDorNameAndClockedOutStatus(searchParam, clockedOutStatusParam, pageable);
        } else if (searchParam != null && !searchParam.isEmpty()) {
            return visitorService.filterByIDorName(searchParam, pageable);
        } else if (clockedOutStatusParam != null) {
            return visitorService.filterByClockedOutStatus(clockedOutStatusParam, pageable);
        } else
            return visitorService.getVisitorDtoList(searchParam,pageable);

    }

    @PostMapping("save")
    public ResponseEntity<String> saveVisitor(@Valid @RequestBody VisitorDto visitorDto) {
        try {
            ResponseEntity<String> saved = visitorService.saveVisitor(visitorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<String> editVisitor(@RequestBody VisitorDto visitorDto) {
        return visitorService.editVisitor(visitorDto);
    }

    @PutMapping("clockOut")
    public ResponseEntity<String> clockOutVisitor(@RequestBody VisitorDto visitorDto) {
        return visitorService.clockoutVisitor(visitorDto);
    }

    @GetMapping("search/{id}")
    public VisitorDto getVisitorDtoWithID(@PathVariable Long id) {
        return visitorService.getVisitor(id);
    }
    @CrossOrigin(exposedHeaders = "Content-Disposition")
    @GetMapping("exportdata")
    public void downloadExcel(@RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate, HttpServletResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        String startDateformat = startDate.format(formatter);
        String endDateformat = endDate.format(formatter);
        List<visitorExcelModel> data = visitorService.getVisitorDataForExcel(startDate, endDate);
        String fileName = "exportData_" + startDateformat + "_To_" + endDateformat + ".xlsx";
        Workbook workBook = excelExportService.exportVisitorToExcel(data, fileName);
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workBook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
