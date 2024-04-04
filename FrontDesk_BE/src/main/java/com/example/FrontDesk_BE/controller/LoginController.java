package com.example.FrontDesk_BE.controller;

import com.example.FrontDesk_BE.dto.UserLoginDto;
import com.example.FrontDesk_BE.service.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/login")
public class LoginController {
    @Autowired private UserLoginService userLoginService;

    @PostMapping
    public ResponseEntity<String> userLogin(@Valid @RequestBody UserLoginDto userLoginDto)
    {
        return userLoginService.userLogin(userLoginDto);
    }
}
