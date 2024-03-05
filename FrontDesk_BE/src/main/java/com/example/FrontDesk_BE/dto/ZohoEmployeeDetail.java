package com.example.FrontDesk_BE.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties
@Data
public class ZohoEmployeeDetail {
    public String Designation;
    public String EmailID;
    public String FirstName;
    public String Employeestatus;
    public String Full_Name;
    public String Department;
    public String LastName;
    public String EmployeeID;
    public String Reporting_To;
}
