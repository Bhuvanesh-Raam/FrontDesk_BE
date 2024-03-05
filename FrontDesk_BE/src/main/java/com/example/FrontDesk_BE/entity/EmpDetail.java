package com.example.FrontDesk_BE.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="emp_details")
public class EmpDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "department")
    private String department;

}
