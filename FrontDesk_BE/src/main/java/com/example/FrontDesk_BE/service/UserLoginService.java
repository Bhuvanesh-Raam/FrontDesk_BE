package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ErrorCode;
import com.example.FrontDesk_BE.constants.ErrorMessages;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.dto.UserLoginDto;
import com.example.FrontDesk_BE.entity.UserLogin;
import com.example.FrontDesk_BE.exception.CustomException;
import com.example.FrontDesk_BE.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserLoginRepository userLoginRepository;
    public ResponseEntity<String> userLogin(UserLoginDto userLoginDto){
        try{
            UserLogin userName=userLoginRepository.findByUserName(userLoginDto.getUserName());
            if(userName!=null && userName.getPassword().equals(userLoginDto.getPassword()))
            {
                return ResponseEntity.ok("Login Success");
            }
            else{
                return ResponseEntity.ok("Invalid Username or password");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new CustomException(ErrorCode.LOGIN_ERROR, ErrorMessages.LOGIN_ERROR);
        }
    }
}
