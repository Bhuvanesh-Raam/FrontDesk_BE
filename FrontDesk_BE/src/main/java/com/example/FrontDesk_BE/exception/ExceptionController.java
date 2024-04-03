package com.example.FrontDesk_BE.exception;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.constants.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException ex){
        ErrorResponse errorResponse=new ErrorResponse(ApplicationConstants.FAILURE,ex.getCode(),ex.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ZohoPeopleServiceException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(ZohoPeopleServiceException exc){
        ErrorResponse errorResponse=new ErrorResponse(ApplicationConstants.FAILURE, exc.getCode(), exc.getErrorMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
