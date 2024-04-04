package com.example.FrontDesk_BE.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_login_credentials")
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name="password")
    private String password;
}
