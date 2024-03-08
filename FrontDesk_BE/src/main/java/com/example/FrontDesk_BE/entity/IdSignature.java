package com.example.FrontDesk_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="id_signature")
public class IdSignature {
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
}
