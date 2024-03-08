package com.example.FrontDesk_BE.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate issueDate;

    @Column(name = "check_in_time", nullable=false)
    private LocalTime inTime;

    @Column(name = "employee_name", nullable=false)
    private String empName;

    @Column(name = "employee_id", nullable=false)
    private Long empId;

    @Column(name = "checkout_time")
    private LocalTime outTime;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(nullable=false)
    private String idIssuer;

    @JoinColumn(name = "temp_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private TempIDCard tempIdCard;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name="receiver_ft")
    private String receiverFileType;

    @Column(name="issuer_ft")
    private String issuerFileType;

    @Column(name="img_ft")
    private String imgFileType;
    @Lob
    @Column(name="receiver_Sign")
    private byte[] receiverSign;
    @Lob
    @Column(name="issuer_Sign")
    private byte[] issuerSign;
    @Lob
    @Column(name="imgCapture")
    private byte[] imgCapture;

}
