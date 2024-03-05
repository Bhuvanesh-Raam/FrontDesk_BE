package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ErrorCode;
import com.example.FrontDesk_BE.dto.ZohoEmployeeDetail;
import com.example.FrontDesk_BE.dto.ZohoSearchParam;
import com.example.FrontDesk_BE.dto.ZohoSearchResponseData;
import com.example.FrontDesk_BE.exception.ZohoPeopleServiceException;
import com.example.FrontDesk_BE.utils.ZohoTokenProvider;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.constants.ErrorMessages;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@Service
public class ZohoPeopleService {
    @Autowired
    private ZohoTokenProvider zohoTokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZohoPeopleService.class);

    public List<ZohoEmployeeDetail> searchEmployee(String text) {
        List<ZohoEmployeeDetail> result = new ArrayList<>();
        if (StringUtils.isBlank(text))
            return result;
        try {
            URI uri = new URIBuilder(ApplicationConstants.ZOHO_PEOPLE_URL).build();

            RestTemplate restTemplate = new RestTemplate(zohoTokenProvider.getClientHttpRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-cache");
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Authorization", "Bearer " + zohoTokenProvider.getAuthToken());

            ZohoSearchParam searchParam =ZohoSearchParam.getRequestBody(text,text.matches("^\\d*$"));
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("searchParams", new Gson().toJson(searchParam));

            final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                    params, headers);
            ResponseEntity<ZohoSearchResponseData> entity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
                    ZohoSearchResponseData.class);

            if (entity.getStatusCodeValue() == 200 && entity.getBody() != null
                    && entity.getBody().getResponse() != null) {
                result = entity.getBody().getResponseData();
                return result;
            } else {
                LOGGER.error(ErrorMessages.ZOHO_SEARCH_SERVICE_FAILURE, entity.getBody().toString());
                throw new ZohoPeopleServiceException(ErrorCode.ZOHO_FAILURE_RESPONSE,
                        ErrorMessages.ZOHO_SEARCH_SERVICE_FAILURE, null);
            }
        } catch (ZohoPeopleServiceException exception) {
            throw exception;
        } catch (Exception e) {
            HttpClientErrorException ex=(HttpClientErrorException) e;
            if(ex!=null && ex.getRawStatusCode()==401) {
                LOGGER.error(ErrorMessages.ZOHO_UNAUTHORIZED_ERROR);
                this.zohoTokenProvider.updateRefershToken();
            }else {
                LOGGER.error(ErrorMessages.ZOHO_SEARCH_SERVICE_ERROR, e);
                throw new ZohoPeopleServiceException(ErrorCode.ZOHO_INTERNAL_SERVER,
                        ErrorMessages.ZOHO_SEARCH_SERVICE_ERROR, e.getCause());
            }
        }
        return result;
    }

}
