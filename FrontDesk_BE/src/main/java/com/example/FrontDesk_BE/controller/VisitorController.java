package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.dto.VisitorDto;
import com.example.FrontDesk_BE.service.IdCardService;
import com.example.FrontDesk_BE.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("list")
    public Page<VisitorDto> getVisitorDtoList(@RequestParam(value = "searchParam", required = false) String searchParam,
            @RequestParam(value = "returnStatus", required = false) Boolean returnStatus, Pageable pageable) {
        
        return visitorService.getVisitorDtoList(pageable);
        
    }

    @PostMapping("save")
    public ResponseEntity<String> saveVisitor(@Valid @RequestBody VisitorDto visitorDto) {
        return visitorService.saveVisitor(visitorDto);
    }

    @PostMapping("edit")
    public ResponseEntity<String> editVisitor(@RequestBody VisitorDto visitorDto) {
        return visitorService.editVisitor(visitorDto);
    }

    // @PutMapping("return")
    // public ResponseEntity<String> clockOutVisitor(@RequestBody VisitorDto
    // visitorDto)
    // {
    // return idCardService.returnIdCard(idCardDto);
    // }

    @GetMapping("search/{id}")
    public VisitorDto getVisitorDtoWithID(@PathVariable Long id) {
        return visitorService.getVisitor(id);
    }
}
