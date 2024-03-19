package com.example.FrontDesk_BE.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String displayId;
    private LocalTime inTime;
    private LocalTime outTime;
}
