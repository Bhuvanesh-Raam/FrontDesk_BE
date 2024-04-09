package com.example.FrontDesk_BE.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "last_updated_date")
    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    @OneToOne(mappedBy = "idCard",cascade = CascadeType.ALL)
    private IdCardSignature idCardSignature;

    @Column(name="returnStatus",nullable = false)
    private Boolean returnStatus;

}
