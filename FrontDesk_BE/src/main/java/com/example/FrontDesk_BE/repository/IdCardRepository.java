package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.IDCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRepository extends JpaRepository<IDCard, Long> {
    Page<IDCard> findAll(Pageable pageable);
}
