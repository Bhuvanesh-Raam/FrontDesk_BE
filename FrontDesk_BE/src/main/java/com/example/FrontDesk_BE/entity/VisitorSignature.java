package com.example.FrontDesk_BE.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="visitor_signature")
public class VisitorSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="issuer_ft")
    private String issuerFileType;
    @Column(name="receiver_ft")
    private String receiverFileType;
    @Column(name="img_ft")
    private String imgFileType;
    @Lob
    @Column(name="receiver_sign")
    private byte[] receiverSign;
    @Lob
    @Column(name="issuer_sign")
    private byte[] issuerSign;
    @Lob
    @Column(name="img_capture")
    private byte[] imgCapture;

    @OneToOne
    @JoinColumn(name="visitor_id")
    private Visitor visitor;
}
