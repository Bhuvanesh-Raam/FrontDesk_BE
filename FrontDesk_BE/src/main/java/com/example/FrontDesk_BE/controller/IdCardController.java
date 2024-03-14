package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.constants.CsvConstants;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import com.example.FrontDesk_BE.service.IdCardService;
import com.example.FrontDesk_BE.service.TempIdCardService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
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


    @GetMapping("list")
    public Page<IdCardDto> getIdCardDtoList(Pageable pageable) {
        return idCardService.getIdCardDtoList(pageable);
    }

    @GetMapping("search/{id}")
    public IdCardDto getIdCardDtoWithID(@PathVariable Long id)
    {
        return idCardService.getIdCard(id);
    }

    @GetMapping("filterByReturnStatus")
    public Page<IdCardDto> filterByReturnStatus(Pageable pageable){
        return idCardService.filterByReturnStatus(pageable);
    }

    @GetMapping("filterByIdOrName")
    public Page<IdCardDto> filterByIdOrName(@RequestParam("searchParam") String searchParam, Pageable pageable)
    {
        return idCardService.filterByIDorName(searchParam,pageable);
    }

    @GetMapping("exportdata")
    public void downloadCsv(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate")LocalDate endDate, HttpServletResponse response)
    {
        File exportFile= new File("exportData.csv");
        try{
            List<IdCardDto> idCardDtoList=idCardService.downloadCsv(startDate,endDate);
            CsvMapper mapper= new CsvMapper();
            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            mapper.registerModule(new JavaTimeModule());
            CsvSchema.Builder builder=CsvSchema.builder().addColumn(CsvConstants.CSV_IDCARD_ID).addColumn(CsvConstants.CSV_ID_ISSUE_DATE).addColumn(CsvConstants.CSV_ID_RECEIVER_NAME).addColumn(CsvConstants.CSV_ID_ISSUER_NAME).addColumn(CsvConstants.CSV_ID_RETURN_DATE).addColumn(CsvConstants.CSV_ID_TEMPID_NAME);
            CsvSchema schema=builder.setUseHeader(true).build();
            String data=mapper.writer(schema).writeValueAsString(idCardDtoList);
            response.setContentType("text/csv; charset=utf-8");
            response.addHeader("content-Disposition","attachment; filename=export.csv");
            response.getWriter().write(data);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(exportFile.exists()){
                exportFile.delete();
            }
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
