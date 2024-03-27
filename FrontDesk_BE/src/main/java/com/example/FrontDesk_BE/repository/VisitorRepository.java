package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,Long> {

    Page<Visitor> findByClockedOutStatus(boolean clockedOutStatus, Pageable pageable);

    Page<Visitor> findByVisitorNameContainingIgnoreCase(String searchParam, Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE CAST(v.visitorId AS STRING) LIKE %:visitorIdPattern%")
    Page<Visitor> findByPartialVisitorId(@Param("visitorIdPattern")String visitorIdPattern, Pageable pageable);

    Page<Visitor> findByVisitorNameContainingIgnoreCaseAndClockedOutStatus(String searchParam, Boolean clockedOutStatus, Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE CAST(v.visitorId AS STRING) LIKE %:visitorIdPattern% AND v.clockedOutStatus=:clockedOutStatus")
    Page<Visitor> findByPartialVisitorIdAndClockedOutStatus(String visitorIdPattern, Boolean clockedOutStatus, Pageable pageable);

    List<Visitor> findAllByIssueDateBetween(LocalDate startDate, LocalDate endDate);
}
