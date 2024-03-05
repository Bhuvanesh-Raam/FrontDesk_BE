package com.example.FrontDesk_BE.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ZohoSearchResponseData {
    Response response;

    public List<ZohoEmployeeDetail> getResponseData() {
        if (this.response != null && this.response.getResult() != null)
            return this.response.getResultData();
        else
            return new ArrayList<>();
    }
}
@Data
class Response {
    private List<EmployeeObject> result;
    private String message;
    private float status;

    public List<ZohoEmployeeDetail> getResultData() {
        List<ZohoEmployeeDetail> empDetails = new ArrayList<>();
        if (!this.result.isEmpty()) {
            for (EmployeeObject employee : result) {
                if (!employee.getData().isEmpty())
                    empDetails.addAll(employee.getData());
            }
        }
        return empDetails;
    }
}
@Data
class EmployeeObject {
     private Map<String, List<ZohoEmployeeDetail>> zohoId = new HashMap<>();

     @JsonAnySetter
     public void setDynamicField(String key, List<ZohoEmployeeDetail> value) {
            zohoId.put(key, value);
        }
     public boolean isNoTEmpty() {
            return !this.zohoId.isEmpty();
        }
     public List<ZohoEmployeeDetail> getData() {
        List<ZohoEmployeeDetail> result = new ArrayList<>();
        for (List<ZohoEmployeeDetail> employeeDetails : zohoId.values()) {
                result.addAll(employeeDetails);
        }
        return result;
     }
}
