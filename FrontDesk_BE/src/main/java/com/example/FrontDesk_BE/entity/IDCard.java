package com.example.FrontDesk_BE.entity;


import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name="id_card")
@NoArgsConstructor
public class IDCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "display_id")
    private String displayId;

    @Column(nullable=false)
//    @NotBlank
    /*@Pattern(regexp = "^((((0[13578])|([13578])|(1[02]))[\\/](([1-9])|([0-2][0-9])|(3[01])))|(((0[469])|([469])|(11))[\\/](([1-9])|([0-2][0-9])|(30)))|((2|02)[\\/](([1-9])|([0-2][0-9]))))[\\/]\\d{4}$|^\\d{4}$",message = "Invalid Date Format")*/
    private LocalDate issueDate;

    @Column(name = "check_in_time", nullable=false)
//    @NotBlank
    /*@Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$",message ="Invalid Time Format")*/
    private LocalTime inTime;

    @Column(name = "employee_name", nullable=false)
//    @NotBlank(message = "Employee Name should not be blank")
    private String empName;

    @Column(name = "employee_id", nullable=false)
//    @NotBlank(message = "Employee ID should not be blank")
    private Long empId;

    @Column(name = "checkout_time")
    /*@Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$", message ="Invalid Time Format")*/
    private LocalTime outTime;

    @Column(name = "return_date")
    /*@Pattern(regexp = "^((((0[13578])|([13578])|(1[02]))[\\/](([1-9])|([0-2][0-9])|(3[01])))|(((0[469])|([469])|(11))[\\/](([1-9])|([0-2][0-9])|(30)))|((2|02)[\\/](([1-9])|([0-2][0-9]))))[\\/]\\d{4}$|^\\d{4}$", message ="Invalid Date Format")*/
    private LocalDate returnDate;

    @Column(nullable=false)
//    @NotBlank(message = "ID Issuer name should not be blank")
    private String idIssuer;

    @JoinColumn(name = "temp_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
//    @NotBlank(message = "Temporary ID Card Number should not be blank")
    private TempIDCard tempIdCard;

}
