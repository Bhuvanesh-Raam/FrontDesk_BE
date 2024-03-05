package com.example.FrontDesk_BE.utils;

import com.example.FrontDesk_BE.constants.ErrorCode;
import com.example.FrontDesk_BE.constants.ErrorMessages;
import com.example.FrontDesk_BE.dto.TokenResponse;

import com.example.FrontDesk_BE.exception.ZohoPeopleServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.FrontDesk_BE.constants.ApplicationConstants;

import java.net.URI;

@Component
public class ZohoTokenProvider {
    private static final Logger LOGGER= LoggerFactory.getLogger(ZohoTokenProvider.class);

    @Value("${zoho.clientId}")
    private String clientId;

    @Value("${zoho.clientSecret}")
    private String clientSecret;

    @Value("${zoho.refreshToken}")
    private String refreshToken;

    @Cacheable("zohoOauthToken")
    public String getAuthToken() {
        LOGGER.info(ErrorMessages.ZOHO_AUTH_TOKEN_MSG);
        TokenResponse tokenResponse = invokeOauthCall();
        if (tokenResponse != null && tokenResponse.getAccess_token() != null) {
            return tokenResponse.getAccess_token();
        }
        LOGGER.error(ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE);
        return null;
    }

    @CachePut(value = "zohoOauthToken")
    public String updateRefershToken() {
        LOGGER.info(ErrorMessages.ZOHO_REFRESH_TOKEN_MSG);
        TokenResponse tokenResponse = invokeOauthCall();
        if (tokenResponse != null && tokenResponse.getAccess_token() != null) {
            return tokenResponse.getAccess_token();
        }
        LOGGER.error(ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE);
        return null;
    }

    @CacheEvict(value = "zohoOauthToken", allEntries = true)
    @Scheduled(fixedRateString = "${spring.caching.zohoOauthTokenTTL}")
    public void clearTokenCache() {
        LOGGER.info(ErrorMessages.ZOHO_TOKEN_CACHE);
    }

    public TokenResponse invokeOauthCall() {
        try {
            URI uri = new URIBuilder(ApplicationConstants.ZOHO_OAUTH_URL).addParameter("grant_type", "refresh_token")
                    .addParameter("client_id", this.clientId).addParameter("client_secret", this.clientSecret)
                    .addParameter("refresh_token", this.refreshToken).build();

            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-cache");
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntitys = new HttpEntity<>(headers);
            ResponseEntity<TokenResponse> entity = restTemplate.exchange(uri, HttpMethod.POST, requestEntitys,
                    TokenResponse.class);
            if (entity.getStatusCodeValue() == 200 && entity.getBody() != null
                    && StringUtils.isNotBlank(entity.getBody().getAccess_token())) {
                return entity.getBody();
            } else {
                LOGGER.error(ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE, entity.getBody().toString());
                throw new ZohoPeopleServiceException(ErrorCode.ZOHO_FAILURE_RESPONSE,
                        ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE, null);
            }
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE, e);
            throw new ZohoPeopleServiceException(ErrorCode.ZOHO_INTERNAL_SERVER, ErrorMessages.ZOHO_OAUTH_TOKEN_FAILURE,
                    e.getCause());
        }
    }

    public SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = null;
        try {
            clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            // Connect timeout
            clientHttpRequestFactory.setConnectTimeout(10000);
            // Read timeout
            clientHttpRequestFactory.setReadTimeout(10000);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.HTTP_CLIENT_EXCEPTION + e);
        }
        return clientHttpRequestFactory;
    }

}
