package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.VisitorSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorSignatureRepository extends JpaRepository<VisitorSignature, Long> {
}
