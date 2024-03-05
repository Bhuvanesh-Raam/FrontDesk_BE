package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.EmpDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpDetailRepository extends JpaRepository<EmpDetail,Long> {
}
