package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.repository.EmpDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpService {
private final EmpDetailRepository empDetailRepository;


}
