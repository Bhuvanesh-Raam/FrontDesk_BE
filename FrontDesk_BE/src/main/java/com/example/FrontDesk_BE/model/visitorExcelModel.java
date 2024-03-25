package com.example.FrontDesk_BE.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class visitorExcelModel {
    private Long id;
    private LocalDate issueDate;
    private LocalTime inTime;
    private LocalTime outTime;
    private String visitorName;
    private String visitorType;
    private String phoneNo;
    private String employeeVisited;
    private String accessoriesBrought;
    private Long tempIdIssued;
    private LocalDate returnDate;
    private LocalTime clockoutDate;
    private String idIssuer;
}
