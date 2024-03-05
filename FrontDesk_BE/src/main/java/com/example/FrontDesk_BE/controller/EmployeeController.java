package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.ZohoEmployeeDetail;
import com.example.FrontDesk_BE.service.ZohoPeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/emp/")
public class EmployeeController {
    private final ZohoPeopleService zohoPeopleService;
    @GetMapping("search/{text}")
    public List<ZohoEmployeeDetail>getEmployee(@PathVariable String text)
    {
        return zohoPeopleService.searchEmployee(text);
    }
}
