package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FrontDesk_BE.entity.TempIDCard;

public interface VisitorRepository extends JpaRepository<Visitor,Long> {

}
