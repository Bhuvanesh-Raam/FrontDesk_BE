package com.example.FrontDesk_BE.dto;

import jakarta.annotation.Nonnull;
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
    private String empName;
    private Long empId;
    private LocalTime outTime;
    private LocalDate returnDate;
    private String idIssuer;
    private String tempId;
}
