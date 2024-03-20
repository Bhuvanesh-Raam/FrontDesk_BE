package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoriesRepository extends JpaRepository<Accessory, Long> {
}
