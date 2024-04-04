package com.example.FrontDesk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "User Name should not be empty or blank")
    private String userName;
    @NotBlank(message = "Password should not be empty or blank")
    private String password;
}
