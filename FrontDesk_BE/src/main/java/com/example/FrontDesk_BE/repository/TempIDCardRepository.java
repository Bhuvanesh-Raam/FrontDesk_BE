package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.TempIDCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempIDCardRepository extends JpaRepository<TempIDCard,Long> {
    List<TempIDCard> findByInUseFalse();
}
