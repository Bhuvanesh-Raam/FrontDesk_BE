package com.example.FrontDesk_BE.dto;

/*import com.example.FrontDesk_BE.entity.IdSignature;*/
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdCardDto {
    private Long id;
    @NotNull(message = "Issue Date should not be empty or null")
    private LocalDate issueDate;
    @NotNull(message = "In Time should not be empty or null")
    private LocalTime inTime;
    @NotBlank(message = "Employee name should not be empty or blank")
    private String empName;
    @NotNull(message = "Employee ID should not be null")
    private Long empId;
    private LocalTime outTime;
    private LocalDate returnDate;
    @NotBlank(message = "Issuer name should not be empty or blank")
    private String idIssuer;
    @NotNull(message = "Temporary ID should not be empty or blank")
    private Long tempId;
    private String tempIdName;
    private  String receiverSign;
    private String issuerSign;
    private String imgCapture;
    private Boolean returnStatus;
    private String DisplayId;
    private LocalDateTime lastUpdatedDate;

}
