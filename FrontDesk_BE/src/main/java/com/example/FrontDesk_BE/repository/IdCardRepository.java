package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRepository extends JpaRepository<IDCard, Long> {
    Page<IDCard> findAll(Pageable pageable);

    Page<IDCard> findByReturnStatus(boolean returnStatus, Pageable pageable);

    Page<IDCard> findByEmpNameContainingIgnoreCase(String searchParam, Pageable pageable);
    @Query("SELECT i FROM IDCard i WHERE CAST(i.empId AS STRING) LIKE   %:empIdPattern%")
    Page<IDCard> findByPartialEmpId(@Param("empIdPattern")String empIdPattern, Pageable pageable);
}
