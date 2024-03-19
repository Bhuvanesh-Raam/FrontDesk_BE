package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.model.excelModel;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import com.example.FrontDesk_BE.service.IdCardExcelExportService;
import com.example.FrontDesk_BE.service.IdCardService;
import com.example.FrontDesk_BE.service.TempIdCardService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
/*@Validated*/
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/idcard/")
public class IdCardController {
    @Autowired
    private TempIDCardRepository tempIDCardRepository;
    @Autowired
    private IdCardService idCardService;
    @Autowired
    private TempIdCardService tempIdCardService;

    @Autowired
    private IdCardExcelExportService excelExportService;

    @GetMapping("list")
    public Page<IdCardDto> getIdCardDtoList( @RequestParam(value = "searchParam",required = false) String searchParam, @RequestParam(value = "returnStatus",required = false) Boolean returnStatus, Pageable pageable) {
        Boolean returnStatusParam=returnStatus!=null?Boolean.valueOf(returnStatus):null;
        if(searchParam!=null && returnStatusParam!=null)
        {
            return idCardService.filterByIDorNameAndReturnStatus(searchParam,returnStatusParam,pageable);
        } else if (searchParam!=null) {
            return idCardService.filterByIDorName(searchParam,pageable);
        } else if (returnStatus!=null) {
            return idCardService.filterByReturnStatus(returnStatus,pageable);
        }
        else{
            return idCardService.getIdCardDtoList(pageable);
        }
    }

    @GetMapping("search/{id}")
    public IdCardDto getIdCardDtoWithID(@PathVariable Long id)
    {
        return idCardService.getIdCard(id);
    }

   /* @GetMapping("filterByReturnStatus")
    public Page<IdCardDto> filterByReturnStatus(Pageable pageable){
        return idCardService.filterByReturnStatus(pageable);
    }

    @GetMapping("filterByIdOrName")
    public Page<IdCardDto> filterByIdOrName(@RequestParam("searchParam") String searchParam, Pageable pageable)
    {
        return idCardService.filterByIDorName(searchParam,pageable);
    }*/

    @GetMapping("filterByDate")
    public Page<IdCardDto> filterByDate(@RequestParam("startDate") LocalDate startDate,@RequestParam("endDate")LocalDate endDate,Pageable pageable)
    {
        return idCardService.filterByDate(startDate,endDate,pageable);
    }

    @GetMapping("exportdata")
    public void downloadExcel(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate")LocalDate endDate, HttpServletResponse response)
    {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MMM-yy");
        String startDateformat=startDate.format(formatter);
        String endDateformat=endDate.format(formatter);
        List<excelModel> data=idCardService.getDataForExcel(startDate,endDate);
        String fileName="exportData_"+startDateformat+"_To_"+endDateformat+".xlsx";
        Workbook workBook=excelExportService.exportToExcel(data,fileName);
        try{
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition","attachment; filename="+fileName);
            workBook.write(response.getOutputStream());
            response.flushBuffer();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveForgotId( @Valid @RequestBody IdCardDto idCardDto){
        return idCardService.saveIdCard(idCardDto);
    }

    @PostMapping("edit")
    public ResponseEntity<String> editIdCard(@RequestBody IdCardDto idCardDto)
    {
        return idCardService.editIdCard(idCardDto);
    }

    @PutMapping("return")
    public ResponseEntity<String> updateIdCard(@RequestBody IdCardDto idCardDto)
    {
        return idCardService.returnIdCard(idCardDto);
    }



}
