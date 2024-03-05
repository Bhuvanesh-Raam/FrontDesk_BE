package com.example.FrontDesk_BE.exception;

import lombok.Data;

@Data
public class ZohoPeopleServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String errorMessage;

    public ZohoPeopleServiceException(String code,String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code=code;
        this.errorMessage = errorMessage;
    }

}
