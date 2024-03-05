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
    private Long id;
    @Column(name="file_type")
    private String fileType;
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
