package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoriesRepository extends JpaRepository<Accessory, Long> {
}
