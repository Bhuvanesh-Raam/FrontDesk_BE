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
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    @Query("SELECT v FROM Visitor v WHERE CAST(v.empId AS STRING) LIKE %:empIdPattern%")
    Page<Visitor> findByPartialEmpId(@Param("empIdPattern") String empIdPattern, Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE CAST(v.empId AS STRING) LIKE %:empIdPattern% AND v.clockedOutStatus=:clockedOutStatus")
    Page<Visitor> findByPartialEmpIdAndClockedOutStatus(String empIdPattern, Boolean clockedOutStatus,
            Pageable pageable);

    Page<Visitor> findByClockedOutStatus(boolean clockedOutStatus, Pageable pageable);

    Page<Visitor> findByVisitorNameContainingIgnoreCase(String searchParam, Pageable pageable);

    Page<Visitor> findByVisitorNameContainingIgnoreCaseAndClockedOutStatus(String searchParam, Boolean clockedOutStatus,
            Pageable pageable);

    List<Visitor> findAllByIssueDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Visitor> findByEmpNameContainingIgnoreCase(String searchString, Pageable pageable);

    Page<Visitor> findByEmpNameContainingIgnoreCaseAndClockedOutStatus(String searchString, Boolean clockedOutStatus, Pageable pageable);
}
