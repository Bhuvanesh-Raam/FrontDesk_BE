package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import com.example.FrontDesk_BE.service.IdCardService;
import com.example.FrontDesk_BE.service.TempIdCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
