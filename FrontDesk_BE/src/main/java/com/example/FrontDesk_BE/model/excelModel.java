package com.example.FrontDesk_BE.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class excelModel {
    private Long id;
    private LocalDate issueDate;
    private String empName;
    private LocalDate returnDate;
    private String idIssuer;
    private String tempIdName;
}
