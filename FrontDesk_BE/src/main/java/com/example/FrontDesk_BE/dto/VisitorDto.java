package com.example.FrontDesk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDto {
    private Long id;
    @NotBlank(message = "Visitor name should not be empty or blank")
    private String visitorName;
    @NotNull(message = "Visitor Type should not be empty or blank")
    private String visitorType;
    @NotNull(message = "Issue Date should not be empty or null")
    private LocalDate issueDate;
    @NotNull(message = "In Time should not be empty or null")
    private LocalTime inTime;
    @NotNull(message = "Contact Number should not be empty or null")
    private String contactNumber;
    private Boolean visitEmployee;
    private String empName;
    private Long empId;
    private String employeeID;
    private LocalTime outTime;
    private LocalDate returnDate;
    @NotBlank(message = "Issuer name should not be empty or blank")
    private String idIssuer;
    private Long tempId;
    private String tempIdName;
    private String purposeOfVisit;
    private Boolean tempIdIssued;
    private Boolean hasAccessories;
    private String visitorSign;
    private String issuerSign;
    private String imgCapture;
//    private Boolean returnStatus;
    private Boolean clockedOutStatus;

    private List<AccessoryDto> accessories;
    private String DisplayId;
    private LocalDateTime lastUpdatedDate;

}
