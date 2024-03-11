package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.TempIDCardDto;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import com.example.FrontDesk_BE.service.TempIdCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/tempid")
public class AdminController {
    @Autowired
    private TempIDCardRepository tempIDCardRepository;
    @Autowired
    private TempIdCardService tempIdCardService;

    @GetMapping("/all")
    public List<TempIDCard>getAllTempIDCard(){return tempIdCardService.getActiveTempIdCard();}

    @PostMapping("/save")
    public ResponseEntity<String> saveTempId(@RequestBody TempIDCardDto tempIDCardDto)
    {
        return tempIdCardService.saveTempIdCard(tempIDCardDto);
    }
}
