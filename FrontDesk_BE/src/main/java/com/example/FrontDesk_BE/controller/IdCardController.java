package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import com.example.FrontDesk_BE.service.IdCardService;
import com.example.FrontDesk_BE.service.TempIdCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @GetMapping("all")
    public List<IDCard> getAllIdCard(){
        return idCardService.getAll();
    }

    @GetMapping("list")
    public List<IDCard> getIdCardList(Pageable pageable) {
        return idCardService.getAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveForgotId(@RequestBody IdCardDto idCardDto){
        return idCardService.saveIdCard(idCardDto);
    }

    @PostMapping("/issueTempIdCard")
    public ResponseEntity<String> assignTempId(@RequestParam("tempIdCardId") Long tempIdCardId, @RequestParam("empId") Long empId){
        tempIdCardService.assignTempIdCard(tempIdCardId,empId);
        return ResponseEntity.ok("Temp Id Assigned successfully");
    }


}
