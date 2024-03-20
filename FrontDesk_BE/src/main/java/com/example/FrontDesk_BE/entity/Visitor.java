package com.example.FrontDesk_BE.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name="visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "display_id")
    private String displayId;

    @Column(nullable=false)
    private LocalDate issueDate;

    @Column(name = "check_in_time", nullable=false)
    private LocalTime inTime;

    @Column(name = "visitor_name", nullable=false)
    private String visitorName;

    @Column(name = "visitor_type", nullable=false)
    private String visitorType;

    @Column(name = "employee_name")
    private String empName;

    @Column(name = "employee_id")
    private Long empId;

    @Column(name = "checkout_time")
    private LocalTime outTime;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "id_Issuer", nullable=false)
    private String idIssuer;

    @JoinColumn(name = "temp_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private TempIDCard tempIdCard;

    @Column(name = "last_updated_date")
    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    @OneToOne(mappedBy = "visitor",cascade = CascadeType.ALL)
    private VisitorSignature visitorSignature;

    @Column(name="id_return_Status",nullable = false)
    private Boolean idReturnStatus;

    @Column(name="clocked_out_status")
    private Boolean clockedOutStatus;

}
