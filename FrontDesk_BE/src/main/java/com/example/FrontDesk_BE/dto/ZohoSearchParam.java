package com.example.FrontDesk_BE.dto;

import lombok.Data;

@Data
public class ZohoSearchParam {
    private String searchField;
    private String searchOperator;
    private String searchText;

    public ZohoSearchParam(String searchText,String field) {
        this.searchField=field;
        this.searchOperator="Contains";
        this.searchText=searchText;
    }

    public static ZohoSearchParam searchByName(String searchText) {
        return new ZohoSearchParam(searchText,"Full_Name");
    }

    public static ZohoSearchParam searchByEmpId(String searchText) {
        return new ZohoSearchParam(searchText,"EmployeeID");
    }

    public static ZohoSearchParam getRequestBody(String searchText,boolean IsEmpId)
    {
        if(IsEmpId)
            return searchByEmpId(searchText);
        else
            return searchByName(searchText);
    }
}
