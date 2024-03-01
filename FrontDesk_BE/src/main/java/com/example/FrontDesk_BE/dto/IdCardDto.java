package com.example.FrontDesk_BE.dto;

import com.example.FrontDesk_BE.entity.TempIDCard;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdCardDto {
    private Long id;
    private LocalDate issueDate;
    private LocalTime inTime;
    @NotEmpty(message = "Employee name should not be empty or blank")
    private String empName;
    private Long empId;
    private LocalTime outTime;
    private LocalDate returnDate;
    private String idIssuer;
    private TempIDCard tempId;
}
