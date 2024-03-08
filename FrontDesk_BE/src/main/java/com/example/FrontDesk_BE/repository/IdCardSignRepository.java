package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.IdSignature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdCardSignRepository extends JpaRepository<IdSignature,Long> {
}
