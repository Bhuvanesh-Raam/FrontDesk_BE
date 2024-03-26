package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FrontDesk_BE.entity.TempIDCard;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,Long> {

    List<Visitor> findAllByIssueDateBetween(LocalDate startDate, LocalDate endDate);
}
