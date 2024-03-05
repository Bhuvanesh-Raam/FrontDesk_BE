package com.example.FrontDesk_BE.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private String api_domain;
    private String token_type;
    private float expires_in;
}
