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
    // @NotBlank(message = "Employee name should not be empty or blank")
    private String empName;
    // @NotNull(message = "Employee ID should not be null")
    private Long empId;
    private LocalTime outTime;
    private LocalDate returnDate;
    @NotBlank(message = "Issuer name should not be empty or blank")
    private String idIssuer;
    @NotNull(message = "Temporary ID should not be empty or blank")
    private Long tempId;
    private String tempIdName;
    private String receiverSign;
    private String issuerSign;
    private String imgCapture;
    private Boolean returnStatus;
    private Boolean clockedOutStatus;
    private Boolean visitEmployee;
    private List<AccessoryDto> accessories;
    private String DisplayId;
    private LocalDateTime lastUpdatedDate;

}
