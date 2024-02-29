package com.example.FrontDesk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempIDCardDto {
    private Long id;
    private String idName;
    private String odcAccessCardNumber;
    private String sezAccessCardNumber;
    private Boolean inUse;
}
