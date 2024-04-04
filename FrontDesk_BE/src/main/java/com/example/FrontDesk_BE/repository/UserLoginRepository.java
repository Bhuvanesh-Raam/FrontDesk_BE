package com.example.FrontDesk_BE.repository;

import com.example.FrontDesk_BE.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {
    UserLogin findByUserName(String userName);
}
