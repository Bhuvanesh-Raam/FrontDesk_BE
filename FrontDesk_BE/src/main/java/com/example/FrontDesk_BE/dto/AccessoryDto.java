package com.example.FrontDesk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessoryDto {
    private String type;
    private String name;
    private String serialNo;
    private Integer quantity;
}
