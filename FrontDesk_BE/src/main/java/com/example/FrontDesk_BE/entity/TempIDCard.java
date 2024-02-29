package com.example.FrontDesk_BE.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="temp_id_card")
public class TempIDCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;
    @Column(name = "id_name")
    private String idName;
    @Column(name="odc_access_number")
    private String odcAccessCardNumber;
    @Column(name="sez_access_number")
    private String sezAccessCardNumber;
    @Column(name="in_use")
    private Boolean inUse;
}
